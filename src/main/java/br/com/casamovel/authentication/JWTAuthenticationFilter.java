package br.com.casamovel.authentication;

import java.io.IOException;
import java.util.Date;

import static br.com.casamovel.authentication.SecurityConstants.*;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.casamovel.models.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private AuthenticationManager authenticationManager;
	//Verificar se recebe builder ou comum;
	
	public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager= authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException 
	{
		try {
			Usuario usuario = new ObjectMapper().readValue(request.getInputStream(),Usuario.class);
			System.out.println("Transformou para usuário "+ usuario.toString());
			//Provavelmente será necessário  passar a lista de autorizações abaixo (getAuthorities()). Rever depois
			return this.getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken( usuario.getUsername(), usuario.getPassword()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		System.out.println("Entrou no método autenticação bem sucedida");
		String username = ((User) authResult.getPrincipal()).getUsername();
		System.out.println("Classe clastada"+username);
		String token = Jwts
				.builder()
				.setSubject(username)
				.setExpiration(new Date (System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, SECRET)
				.compact();
		System.out.println("token: "+token);
		
		response.addHeader(HEADER_STRING,TOKEN_PREFIX + token);
	}
	
	public AuthenticationManager getAuthenticationManager() {
		return authenticationManager;
	}
}
