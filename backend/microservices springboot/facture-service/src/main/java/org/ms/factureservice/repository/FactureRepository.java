package org.ms.factureservice.repository;

import java.util.List;

import org.ms.factureservice.entities.Facture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;

@RepositoryRestController
public interface FactureRepository extends JpaRepository<Facture, Long> {
	 
}