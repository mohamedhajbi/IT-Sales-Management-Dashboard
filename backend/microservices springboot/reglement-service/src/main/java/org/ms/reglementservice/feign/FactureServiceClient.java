package org.ms.reglementservice.feign;

import java.util.List;

import org.ms.reglementservice.model.Facture;
import org.ms.reglementservice.model.FactureLigne;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "facture-service", url = "http://localhost:8083/api/factures")
public interface FactureServiceClient {
    @GetMapping("/{id}")
    Facture getFactureById(@PathVariable(name = "id") Long id);

    @PutMapping("/{id}/update-status")
    void updateFactureStatus(@PathVariable(name = "id") Long id, @RequestParam(name = "status") String status);

    @GetMapping("/{id}/total")
    double getFactureTotal(@PathVariable(name = "id") Long id);

    @GetMapping
    List<Facture> getAllFactures();
}


