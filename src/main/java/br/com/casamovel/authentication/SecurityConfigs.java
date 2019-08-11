package br.com.casamovel.authentication;

import static br.com.casamovel.authentication.SecurityConstants.*;
import br.com.casamovel.authentication.JWTAuthenticationFilter;
import br.com.casamovel.authentication.JWTAuthorizationFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;

@EnableWebSecurity
public class SecurityConfigs extends WebSecurityConfigurerAdapter{
	@Autowired
	private ImplementsUserDetailsService userDetailsService;
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		System.out.println("Executando configurações da classe Auth");
		http.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues())
			.and()
			.csrf().disable().authorizeRequests()
			.antMatchers(HttpMethod.POST, SIGN_UP_URL).permitAll()
			.antMatchers(HttpMethod.GET,"/categoria").hasRole("USER")
			.antMatchers(HttpMethod.POST,"/categoria").hasRole("ADMIN")
			.antMatchers(HttpMethod.GET,"/usuario").hasRole("ADMIN")
			.antMatchers(HttpMethod.POST,"/usuario").hasRole("ADMIN")
			.and()
			// filtra requisições de login
			.addFilter(new JWTAuthenticationFilter(authenticationManager()))
			// filtra outras requisições para verificar a presença do JWT no header
			.addFilter(new JWTAuthorizationFilter(authenticationManager(), userDetailsService));
		
		
//		BASIC AUTH FUNCIONAL
//		http.csrf().disable().authorizeRequests()
//		.antMatchers(HttpMethod.GET,"/home").permitAll()
//		.antMatchers(HttpMethod.POST,"/home").permitAll()
//		
//		.anyRequest().authenticated()
//		.and()
//		.httpBasic(); 
	}
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService)
		.passwordEncoder(new BCryptPasswordEncoder());
	}
}
