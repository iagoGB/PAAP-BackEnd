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
			throws IOException, ServletException 
	{	
		//Pega o cabecalho da autenticação
		String header = request.getHeader(HEADER_STRING);
		System.out.println("Escrevendo o header que chegou da requisição "+ header);
		//Checa se o header é nulo ou se nao inicia com 'Bearer '
		if (header == null || !header.startsWith(TOKEN_PREFIX)){//(!header.startsWith(HEADER_STRING))){ //O problema esta aqui
			//Ignora requisição
			chain.doFilter(request, response);
			System.out.println("Se essa mensagem está aqui é pq o header esta nulo ou nao começa com Bearer ");
			//Caso verdadeiro, ele sai da execussão do método
			return;
			
		}
		//Se ele chegou até aqui significa que o token atende os requisitos
		
		UsernamePasswordAuthenticationToken authenticationToken = getAuthenticationToken(request);
		SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		//Com isso, é possivel usar nos controllers
		System.out.println("Se chegou aqui é pq esta pegando a chave de autenticação"+ authenticationToken.toString());
		chain.doFilter(request, response);
	}
	
	private UsernamePasswordAuthenticationToken getAuthenticationToken(HttpServletRequest request) {
		System.out.println("Pegando a autenticação");
		String token = request.getHeader(HEADER_STRING);
		System.out.println("Escrevendo o token do método getAuthentication()"+token);
		if (token == null) return null;
		//Pegar o valor do token e verificar dados
		String username = Jwts
				.parser()
				.setSigningKey(SECRET)
				//Retira prefixo adicionado
				.parseClaimsJws(token.replace(TOKEN_PREFIX,""))
				.getBody()
				.getSubject();
		UserDetails userDetails = implementsUserDetailsService.loadUserByUsername(username);
		System.out.println("Debugando user details"+userDetails.toString());
		return username != null ? new UsernamePasswordAuthenticationToken(username, null, userDetails.getAuthorities()) : null;
	}
	

}
