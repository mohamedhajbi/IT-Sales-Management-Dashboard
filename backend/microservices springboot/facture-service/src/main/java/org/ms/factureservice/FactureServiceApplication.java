package org.ms.factureservice;

import org.ms.factureservice.entities.Facture;
import org.ms.factureservice.entities.FactureLigne;
import org.ms.factureservice.feign.ClientServiceClient;
import org.ms.factureservice.feign.ProduitServiceClient;
import org.ms.factureservice.model.Client;
import org.ms.factureservice.model.Produit;
import org.ms.factureservice.repository.FactureLigneRepository;
import org.ms.factureservice.repository.FactureRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootApplication
@EnableFeignClients
public class FactureServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(FactureServiceApplication.class, args);
    }
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("*")
                        .allowedHeaders("*")
                        .allowCredentials(false)
                        .maxAge(3600);
            }
        };
/*
    @Bean
    CommandLineRunner start(FactureRepository factureRepository, FactureLigneRepository ligneRepository,
                             ClientServiceClient clientServiceClient, ProduitServiceClient produitServiceClient) {
        return args -> {
            // Récupérer un client depuis client-service
            Client client = clientServiceClient.findClientById(1L);
            if (client == null || client.getId() == -1) {
                throw new RuntimeException("Client ou ID client non valide");
            }

            System.out.println(client);
            System.out.println(client.getId());
            System.out.println(ligneRepository.findByFactureId(1L));

            // Créer une nouvelle facture
            Facture facture = new Facture(1L, new Date(), client.getId());
            System.out.println(client.getId());
            System.out.println(facture);
            Facture savedFacture = factureRepository.save(facture);

            List<Produit> produits;
            try {
                produits = produitServiceClient.getAllProduits();
                System.out.println(produits);
            } catch (Exception e) {
                throw new RuntimeException("Impossible de récupérer les produits depuis produit-service", e);
            }

            if (produits != null) {
                produits.forEach(produit -> {
                    FactureLigne ligne = new FactureLigne();
                    ligne.setProduitID(produit.getId());
                    ligne.setPrice(produit.getPrice());
                    ligne.setQuantity(5); // Exemple de quantité
                    ligne.setFacture(savedFacture);
                    ligneRepository.save(ligne);
                });
            }

            System.out.println("=== Test Chiffre d'Affaires ===");
            List<Facture> factures = factureRepository.findAll();
            double chiffreAffairesGlobal = factures.stream()
                .mapToDouble(f -> {
                    List<FactureLigne> lignes = new ArrayList<>(ligneRepository.findByFactureId(f.getId()));
                    return lignes.stream().mapToDouble(l -> l.getPrice().doubleValue() * l.getQuantity()).sum();
                })
                .sum();
            System.out.println("Chiffre d'affaires global: " + chiffreAffairesGlobal);

            int testYear = 2024; // Exemple d'année à tester
            double chiffreAffairesAnnee = factures.stream()
                .filter(f -> f.getDateFacture().toInstant().atZone(java.time.ZoneId.systemDefault()).getYear() == testYear)
                .mapToDouble(f -> {
                    List<FactureLigne> lignes = new ArrayList<>(ligneRepository.findByFactureId(f.getId()));
                    return lignes.stream().mapToDouble(l -> l.getPrice().doubleValue() * l.getQuantity()).sum();
                })
                .sum();
            System.out.println("Chiffre d'affaires pour l'année " + testYear + ": " + chiffreAffairesAnnee);
        };
    }*/
    }}
