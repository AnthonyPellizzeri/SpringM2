package org.miage.clientservice.boundary;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "degradationService")
public interface DegradationRestClient {

    @GetMapping(value = "/degradations/client/{idClient}")
    ResponseEntity<?> ListClient(@PathVariable("idClient") String id);
}
