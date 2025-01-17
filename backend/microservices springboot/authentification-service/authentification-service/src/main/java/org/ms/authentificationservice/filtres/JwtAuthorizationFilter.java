package org.ms.authentificationservice.filtres;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.ms.authentificationservice.entities.AppUser;
import org.ms.authentificationservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

@Component // Ajoutez cette annotation pour que Spring gère ce filtre
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    public final String PREFIXE_JWT = "Bearer ";
    public final String CLE_SIGNATURE = "MaClé";

    @Autowired // Injectez UserService
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Exclure le path "/refreshToken" du filtre
        if (request.getServletPath().equals("/refreshToken")) {
            filterChain.doFilter(request, response);
        } else {
            // Appliquer le filtre pour les autres requêtes
            String authorizationToken = request.getHeader("Authorization");

            if (authorizationToken != null && authorizationToken.startsWith(PREFIXE_JWT)) {
            	try {
            	    String jwt = authorizationToken.substring(PREFIXE_JWT.length());
            	    Algorithm algo = Algorithm.HMAC256(CLE_SIGNATURE);
            	    JWTVerifier jwtVerifier = JWT.require(algo).build();
            	    DecodedJWT decodedJWT = jwtVerifier.verify(jwt);

            	    String username = decodedJWT.getSubject();
            	    String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
            	    Collection<GrantedAuthority> permissions = new ArrayList<>();
            	    for (String role : roles) {
            	        permissions.add(new SimpleGrantedAuthority(role));
            	    }

            	    SecurityContextHolder.getContext().setAuthentication(
            	        new UsernamePasswordAuthenticationToken(username, null, permissions)
            	    );
            	} catch (Exception e) {
            	    response.setHeader("error-message", "Token invalide ou expiré");
            	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Token invalide ou expiré");
            	}

            } else {
                filterChain.doFilter(request, response);
            }
        }
    }
}