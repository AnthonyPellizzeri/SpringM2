package org.miage.degradationservice.services;

import lombok.RequiredArgsConstructor;
import org.miage.degradationservice.boundary.degradation.DegradationRepresentation;
import org.miage.degradationservice.boundary.degradation.DegradationRessource;
import org.miage.degradationservice.config.UserPrincipal;
import org.miage.degradationservice.entity.action.Action;
import org.miage.degradationservice.entity.action.Etat;
import org.miage.degradationservice.entity.action.StatutAction;
import org.miage.degradationservice.entity.degradation.Degradation;
import org.miage.degradationservice.entity.degradation.DegradationInput;
import org.miage.degradationservice.entity.degradation.StatutDegradation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RequiredArgsConstructor
@Service
public class ServiceDegradation {
    private final DegradationRessource degradations;
    private final ServiceAction ServiceActions;

    public ResponseEntity<?> AddDegradation(DegradationInput degrad){
        List<Action> action = new ArrayList<>();
        String respo="ROLE_ANONYMOUS";
        // if is connect
        if(ServiceActions.getRole()!="ROLE_ANONYMOUS"){
            respo = ((UserPrincipal)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        }
        //Add Begin action
        action.add(new Action(UUID.randomUUID().toString(),
                Etat.DEBUT,
                StatutAction.ACTIVE,
                respo,
                LocalDateTime.now()
        ));
        // add study action
        action.add(new Action(UUID.randomUUID().toString(),
                Etat.ETUDE,
                StatutAction.ACTIVE,
                "",
                LocalDateTime.now()
        ));
        // create degradation object
        Degradation client2Save = new Degradation(
                UUID.randomUUID().toString(),
                degrad.getName(),
                degrad.getDescription(),
                StatutDegradation.ACTIF,
                action
        );
        Degradation saved = degradations.save(client2Save);
        URI location = linkTo(DegradationRepresentation.class).slash(saved.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    public ResponseEntity<?> UpdateDegradation(String id, Degradation degrad){
        if(ServiceActions.getLastAction(id).getStatut().equals(StatutDegradation.ACTIF)
        && ServiceActions.getLastAction(id).getEtat().equals(Etat.ETUDE)){
            Optional<Degradation> body = Optional.ofNullable(degrad);
            if(!body.isPresent()){
                return ResponseEntity.badRequest().build();
            }
            if(!degradations.existsById(id)){
                return ResponseEntity.notFound().build();
            }
            degrad.setId(id);
            Degradation result = degradations.save(degrad);
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    public ResponseEntity<?> UpdatePartielDegradation(String id, Degradation degrad){
        if(ServiceActions.getLastAction(id).getEtat().equals(Etat.ETUDE)){
            Optional<Degradation> body = Optional.ofNullable(degrad);
            if(!body.isPresent()){
                return ResponseEntity.badRequest().build();
            }
            if(!degradations.existsById(id)){
                return ResponseEntity.notFound().build();
            }
            degrad.setId(id);
            Degradation result = degradations.save(degrad);
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    public ResponseEntity<?> DeleteDegradation(String id){
        Optional<Degradation> client = degradations.findById(id);
        if(client.isPresent()){
            degradations.findById(id).get().setStatut(StatutDegradation.INACTIF);
        }
        return ResponseEntity.noContent().build();
    }

}
