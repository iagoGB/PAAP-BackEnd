package br.com.casamovel.endpoints;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.casamovel.models.Usuario;
import br.com.casamovel.repositories.UsuarioRepository;

@RestController
public class UsuarioEndpoint {
	@Autowired
	 UsuarioRepository usuarioRepository;
	
	@GetMapping("/usuario")
	List<Usuario> listaUsuario() {
		return usuarioRepository.findAll();
	}
	
	@GetMapping("/usuario/{id}")
	Optional<Usuario> usuarioPorId(@PathVariable(value="id") Long id) {
		return usuarioRepository.findById(id);
	}
	
	@PostMapping("/usuario")
	Usuario salvaUsuario(@RequestBody Usuario usuario) {
		return usuarioRepository.save(usuario);
	}
}
