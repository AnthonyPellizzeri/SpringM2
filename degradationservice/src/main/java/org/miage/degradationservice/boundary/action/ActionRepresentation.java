package org.miage.degradationservice.boundary.action;

import lombok.RequiredArgsConstructor;
import org.miage.degradationservice.entity.action.Action;
import org.miage.degradationservice.entity.action.ActionInput;
import org.miage.degradationservice.entity.degradation.ChoiceInput;
import org.miage.degradationservice.entity.degradation.Degradation;
import org.miage.degradationservice.services.ServiceAction;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/actions", produces = APPLICATION_JSON_VALUE)
@ExposesResourceFor(Degradation.class)
public class ActionRepresentation {
    private final ActionRessource Actions;
    private final ServiceAction ServiceActions;


    // GET all
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping // la méthode va répondre à la méthode HTTP GET
    public ResponseEntity<?> getAllDegradation(){
        Iterable<Action> allAction = Actions.findAll();
        return ResponseEntity.ok(actionToResource(allAction));
    }

    // GET one
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value="/{idAction}")
    public ResponseEntity<?> getDegradation(@PathVariable("idAction") String id) {
        return Optional.ofNullable(Actions.findById(id))
                .filter(Optional::isPresent)
                .map(i -> ResponseEntity.ok(actionToResource(i.get(), true)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/{idAction}")
    @Transactional
    public ResponseEntity<?> lastState(@PathVariable("idAction") String id, @RequestBody ActionInput action){
        return Optional.ofNullable(Actions.findById(id))
                .filter(Optional::isPresent)
                .map(i -> {
                   i.get().setEtat(action.getEtat());
                    i.get().setStatut(action.getStatus());
                    i.get().setResponsable(action.getResponsable());
                    i.get().setDate(action.getDate());
                    return ResponseEntity.ok(actionToResource(i.get(), true));
                }
                )
                .orElse(ResponseEntity.notFound().build());
    }

    private EntityModel<Action> actionToResource(Action degrad, Boolean isCollection) {
        var selfLink = linkTo(ActionRepresentation.class).slash(degrad.getId()).withSelfRel();
        if (isCollection) {
            Link collectionLink = linkTo(methodOn(ActionRepresentation.class).getAllDegradation()).withRel("collection");
            return EntityModel.of(degrad, selfLink, collectionLink);
        } else {
            return EntityModel.of(degrad, selfLink);
        }
    }

    private CollectionModel<EntityModel<Action>> actionToResource(Iterable<Action> degrad){
        Link selfLink = linkTo(methodOn(ActionRepresentation.class).getAllDegradation()).withSelfRel();
        List<EntityModel<Action>> clientResource = new ArrayList<>();
        degrad.forEach(i ->
                clientResource.add(actionToResource(i,false)));
        return CollectionModel.of(clientResource, selfLink);
    }
}
