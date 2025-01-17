package org.ms.factureservice.web;

import org.ms.factureservice.entities.Facture;
import org.ms.factureservice.model.Produit;
import org.ms.factureservice.service.FactureService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/factures")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class FactureController {

    private final FactureService factureService;

    public FactureController(FactureService factureService) {
        this.factureService = factureService;
    }

    @GetMapping("/{id}")
    public Facture getFacture(@PathVariable long id) {
        return factureService.getFullFacture(id);
    }


    @PutMapping("/{id}/update-status")
    public Facture updateFactureStatus(@PathVariable Long id, @RequestParam String status) {
        return factureService.updateFactureStatus(id, status);
    }

    @GetMapping("/{id}/total")
    public double getFactureTotal(@PathVariable Long id) {
        return factureService.calculateFactureTotal(id);
    }
    @GetMapping("/most-loyal-clients")
    public List<Map<String, Object>> getMostLoyalClients() {
        return factureService.getMostLoyalClients();
    }
    @GetMapping("/most-requested-products")
    public List<Map<String, Object>> getMostRequestedProducts() {
        return factureService.getMostRequestedProductsWithDetails();
    }
    
  
    @GetMapping
    public List<Facture> getAllFactures() {
        return factureService.getAllFactures();
    }

    @PostMapping
    public Facture createFacture(@RequestBody Facture newFacture) {
        return factureService.createFacture(newFacture);
    }

    @GetMapping("/chiffre-affaires/global")
    public double getChiffreAffairesGlobal() {
        return factureService.calculateGlobalChiffreAffaires();
    }

    @GetMapping("/chiffre-affaires/annee")
    public double getChiffreAffairesParAnnee(@RequestParam int year) {
        return factureService.calculateChiffreAffairesByYear(year);
    }
    @GetMapping("/most-sold-products")
    public List<Map<String, Object>> getMostSoldProductsGlobal() {
        return factureService.getMostSoldProductsGlobal();
    }

    @GetMapping("/most-sold-products/year")
    public List<Map<String, Object>> getMostSoldProductsByYear(@RequestParam int year) {
        return factureService.getMostSoldProductsByYear(year);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFacture(@PathVariable Long id) {
        try {
            factureService.deleteFacture(id);
            return ResponseEntity.ok("Facture supprimée avec succès !");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/products/out-of-stock")
    public List<Produit> getOutOfStockProducts() {
        return factureService.getOutOfStockProducts();
    }

    @GetMapping("/payees")
    public List<Facture> getAllFacturesPayees() {
        return factureService.getFacturesPayees();
    }

    @GetMapping("/non-reglees")
    public List<Facture> getAllFacturesNonReglees() {
        return factureService.getFacturesNonReglees();
    }



}
