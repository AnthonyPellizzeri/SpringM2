package org.miage.degradationservice.boundary.action;

import org.miage.degradationservice.entity.action.Action;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionRessource extends JpaRepository<Action,String> {
}
