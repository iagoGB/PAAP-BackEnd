package br.com.casamovel.authentication;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import br.com.casamovel.model.Usuario;
import br.com.casamovel.repository.UsuarioRepository;

@Repository
@Transactional
public class ImplementsUserDetailsService implements UserDetailsService {
	@Autowired
	private UsuarioRepository usuarioRepository;
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<Usuario> optUser = usuarioRepository.findByEmail(email);
		Usuario user = optUser.get();
		if (user == null) {
			throw new UsernameNotFoundException("Usuário não encontrado");
		}
		return new User (
				user.getUsername(), user.getPassword(),user.isEnabled(),
				user.isAccountNonExpired(),user.isCredentialsNonExpired(),
				user.isAccountNonLocked(),user.getAuthorities()
		);
	}

}
