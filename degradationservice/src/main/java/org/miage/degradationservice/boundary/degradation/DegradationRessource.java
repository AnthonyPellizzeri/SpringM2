package org.miage.degradationservice.boundary.degradation;

import org.miage.degradationservice.entity.degradation.Degradation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DegradationRessource extends JpaRepository<Degradation,String> {
}
