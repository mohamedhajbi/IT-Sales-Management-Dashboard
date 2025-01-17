package org.ms.factureservice.model;



import java.math.BigDecimal;

import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Produit {
    private long id;
    private String name;
    private String photoPath;
    private BigDecimal  price;
    private int stockQuantity;
    private int categoryId;
    private Category category; // L'objet catégorie, si nécessaire
}
