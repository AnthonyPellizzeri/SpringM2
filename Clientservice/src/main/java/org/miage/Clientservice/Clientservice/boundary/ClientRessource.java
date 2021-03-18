package org.miage.Clientservice.Clientservice.boundary;

import org.miage.Clientservice.Clientservice.entity.client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRessource extends JpaRepository<client,String> {
}
