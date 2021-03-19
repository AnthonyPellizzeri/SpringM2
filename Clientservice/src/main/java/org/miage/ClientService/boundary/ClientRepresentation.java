package org.miage.Clientservice.Clientservice.boundary;

import org.miage.Clientservice.Clientservice.entity.client;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/client", produces = APPLICATION_JSON_VALUE)
@ExposesResourceFor(client.class)
public class ClientRepresentation {
    private ClientRessource clients;

    public ClientRepresentation(ClientRessource clients){this.clients=clients; }
    @GetMapping
    public ResponseEntity getOne(){
        return ResponseEntity.ok(clients.findAll());
    }
}
