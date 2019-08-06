package br.com.casamovel.authentication;

import java.io.IOException;
import java.util.Date;
import br.com.casamovel.authentication.SecurityConstants.*;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.aop.ThrowsAdvice;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.GenericFilterBean;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.casamovel.models.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private AuthenticationManagerBuilder authenticationManagerBuilder;
	
	public JWTAuthenticationFilter( AuthenticationManagerBuilder authenticationManager) {
		this.authenticationManagerBuilder = authenticationManager;
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException 
	{
		try {
			Usuario usuario = new ObjectMapper().readValue(request.getInputStream(),Usuario.class);
			//Provavelmente será necessário  passar a lista de autorizações abaixo (getAuthorities()). Rever depois
			return this.getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken( usuario.getUsername(), usuario.getPassword()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		String username = ((User) authResult).getUsername();
		String token = Jwts
				.builder()
				.setSubject(username)
				.setExpiration(new Date (System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, SECRET)
				.compact();
		
		response.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
	}
	
	public AuthenticationManagerBuilder getAuthenticationManagerBuilder() {
		return authenticationManagerBuilder;
	}
}
