package br.com.casamovel.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import br.com.casamovel.models.Usuario;
import br.com.casamovel.repositories.UsuarioRepository;

@Repository
public class ImplementsUserDetailsService implements UserDetailsService {
	@Autowired
	private UsuarioRepository usuarioRepository;
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Usuario user = usuarioRepository.findByEmail(email);
		if (user == null) {
			throw new UsernameNotFoundException("Usuário não encontrado");
		}
		return user;
	}

}
