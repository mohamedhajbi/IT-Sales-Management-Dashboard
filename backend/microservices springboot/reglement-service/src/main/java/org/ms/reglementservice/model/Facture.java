package org.ms.reglementservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Facture {
    private Long id;
    private java.util.Date dateFacture;
    private Long clientID;
    private List<FactureLigne> facturelignes; // Détails des lignes de la facture

    public double getTotal() {
        return facturelignes.stream()
                .mapToDouble(ligne -> ligne.getPrice().doubleValue() * ligne.getQuantity())
                .sum();
    }
}