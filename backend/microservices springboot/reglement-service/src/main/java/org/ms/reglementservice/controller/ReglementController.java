package org.ms.reglementservice.controller;

import org.ms.reglementservice.entities.Reglement;
import org.ms.reglementservice.model.Facture;
import org.ms.reglementservice.service.ReglementService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reglements")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ReglementController {

    private final ReglementService reglementService;

    public ReglementController(ReglementService reglementService) {
        this.reglementService = reglementService;
    }

    @GetMapping
    public List<Reglement> getAllReglements() {
        return reglementService.getAllReglements();
    }

    @GetMapping("/facture/{id}")
    public List<Reglement> getReglementsByFacture(@PathVariable Long id) {
        return reglementService.getReglementsByFacture(id);
    }

    @GetMapping("/reste-global")
    public double getResteGlobal() {
        return reglementService.calculateResteGlobal();
    }

    @PostMapping
    public Reglement createReglement(@RequestBody Reglement reglement) {
        return reglementService.createReglement(reglement);
    }

    @GetMapping("/facture-details/{id}")
    public Facture getFactureDetails(@PathVariable Long id) {
        return reglementService.getFactureDetails(id);
    }
    @GetMapping("/dettes-par-client")
    public List<Map<String, Object>> getDettesParClient() {
        return reglementService.getDettesParClient();
    }


}