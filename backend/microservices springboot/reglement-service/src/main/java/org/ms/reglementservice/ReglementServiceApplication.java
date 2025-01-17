package org.ms.reglementservice;

import org.ms.reglementservice.controller.ReglementController;
import org.ms.reglementservice.entities.Reglement;
import org.ms.reglementservice.feign.FactureServiceClient;
import org.ms.reglementservice.model.Facture;
import org.ms.reglementservice.repository.ReglementRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@SpringBootApplication
@EnableFeignClients
public class ReglementServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReglementServiceApplication.class, args);
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
    /* @Bean
    CommandLineRunner start(ReglementRepository reglementRepository, FactureServiceClient factureServiceClient, ReglementController reglementController) {
        return args -> {
            // Utiliser la méthode du contrôleur pour obtenir le reste global à payer
            double resteGlobal = reglementController.obtenirResteGlobal();
            System.out.println("Reste global à payer : " + resteGlobal);

            // Exemple de création d'un règlement
            Long factureId = 1L; // ID de la facture à régler
            Facture facture = factureServiceClient.getFactureById(factureId);
            double totalFacture = factureServiceClient.getFactureTotal(factureId);

            // Exemple de montant réglé
            double montantRegle = 100; // Montant payé

            Reglement reglement = new Reglement();
            reglement.setFactureId(facture.getId());
            reglement.setMontant(BigDecimal.valueOf(montantRegle));
            reglement.setDateReglement(new Date());
            reglement.setModePaiement("Carte Bancaire");

            reglementRepository.save(reglement);

            // Calculer le reste à payer directement en utilisant la méthode du contrôleur
            double resteARegler = reglementController.obtenirResteGlobal(); // Utilisation de la méthode déjà définie

            // Mise à jour du statut de la facture en fonction du total des paiements
            String statut = null;
            if (resteARegler <= 0) {
                statut = "Payée";
                factureServiceClient.updateFactureStatus(factureId, "Payée");
            } else if (resteARegler > 0) {
                statut = "Partiellement Payée";
                factureServiceClient.updateFactureStatus(factureId, "Partiellement Payée");
            }

            // Afficher les résultats
            System.out.println("===== TEST REGLEMENT =====");
            System.out.println("Facture ID : " + facture.getId());
            System.out.println("Montant total de la facture : " + totalFacture);
            System.out.println("Montant réglé : " + montantRegle);
            System.out.println("Reste à régler pour cette facture : " + resteARegler);
            System.out.println("Statut de la facture : " + statut);
            System.out.println("==========================");
        };
    }*/
}}
