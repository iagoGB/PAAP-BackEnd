package br.com.paap.authentication;

import java.util.Collections;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class SecurityConstants {
		//Seguir convenções: Authorization Bearer $2kansnjjw2oi5j&jwa
		// EXPIRATION_TIME = 10 dias
		//static final long EXPIRATION_TIME = 860_000_000;
		 static final String SECRET = "MySecret";
		 static final String TOKEN_PREFIX = "Bearer ";
		 static final String HEADER_STRING = "Authorization";
		//Url permitida para autenticação
		 static final String SIGN_UP_URL = "/login";
		 static final long EXPIRATION_TIME = 86400000L;
		
	/*
		Metodo para converter tempo em MILLIS a partir da unidade desejada
		public static void main(String[] args) {
			System.out.println(TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)); - 1dia
		}
	*/
		static void addAuthentication(HttpServletResponse response, String email) {
			String JWT = Jwts.builder()
					.setSubject(email)
					.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
					.signWith(SignatureAlgorithm.HS512, SECRET)
					.compact();
			
			response.addHeader(HEADER_STRING, TOKEN_PREFIX + JWT);
		}
		
		static Authentication getAuthentication(HttpServletRequest request) {
			String token = request.getHeader(HEADER_STRING);
			
			if (token != null) {
				// faz parse do token
				String user = Jwts.parser()
						.setSigningKey(SECRET)
						.parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
						.getBody()
						.getSubject();
				
				if (user != null) {
					return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
				}
			}
			return null;
		}

}

