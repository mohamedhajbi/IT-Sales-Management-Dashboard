package org.ms.factureservice.feign;
import java.util.List;

import org.ms.factureservice.model.Client;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "Client-service", url = "http://localhost:5000/api")
public interface ClientServiceClient {
	@GetMapping
    List<Client> getAllClients();
    @GetMapping(path = "/Clients/{id}")
    Client findClientById(@PathVariable(name = "id") Long id);
}
