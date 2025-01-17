package org.ms.factureservice.entities;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

import org.ms.factureservice.model.Produit;
import jakarta.persistence.*;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class FactureLigne {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long produitID;
    private long quantity;
    private BigDecimal price; // Prix sera automatiquement défini

    @Transient
    private Produit produit;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    @JoinColumn(name = "facture_id")
    private Facture facture;

    // Méthode pour définir le prix à partir du produit
    public void updatePrice(Produit produit) {
        if (produit != null) {
            this.price = produit.getPrice();
        }
    }
}