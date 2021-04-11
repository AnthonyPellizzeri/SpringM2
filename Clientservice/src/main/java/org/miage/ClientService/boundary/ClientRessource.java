package org.miage.clientservice.boundary;

import org.miage.clientservice.entity.client.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRessource extends JpaRepository<Client,String> {
}
