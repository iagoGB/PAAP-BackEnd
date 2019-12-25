package br.com.casamovel.endpoint;

import br.com.casamovel.dto.NovoUsuarioDTO;
import br.com.casamovel.dto.UsuarioDTO;

import java.net.URI;
import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.casamovel.model.Usuario;
import br.com.casamovel.repository.RoleRepository;
import br.com.casamovel.repository.UsuarioRepository;
import br.com.casamovel.util.Disco;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/usuario")
public class UsuarioEndpoint {
	@Autowired
	UsuarioRepository usuarioRepository;
    @Autowired
    RoleRepository roleRepository;       
    Disco disco = new Disco();
	
	@GetMapping
	Page<UsuarioDTO> listaUsuario(
		@RequestParam(required = false) LocalDate dataIngresso,
		@RequestParam int pagina, 
		@RequestParam int quantidade,
		@RequestParam(defaultValue = "id") String ordenacao
	) 
	{
		Pageable pagination = PageRequest.of(pagina, quantidade,Direction.DESC,ordenacao);
		Page<Usuario> usuarioEntidade = usuarioRepository.findAll(pagination);
		return UsuarioDTO.parse(usuarioEntidade);
	}
	
	@GetMapping("/{id}")
	Optional<Usuario> usuarioPorId(@PathVariable(value="id") Long id) {
		return usuarioRepository.findById(id);
	}
	
	@PostMapping
	ResponseEntity<UsuarioDTO> salvarUsuario(@RequestBody @Valid NovoUsuarioDTO NovoUsuarioDTO,
			UriComponentsBuilder uriBuilder) throws Exception {
		Usuario novoUsuario = new Usuario();
		novoUsuario.parse(NovoUsuarioDTO, roleRepository);
		// Salvar
		usuarioRepository.save(novoUsuario);
		UsuarioDTO usuarioDTO = UsuarioDTO.parse(novoUsuario);
		// Caminho do novo recurso criado
		URI uri = uriBuilder.path("/usuario/{id}").buildAndExpand(novoUsuario.getId()).toUri();
		return ResponseEntity.created(uri).body(usuarioDTO);

	}   
}
