package org.ms.authentificationservice.web;

import org.ms.authentificationservice.dto.UserRoleData;
import org.ms.authentificationservice.entities.AppRole;
import org.ms.authentificationservice.entities.AppUser;
import org.ms.authentificationservice.services.UserService;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserServiceREST {

    private final UserService userService;
    public final String PREFIXE_JWT = "Bearer ";
    public final String CLE_SIGNATURE = "MaClé";

    public UserServiceREST(UserService userService) {
        this.userService = userService;
    }

    @CrossOrigin(origins = "http://localhost:8080") // Ajoutez cette annotation pour permettre les requêtes CORS
    @PostMapping("/register") // Assurez-vous que la route est correcte ici
    public AppUser addUser(@RequestBody AppUser user) {
        return userService.addUser(user);
    }

    @PostMapping("/roles")
    @PostAuthorize("hasAuthority('ADMIN')")
    public AppRole addRole(@RequestBody AppRole role) {
        return userService.addRole(role);
    }

    @PostMapping("/addRoleToUser")
    @PostAuthorize("hasAuthority('ADMIN')")
    public void addRoleToUser(@RequestBody UserRoleData userRoleData) {
        userService.addRoleToUser(userRoleData.getUsername(), userRoleData.getRoleName());
    }

    @GetMapping
    public List<AppUser> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping(path = "/refreshToken")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String refreshToken = request.getHeader("Authorization");
        if (refreshToken != null && refreshToken.startsWith(PREFIXE_JWT)) {
            try {
                String jwtRefresh = refreshToken.substring(PREFIXE_JWT.length());
                Algorithm algo = Algorithm.HMAC256(CLE_SIGNATURE);
                JWTVerifier jwtVerifier = JWT.require(algo).build();
                DecodedJWT decodedJWT = jwtVerifier.verify(jwtRefresh);
                String username = decodedJWT.getSubject();
                AppUser user = userService.getUserByName(username);
                String[] roles = new String[user.getAppRoles().size()];
                int index = 0;
                for (AppRole r : user.getAppRoles()) {
                    roles[index] = r.getRoleName();
                    index++;
                }

                String jwtAccessToken = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 1 * 60 * 1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withArrayClaim("roles", roles)
                        .sign(algo);

                Map<String, String> mapTokens = new HashMap<>();
                mapTokens.put("access-token", jwtAccessToken);
                mapTokens.put("refresh-token", jwtRefresh);

                response.setHeader("Access-Control-Allow-Origin", "*");
                response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
                response.setContentType("application/json");
                new ObjectMapper().writeValue(response.getOutputStream(), mapTokens);
            } catch (Exception e) {
                response.setHeader("error-message", e.getMessage());
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Token invalide ou expiré");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Refresh Token non disponible.");
        }
    }
}
