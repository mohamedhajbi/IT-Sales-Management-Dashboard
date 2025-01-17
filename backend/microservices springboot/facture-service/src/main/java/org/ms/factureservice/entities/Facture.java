package org.ms.factureservice.entities;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.ms.factureservice.model.Client;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import jakarta.persistence.PrePersist;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Facture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Date dateFacture;

    @OneToMany(mappedBy = "facture", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<FactureLigne> facturelignes = new ArrayList<>();

    @Transient
    private Client client;

    private long clientID;

    private String statut = "Non Payée"; // Statut par défaut

    @PrePersist
    private void setDefaultDate() {
        if (dateFacture == null) {
            dateFacture = new Date(); // Utiliser la date du système
        }
    }
}
