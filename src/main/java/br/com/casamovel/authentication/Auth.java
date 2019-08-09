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

@EnableWebSecurity
public class Auth extends WebSecurityConfigurerAdapter{
	@Autowired
	private ImplementsUserDetailsService userDetailsService;
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		System.out.println("Executando configurações da classe Auth");
		http.cors().and().csrf().disable().authorizeRequests()
			.antMatchers(HttpMethod.POST, "/login").permitAll() //SIGN_UP_URL
			.antMatchers(HttpMethod.GET,"/categoria").hasRole("ADMIN")
			.antMatchers(HttpMethod.POST,"/categoria").hasRole("ADMIN")
			.and()
			.addFilter(new JWTAuthenticationFilter(authenticationManager()))
			.addFilter(new JWTAuthorizationFilter(authenticationManager(), userDetailsService));
		
		
//		FUNCIONAL
//		http.csrf().disable().authorizeRequests()
//		.antMatchers(HttpMethod.GET,"/home").permitAll()
//		.antMatchers(HttpMethod.POST,"/home").permitAll()
//		
//		.anyRequest().authenticated()
//		.and()
//		.httpBasic(); 
		
		/*
		// filtra requisições de login
		.addFilterBefore(new JWTLoginFilter("/login", authenticationManager()),
				UsernamePasswordAuthenticationFilter.class)

		// filtra outras requisições para verificar a presença do JWT no header
		.addFilterBefore(new JWTAuthenticationFilter(),
				UsernamePasswordAuthenticationFilter.class);
*/
	}
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService)
		.passwordEncoder(new BCryptPasswordEncoder());
		System.out.println("Encriptou password");
	}
	/*   
	 @Bean
	    CorsConfigurationSource corsConfigurationSource() {
	        CorsConfiguration configuration = new CorsConfiguration();
	        configuration.setAllowedOrigins(Arrays.asList("*"));
	        configuration.setAllowedMethods(Arrays.asList("*"));
	        configuration.setAllowedHeaders(Arrays.asList("*"));
	        configuration.setAllowCredentials(true);
	        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	        source.registerCorsConfiguration("/**", configuration);
	        return source;
	    } */
}
