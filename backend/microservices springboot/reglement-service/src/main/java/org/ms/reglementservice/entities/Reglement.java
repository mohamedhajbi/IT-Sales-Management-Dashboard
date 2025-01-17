package org.ms.reglementservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reglement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long factureId;
    private BigDecimal montant;
    private String modePaiement;
    private String statut;
    private LocalDateTime dateReglement;

    // Champ temporaire pour le reste à payer (non persistant)
    @Transient
    private double resteAPayer;
}