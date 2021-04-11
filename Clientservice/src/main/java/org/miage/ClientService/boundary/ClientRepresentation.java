package org.miage.clientservice.boundary;

import lombok.RequiredArgsConstructor;
import org.miage.clientservice.config.UserPrincipal;
import org.miage.clientservice.entity.client.Client;
import org.miage.clientservice.entity.client.ClientInput;
import org.miage.clientservice.entity.client.Statut;
import org.miage.clientservice.entity.Oauth.UserOauth;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.Field;
import java.net.URI;
import java.util.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/client", produces = APPLICATION_JSON_VALUE)
@ExposesResourceFor(Client.class)
public class ClientRepresentation {

    private final ClientRessource clients;
    private final OauthRestClient oauthRestClient;
    private final DegradationRestClient degradationRestClient;

    // GET all
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping // la méthode va répondre à la méthode HTTP GET
    public ResponseEntity<?> getAllClients(){
        Iterable<Client> allClients = clients.findAll();
        return ResponseEntity.ok(clientToResource(allClients));
    }

    // GET one
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value="/{clientId}")
    public ResponseEntity<?> getClient(@PathVariable("clientId") String id) {
        return Optional.of(clients.findById(id))
                .filter(Optional::isPresent)
                .map(i -> ResponseEntity.ok(clientToResource(i.get(), true)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    @Transactional
    public ResponseEntity<?> saveClient(@RequestBody @Valid ClientInput client){
        String id= UUID.randomUUID().toString();
        Client client2Save = new Client(
                id,
                client.getFirstname(),
                client.getLastname(),
                client.getCity(),
                client.getPostalcode(),
                Statut.ACTIF
        );
        Client saved = clients.save(client2Save);
        oauthRestClient.createClient(new UserOauth(id,client.getFirstname(),client.getPassword()));
        URI location = linkTo(ClientRepresentation.class).slash(saved.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value="/{clientId}")
    @Transactional
    public ResponseEntity<?> deleteIntervenant(@PathVariable("clientId") String id){
        Optional<Client> client = clients.findById(id);
        if(client.isPresent()){
            clients.delete(client.get());
        }
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/{clientId}")
    @Transactional
    public ResponseEntity<?> updateIntervenant(@PathVariable("clientId") String id,
                                               @RequestBody Client client){
        Optional<Client> body = Optional.ofNullable(client);
        if(!body.isPresent()){
            return ResponseEntity.badRequest().build();
        }
        if(!clients.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        client.setId(id);
        Client result = clients.save(client);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping(value = "/{clientId}")
    @Transactional
    public ResponseEntity<?> updatePartielClient(@RequestBody Map<Object,Object> champsJson,
                                                      @PathVariable("clientId") String clientId) {
        Optional<Client> body = clients.findById(clientId);
        if(body.isPresent()){
            Client client = body.get();
            champsJson.forEach((f,v) -> {
                Field champ = ReflectionUtils.findField(Client.class, f.toString());
                champ.setAccessible(true);
                ReflectionUtils.setField(champ, client, v);
            });
            client.setId(clientId);
            clients.save(client);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/degradations")
    @Transactional
    public ResponseEntity<?> GetAllDegradation(){
        return ResponseEntity.ok(degradationRestClient.ListClient(((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()));
    }

    private EntityModel<Client> clientToResource(Client client, Boolean isCollection) {
        var selfLink = linkTo(ClientRepresentation.class).slash(client.getId()).withSelfRel();
        if (isCollection) {
            Link collectionLink = linkTo(methodOn(ClientRepresentation.class).getAllClients()).withRel("collection");
            return EntityModel.of(client, selfLink, collectionLink);
        } else {
            return EntityModel.of(client, selfLink);
        }
    }

    private CollectionModel<EntityModel<Client>> clientToResource(Iterable<Client> client){
        Link selfLink = linkTo(methodOn(ClientRepresentation.class).getAllClients()).withSelfRel();
        List<EntityModel<Client>> clientResource = new ArrayList<>();
        client.forEach(i ->
                clientResource.add(clientToResource(i,false)));
        return CollectionModel.of(clientResource, selfLink);
    }
}
