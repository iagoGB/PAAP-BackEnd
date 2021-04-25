package br.com.casamovel.authentication;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import br.com.casamovel.repository.UserRepository;

@Repository
@Transactional
public class ImplementsUserDetailsService implements UserDetailsService {
	@Autowired
	private UserRepository usuarioRepository;
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		return usuarioRepository.findByEmail(email)
				.map( user -> {
					return new User(
						user.getUsername(), user.getPassword(),user.isEnabled(),
						user.isAccountNonExpired(),user.isCredentialsNonExpired(),
						user.isAccountNonLocked(),user.getAuthorities()
					);
				}).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));	
	}
}
