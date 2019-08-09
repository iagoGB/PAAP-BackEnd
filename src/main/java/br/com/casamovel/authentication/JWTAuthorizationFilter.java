package br.com.casamovel.authentication;

import static br.com.casamovel.authentication.SecurityConstants.*;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import io.jsonwebtoken.Jwts;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter{
	private final ImplementsUserDetailsService implementsUserDetailsService;
	
	public JWTAuthorizationFilter(AuthenticationManager authenticationManager, ImplementsUserDetailsService iuds) {
		super(authenticationManager);
		this.implementsUserDetailsService = iuds;
		
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String header = request.getHeader(HEADER_STRING);
		System.out.println("Escrevendo o header que chegou da requisição "+ header);
		if (header == null ) {//(!header.startsWith(HEADER_STRING))){ //O problema esta aqui
			chain.doFilter(request, response);
			System.out.println("Se essa mensagem está aqui é pq o header esta nulo ou nao começa com Bearer ");
			return;
			
		}
		UsernamePasswordAuthenticationToken authenticationToken = getAuthenticationToken(request);
		SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		System.out.println("Se chegou aqui é pq esta pegando a chave de autenticação"+ authenticationToken.toString());
		chain.doFilter(request, response);
	}
	
	private UsernamePasswordAuthenticationToken getAuthenticationToken(HttpServletRequest request) {
		System.out.println("Pegando a autenticação");
		String token = request.getHeader(HEADER_STRING);
		System.out.println("Escrevendo o token do método getAuthentication()"+token);
		if (token == null) return null;
		String username = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token.replace(TOKEN_PREFIX,"")).getBody().getSubject();
		UserDetails userDetails = implementsUserDetailsService.loadUserByUsername(username);
		System.out.println("Debugando user details"+userDetails.toString());
		return username != null ? new UsernamePasswordAuthenticationToken(username, null, userDetails.getAuthorities()) : null;
	}
	

}
