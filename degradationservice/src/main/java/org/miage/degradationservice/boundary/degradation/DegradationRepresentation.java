package org.miage.degradationservice.boundary.degradation;

import lombok.RequiredArgsConstructor;
import org.miage.degradationservice.boundary.action.ActionRessource;
import org.miage.degradationservice.entity.action.Action;
import org.miage.degradationservice.entity.action.Etat;
import org.miage.degradationservice.entity.action.StatutAction;
import org.miage.degradationservice.entity.degradation.ChoiceInput;
import org.miage.degradationservice.entity.degradation.Degradation;
import org.miage.degradationservice.entity.degradation.DegradationInput;
import org.miage.degradationservice.entity.degradation.StatutDegradation;
import org.miage.degradationservice.services.ServiceAction;
import org.miage.degradationservice.services.ServiceDegradation;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/degradations", produces = APPLICATION_JSON_VALUE)
@ExposesResourceFor(Degradation.class)
public class DegradationRepresentation {
    private final DegradationRessource degradations;
    private final ActionRessource actions;
    private final ServiceAction ServiceActions;
    private final ServiceDegradation ServiceDegradation;


    // GET all
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping // la méthode va répondre à la méthode HTTP GET
    public ResponseEntity<?> getAllDegradation(){
        Iterable<Degradation> allClients = degradations.findAll();
        return ResponseEntity.ok(degradationToResource(allClients));
    }

    // GET one
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value="/{degradationId}")
    public ResponseEntity<?> getDegradation(@PathVariable("degradationId") String id) {
        return Optional.ofNullable(degradations.findById(id))
                .filter(Optional::isPresent)
                .map(i -> ResponseEntity.ok(degradationToResource(i.get(), true)))
                .orElse(ResponseEntity.notFound().build());
    }

    //it's possible to add degradation for connected person and not connected
    @PostMapping
    @Transactional
    public ResponseEntity<?> saveDegradation(@RequestBody @Valid DegradationInput degrad){
        return ServiceDegradation.AddDegradation(degrad);

    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_CITIZEN')")
    @PutMapping(value = "/{degradationId}")
    @Transactional
    public ResponseEntity<?> updateDegradation(@PathVariable("degradationId") String id,
                                               @RequestBody Degradation degrad){
        return ServiceDegradation.UpdateDegradation(id,degrad);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping(value = "/statut/{degradationId}")
    @Transactional
    public ResponseEntity<?> updateStatutDegradation(@PathVariable("degradationId") String id){
        if(!degradations.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        degradations.findById(id).get().setStatut(StatutDegradation.INACTIF);
        return ResponseEntity.ok(degradationToResource(degradations.findById(id).get(), true));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value="/{degradationId}")
    @Transactional
    public ResponseEntity<?> deleteDegradation(@PathVariable("degradationId") String id){
        return ServiceDegradation.DeleteDegradation(id);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/action/{id}")
    @Transactional
    public ResponseEntity<?> CreateAction(@PathVariable("id") String id, @RequestBody @Valid ChoiceInput action){
        return ServiceActions.action(id, action);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping(value = "/action/{id}")
    @Transactional
    public ResponseEntity<?> ValdideAction(@PathVariable("id") String id){
        return ServiceActions.ValideAction(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping(value = "/{degradationId}")
    @Transactional
    public ResponseEntity<?> UpdatePartielDegradation(@RequestBody Map<Object,Object> champsJson,
                                                      @PathVariable("degradationId") String id) {
        Optional<Degradation> body = degradations.findById(id);
        if(body.isPresent()){
            Degradation client = body.get();
            champsJson.forEach((f,v) -> {
                Field champ = ReflectionUtils.findField(Degradation.class, f.toString());
                champ.setAccessible(true);
                ReflectionUtils.setField(champ, client, v);
            });
            client.setId(id);
            degradations.save(client);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }

    // GET all degradations for one client
    @PreAuthorize("hasAnyRole('ROLE_CITIZEN','ROLE_ADMIN')")
    @GetMapping("/client/{idClient}")
    public ResponseEntity<?> getAllDegradationForCitizen(@PathVariable("idClient")String idCitizen){
        List<Degradation> degrad= new ArrayList<>();
        degrad = degradations.findAll()
                .stream().filter(
                        f2->f2.getAction()
                                .stream()
                                .anyMatch(f3->f3.getResponsable().equals(idCitizen)
                                        && f3.getEtat().equals(Etat.ETUDE)))
                .collect(Collectors.toList());
        return ResponseEntity.ok(degradationToResource(degrad));
    }

    private EntityModel<Degradation> degradationToResource(Degradation degrad, Boolean isCollection) {
        var selfLink = linkTo(DegradationRepresentation.class).slash(degrad.getId()).withSelfRel();
        if (isCollection) {
            Link collectionLink = linkTo(methodOn(DegradationRepresentation.class).getAllDegradation()).withRel("collection");
            return EntityModel.of(degrad, selfLink, collectionLink);
        } else {
            return EntityModel.of(degrad, selfLink);
        }
    }

    private CollectionModel<EntityModel<Degradation>> degradationToResource(Iterable<Degradation> degrad){
        Link selfLink = linkTo(methodOn(DegradationRepresentation.class).getAllDegradation()).withSelfRel();
        List<EntityModel<Degradation>> clientResource = new ArrayList<>();
        degrad.forEach(i ->
                clientResource.add(degradationToResource(i,false)));
        return CollectionModel.of(clientResource, selfLink);
    }
}
