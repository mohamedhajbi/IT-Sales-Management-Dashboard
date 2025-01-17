package org.ms.factureservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    private int id;               // Correspond à "Id" dans JSON
    private String name;          // Correspond à "Name" dans JSON
}
