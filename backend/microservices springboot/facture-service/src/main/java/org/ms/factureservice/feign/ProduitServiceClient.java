package org.ms.factureservice.feign;
import java.util.List;

import org.ms.factureservice.model.Produit;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "Produit-service", url = "http://localhost:5050/api")
public interface ProduitServiceClient {
    @GetMapping(path = "/Products")
    List<Produit> getAllProduits();

    @GetMapping(path = "/Products/{id}")
    Produit findProductById(@PathVariable(name = "id") long id);
    @GetMapping("/Products/out-of-stock")
    List<Produit> getOutOfStockProducts();
    @PutMapping("/Products/{id}/update-stock")
    Produit updateStock(@PathVariable("id") int productId, @RequestBody int quantityChange);
}
