package org.ms.reglementservice.service;

import org.ms.reglementservice.entities.Reglement;
import org.ms.reglementservice.feign.FactureServiceClient;
import org.ms.reglementservice.model.Facture;
import org.ms.reglementservice.repository.ReglementRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReglementService {

    private final ReglementRepository reglementRepository;
    private final FactureServiceClient factureServiceClient;

    public ReglementService(ReglementRepository reglementRepository, FactureServiceClient factureServiceClient) {
        this.reglementRepository = reglementRepository;
        this.factureServiceClient = factureServiceClient;
    }

    public List<Reglement> getAllReglements() {
        return reglementRepository.findAll();
    }

    public List<Reglement> getReglementsByFacture(Long factureId) {
        return reglementRepository.findByFactureId(factureId);
    }

    public double calculateResteGlobal() {
        List<Facture> factures = factureServiceClient.getAllFactures();

        return factures.stream()
                .mapToDouble(facture -> {
                    List<Reglement> reglements = reglementRepository.findByFactureId(facture.getId());
                    double totalReglements = reglements.stream()
                            .mapToDouble(r -> r.getMontant().doubleValue())
                            .sum();
                    return Math.max(facture.getTotal() - totalReglements, 0);
                })
                .sum();
    }

    public Reglement createReglement(Reglement reglement) {
        // Remplir la date du règlement
        reglement.setDateReglement(LocalDateTime.now());

        // Sauvegarder le règlement
        Reglement savedReglement = reglementRepository.save(reglement);

        // Calculer le total des règlements pour la facture
        List<Reglement> reglements = reglementRepository.findByFactureId(reglement.getFactureId());
        double totalReglements = reglements.stream()
                                           .mapToDouble(r -> r.getMontant().doubleValue())
                                           .sum();

        // Récupérer le total de la facture
        double totalFacture = factureServiceClient.getFactureTotal(reglement.getFactureId());

        // Calculer et mettre à jour le statut
        String statut = calculateFactureStatus(reglement.getFactureId(), totalFacture, totalReglements);
        savedReglement.setStatut(statut);

        // Calculer le reste à payer
        savedReglement.setResteAPayer(Math.max(totalFacture - totalReglements, 0));

        return reglementRepository.save(savedReglement);
    }

    public Facture getFactureDetails(Long factureId) {
        return factureServiceClient.getFactureById(factureId);
    }

    private String calculateFactureStatus(Long factureId, double totalFacture, double totalReglements) {
        String statut;
        double reste = totalFacture - totalReglements;

        if (reste <= 0) {
            statut = "Payée";
        } else if (totalReglements > 0) {
            statut = "Partiellement Payée";
        } else {
            statut = "Non Payée";
        }

        // Mettre à jour le statut dans facture-service
        factureServiceClient.updateFactureStatus(factureId, statut);

        return statut;
    }
    public List<Map<String, Object>> getDettesParClient() {
        // Récupérer toutes les factures
        List<Facture> factures = factureServiceClient.getAllFactures();

        // Grouper les factures par clientID
        Map<Long, List<Facture>> facturesParClient = factures.stream()
                .collect(Collectors.groupingBy(Facture::getClientID));

        List<Map<String, Object>> result = new ArrayList<>();

        for (Map.Entry<Long, List<Facture>> entry : facturesParClient.entrySet()) {
            Long clientId = entry.getKey();
            List<Facture> facturesClient = entry.getValue();

            // Calculer la somme des totaux des factures du client
            double totalFactures = facturesClient.stream()
                    .mapToDouble(Facture::getTotal)
                    .sum();

            // Calculer la somme des règlements pour ces factures
            double totalReglements = facturesClient.stream()
                    .mapToDouble(facture -> {
                        List<Reglement> regs = reglementRepository.findByFactureId(facture.getId());
                        return regs.stream().mapToDouble(r -> r.getMontant().doubleValue()).sum();
                    })
                    .sum();

            double dette = Math.max(totalFactures - totalReglements, 0);

            Map<String, Object> clientDettes = new HashMap<>();
            clientDettes.put("clientID", clientId);
            clientDettes.put("dette", dette);

            result.add(clientDettes);
        }

        return result;
    }
}