package br.com.paap.authentication;

import static br.com.paap.authentication.SecurityConstants.SIGN_UP_URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;

import br.com.paap.repository.UserRepository;

@EnableWebSecurity
public class SecurityConfigs extends WebSecurityConfigurerAdapter{
	@Autowired
	private ImplementsUserDetailsService userDetailsService;
	@Autowired
	private UserRepository userRepository;
	private CorsConfiguration applyPermitDefaultValues;
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		System.out.println("Executando configurações da classe Auth");
		http.cors().configurationSource(request -> {
			applyPermitDefaultValues = new CorsConfiguration().applyPermitDefaultValues();
			applyPermitDefaultValues.addAllowedMethod("*");
			return applyPermitDefaultValues;
		})
			.and()
			.csrf().disable().authorizeRequests()
			.antMatchers(HttpMethod.POST, SIGN_UP_URL).permitAll()
			// .antMatchers(HttpMethod.GET,"/category").hasAnyRole("ADMIN","USER")
			.antMatchers(HttpMethod.GET,"/user").hasAnyRole("ADMIN","USER")
			.antMatchers(HttpMethod.POST, "/user").hasRole("ADMIN")
			.antMatchers(HttpMethod.PUT,"/event/**").hasAnyRole("ADMIN","USER")
			.antMatchers(HttpMethod.PUT,"/event/*/subscribe").hasRole("USER")
			.and()
			// filtra requisições de login
			.addFilter(new JWTAuthenticationFilter(authenticationManager(), userRepository))
			// filtra outras requisições para verificar a presença do JWT no header
			.addFilter(new JWTAuthorizationFilter(authenticationManager(), userDetailsService));
		
	}
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService)
		.passwordEncoder(new BCryptPasswordEncoder());
	}
}
