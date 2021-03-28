package br.com.casamovel.endpoint;

import br.com.casamovel.dto.usuario.AtualizarUsuarioDTO;
import br.com.casamovel.dto.usuario.NovoUsuarioDTO;
import br.com.casamovel.dto.usuario.UsuarioDTO;

import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.casamovel.model.Usuario;
import br.com.casamovel.repository.RoleRepository;
import br.com.casamovel.repository.UsuarioRepository;
import br.com.casamovel.service.UsuarioService;
import javassist.NotFoundException;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

@RestController
@RequestMapping("/user")
public class UsuarioEndpoint {
	@Autowired
	UsuarioRepository usuarioRepository;
	@Autowired
	RoleRepository roleRepository;
	@Autowired
	UsuarioService usuarioService;

	@GetMapping
	public ResponseEntity<Page<UsuarioDTO>> listaUsuario(Pageable pagination) {
		return usuarioService.findAll(pagination);
	}

	@GetMapping("/{id}")
	public ResponseEntity<UsuarioDTO> usuarioPorId(@PathVariable(value = "id") Long id) throws NotFoundException {
		return usuarioService.findById(id);
	}
	
	@GetMapping("/email")
	public ResponseEntity<?> usuarioPorId(@RequestParam(value="username") String username) {
		 return usuarioService.findByEmail(username);
	}
	
	@PostMapping
	public ResponseEntity<?> salvarUsuario(@RequestBody @Valid NovoUsuarioDTO NovoUsuarioDTO,
			UriComponentsBuilder uriBuilder) throws Exception {
		return usuarioService.save(NovoUsuarioDTO, uriBuilder);

	} 
	
	@PutMapping("/{id}")
	@Transactional
	// Atualização é feita em memória, e ao término do método jpa dispara commit para atualizar no banco
	public ResponseEntity<UsuarioDTO> atualizarUsuario(@PathVariable Long id, @RequestBody @Valid AtualizarUsuarioDTO atualizarUsuarioDTO,
			UriComponentsBuilder uriBuilder){
		Usuario usuarioAtualizado = atualizarUsuarioDTO.atualizar(id, usuarioRepository);
		return ResponseEntity.ok(new UsuarioDTO(usuarioAtualizado));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletarUsuario(@PathVariable Long id) {
		usuarioRepository.deleteById(id);
		return ResponseEntity.ok().build();
		
	}
	
	public ResponseEntity<?> getCertificate(
			@RequestParam(value="eventID") Long eventID,
			@RequestParam(value="userID") Long userID
	) {
		return null;	
	}
}
