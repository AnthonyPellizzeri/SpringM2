package org.miage.degradationservice.services;

import lombok.RequiredArgsConstructor;
import org.miage.degradationservice.boundary.action.ActionRessource;
import org.miage.degradationservice.boundary.degradation.DegradationRessource;
import org.miage.degradationservice.config.UserPrincipal;
import org.miage.degradationservice.entity.action.Action;
import org.miage.degradationservice.entity.degradation.ChoiceInput;
import org.miage.degradationservice.entity.action.Etat;
import org.miage.degradationservice.entity.action.StatutAction;
import org.miage.degradationservice.entity.degradation.StatutDegradation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ServiceAction {
    private final DegradationRessource degradations;
    private final ActionRessource actions;
    private final ServiceMail ServiceMails;

    public ResponseEntity<?> action(String id, ChoiceInput action){
        // if last action is finish
        if(getLastAction(id).getStatut().equals(StatutAction.TERMINEE)
                // if this degradation is active
                && degradations.findById(id).get().getStatut().equals(StatutDegradation.ACTIF)){
            String role= getRole();
            switch (role){
                case "ROLE_ADMIN":
                    switch (getLastAction(id).getEtat()){
                        case ETUDE: return checkDamageReport(id, action);
                        case ETUDE_DETAILLEE: return maintenance(id, action);
                        case APPROUVEE: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                        case REJET: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                    }
                    break;
                    case "ROLE_CLERK": return checkDamageReport(id, action);
                case "ROLE_MANAGER": return maintenance(id, action);
                //case "ROLE_CITIZEN": return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                //case "ROLE_ERP": return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                //case "ROLE_EMPLOYE": return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                //case "ROLE_PARTENER": return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                default: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    public ResponseEntity<?> ValideAction(String id){
        if(degradations.findById(id).get().getStatut().equals(StatutDegradation.ACTIF)
            &&
                getLastAction(id).getStatut()== StatutAction.ACTIVE){
            if(
                    // if is respo or is admin or is (clerk and action is etude)
                    getLastAction(id).getResponsable().equals(((UserPrincipal)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId())
                    || getRole().equals("ROLE_ADMIN")
                            || (getRole().equals("ROLE_CLERK") && getLastAction(id).getEtat().equals(Etat.ETUDE))
            ){
                //set action status
                Action a= actions.findById(getLastAction(id).getId()).get();
                a.setStatut(StatutAction.TERMINEE);
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    public ResponseEntity<?> checkDamageReport(String id, ChoiceInput choice){
        if(degradations.findById(id).get().getStatut().equals(StatutDegradation.ACTIF)){
            Action act= new Action(
                    UUID.randomUUID().toString(),
                    Etat.ETUDE,
                    StatutAction.ACTIVE,
                    ((UserPrincipal)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId(),
                    LocalDateTime.now());
            if(getLastAction(id).getEtat()==Etat.ETUDE){
                return Optional.ofNullable(degradations.findById(id))
                        .filter(Optional::isPresent)
                        .map(i -> {
                            switch (choice.getChoice()){
                                case "refuser": act.setEtat(Etat.REJET);
                                    degradations.findById(id).get().getAction().add(act);
                                    break;
                                case "valider": act.setEtat(Etat.APPROUVEE);
                                    degradations.findById(id).get().getAction().add(act);
                                    break;
                                case "detail": act.setEtat(Etat.ETUDE_DETAILLEE);
                                    degradations.findById(id).get().getAction().add(act);
                                    break;
                                default: return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                            }
                            return ResponseEntity.ok().build();
                        })
                        .orElse(ResponseEntity.notFound().build());
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    public ResponseEntity<?> maintenance(String id, ChoiceInput choice){
        if(degradations.findById(id).get().getStatut().equals(StatutDegradation.ACTIF)){
            Action act= new Action(
                    UUID.randomUUID().toString(),
                    Etat.ETUDE,
                    StatutAction.ACTIVE,
                    ((UserPrincipal)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId(),
                    LocalDateTime.now());
            if(getLastAction(id).getEtat()==Etat.ETUDE_DETAILLEE){
                return Optional.ofNullable(degradations.findById(id))
                        .filter(Optional::isPresent)
                        .map(i -> {
                            switch (choice.getChoice()){
                                case "yes": act.setEtat(Etat.APPROUVEE);
                                    degradations.findById(id).get().getAction().add(act);
                                    break;
                                case "non": act.setEtat(Etat.ETUDE);
                                    degradations.findById(id).get().getAction().add(act);
                                    break;
                                default: return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                            }
                            return ResponseEntity.ok().build();
                        })
                        .orElse(ResponseEntity.notFound().build());
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    public Action getLastAction(String id){
        return Collections.max(degradations.findById(id).get().getAction(), Comparator.comparing(s -> s.getDate()));
    }

    public String getRole(){
        return String.valueOf(SecurityContextHolder.getContext().getAuthentication().getAuthorities().toArray()[0]);
    }
}
