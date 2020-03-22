package br.com.casamovel.authentication;

import java.io.IOException;

import java.util.Collection;
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.casamovel.model.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	//Gerenciador de autenticação
	private AuthenticationManager authenticationManager;
	
	public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager= authenticationManager;
	}
	//Tentativa de autenticação
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException 
	{	//Tentar passar os valores que vem do Json para um objeto usuário
		try {
			Usuario usuario = new ObjectMapper().readValue(request.getInputStream(),Usuario.class);
			System.out.println("Transformou para usuário "+ usuario.toString());
			//Provavelmente será necessário  passar a lista de autorizações abaixo (getAuthorities()). Rever depois
			//Solicita a autenticação para o gerenciador
			return this.getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken( usuario.getUsername(), usuario.getPassword()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	//Quando a autenticação é bem sucessida, este método é invocado para gerar os dados do token
	@Override
	protected void successfulAuthentication(
			HttpServletRequest request, HttpServletResponse response, 
			FilterChain chain, Authentication authResult
	) throws IOException, ServletException {
		System.out.println("Entrou no método autenticação bem sucedida");
		String username = ((User) authResult.getPrincipal()).getUsername(); // authResult.getPrincipal()).getUsername();
		Collection<GrantedAuthority> u = ((User) authResult.getPrincipal()).getAuthorities();

		//Pesquisar uma método mais elegante para trazer o dado depois
			//Verifica qual role o usuário tem através do toString, então da um replace nos dados do objeto, deixando apenas o valor da role.
		String r =  u.toString().replace("[Role [roleName=ROLE_", "").replace("]]", "");
		System.out.println("Usuário tipo:" + r);
		String token = Jwts
				.builder()
				.setSubject(username)
				.setExpiration(new Date (System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, SECRET)
				.compact();
		String bearerToken = (TOKEN_PREFIX + token);
		//Escrever no body o Token e a role
		response.getWriter().write("{\"token\":\""+bearerToken+"\", \"role\":\""+ r + "\", \"username\":\""+ username + "\"}");
		//Escrever no header o token
		response.addHeader(HEADER_STRING,bearerToken);
	}
	
	public AuthenticationManager getAuthenticationManager() {
		return authenticationManager;
	}
}
