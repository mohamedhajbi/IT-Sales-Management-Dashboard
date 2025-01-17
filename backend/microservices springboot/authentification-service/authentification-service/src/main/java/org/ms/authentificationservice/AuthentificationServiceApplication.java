package org.ms.authentificationservice;

import java.util.ArrayList;

import org.ms.authentificationservice.entities.AppRole;
import org.ms.authentificationservice.entities.AppUser;
import org.ms.authentificationservice.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableMethodSecurity (prePostEnabled = true, securedEnabled = true)
public class AuthentificationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthentificationServiceApplication.class, args);
    }
    

    @Bean
    CommandLineRunner start(UserService userService) {
        return args -> {
            // Ajouter les rôles
           /* userService.addRole(new AppRole(null, "USER"));
            userService.addRole(new AppRole(null, "ADMIN"));

            // Ajouter les utilisateurs
            userService.addUser(new AppUser(null, "user1", "123", new ArrayList<>()));
            userService.addUser(new AppUser(null, "user2", "456", new ArrayList<>()));

            // Associer les rôles aux utilisateurs
            userService.addRoleToUser("user1", "USER");
            userService.addRoleToUser("user2", "USER");
            userService.addRoleToUser("user2", "ADMIN");
*/
            System.out.println("Initialisation terminée");
        }; 
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
    }
}