package org.ms.authentificationservice.securite;

import org.ms.authentificationservice.entities.AppUser;
import org.ms.authentificationservice.filtres.JwtAuthenticationFilter;
import org.ms.authentificationservice.filtres.JwtAuthorizationFilter;
import org.ms.authentificationservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.ArrayList;
import java.util.Collection;
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig {
    private final UserService userService;

    @Autowired // Injectez JwtAuthorizationFilter
    private JwtAuthorizationFilter jwtAuthorizationFilter;

    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void globalConfig(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(new UserDetailsService() {
        	@Override
        	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        	    System.out.println("Loading user by username: " + username);
        	    AppUser appUser = userService.getUserByName(username);
        	    if (appUser == null) {
        	        throw new UsernameNotFoundException("User not found with username: " + username);
        	    }
        	    Collection<GrantedAuthority> permissions = new ArrayList<>();
        	    appUser.getAppRoles().forEach(r -> {
        	        permissions.add(new SimpleGrantedAuthority(r.getRoleName()));
        	    });
        	    return new User(appUser.getUsername(), appUser.getPassword(), permissions);
        	}
        });
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.headers().frameOptions().disable();

        // 🔥 Autorise l'accès à /login et /refreshToken sans authentification
        http.authorizeRequests().requestMatchers("/h2-console/**", "/login", "/refreshToken").permitAll();

        // ✅ Bloque les autres requêtes avec des rôles spécifiques
        http.authorizeRequests().requestMatchers(HttpMethod.POST, "/users/").hasAuthority("ADMIN");
        http.authorizeRequests().requestMatchers(HttpMethod.GET, "/users/").hasAuthority("USER");

        // 🔥 Toute autre requête doit être authentifiée
        http.authorizeRequests().anyRequest().authenticated();

        // Ajout des filtres JWT
        http.addFilter(new JwtAuthenticationFilter(authenticationManager(http.getSharedObject(AuthenticationConfiguration.class))));
        http.addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


}