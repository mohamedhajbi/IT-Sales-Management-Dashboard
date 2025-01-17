package org.ms.authentificationservice.filtres;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private AuthenticationManager authenticationManager;

// injecter par constructeur un gestionnaire d'authentication
	public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
		super.setFilterProcessesUrl("/login");
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		try {
			// Lire les données JSON du body
			Map<String, String> credentials = new ObjectMapper().readValue(request.getInputStream(), Map.class);

			String username = credentials.get("username");
			String password = credentials.get("password");

			System.out.println("Username from request: " + username);
			System.out.println("Password from request: " + password);

			if (username == null || password == null) {
				throw new RuntimeException("Username or password not provided");
			}

			// Construire un objet "Authentication"
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
			return authenticationManager.authenticate(authToken);

		} catch (IOException e) {
			throw new RuntimeException("Error reading request input stream", e);
		}
	}

//appelée en cas de l'existence de l'utilisateur dans la BD pour créer le JWT
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		System.out.println("successfulAuthentication");
		// retourner l'utilisateur authentifié
		User user = (User) authResult.getPrincipal();
		// récupérer la liste des rôles
		String[] roles = new String[user.getAuthorities().size()];
		int index = 0;
		for (GrantedAuthority gi : user.getAuthorities()) {
			roles[index] = gi.toString();
			index++;
		}
		// Choisir un algorithme de cryptage
		Algorithm algo = Algorithm.HMAC256("MaClé");
		// Construire le JWT
		String jwtAccessToken = JWT.create().withSubject(user.getUsername()) // stocker le nom de l'utilisateur
				.withExpiresAt(new Date(System.currentTimeMillis() + 1 * 60 * 1000))
				// date d'expiration après 1 minute
				.withIssuer(request.getRequestURL().toString())
				// url de la requête d'origine
				// placer la liste des rôles associés à l'utilisateur courant
				.withArrayClaim("roles", roles).sign(algo); // signer le JWT avec l'algorithme choisi
		// Construire le refresh JWT
		String jwtRefreshToken = JWT.create()
				// stocker le nom de l'utilisateur
				.withSubject(user.getUsername())
				// date d'expiration après 1 heure
				.withExpiresAt(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
				// url de la reuête d'origine
				.withIssuer(request.getRequestURL().toString())
				// signer le refresh JWT avec l'algorithme choisi
				.sign(algo);
		// stocker les deux tokens dans un objet HashMap
		Map<String, String> mapTokens = new HashMap<>();
		mapTokens.put("access-token", jwtAccessToken);
		mapTokens.put("refresh-token", jwtRefreshToken);
		// Spécifier le format du contenu de la réponse
		response.setContentType("application/json");
		// place l'objet HashMap dans le corps de la réponse
		new ObjectMapper().writeValue(response.getOutputStream(), mapTokens);
	}

}