package br.com.casamovel.endpoint;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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

import br.com.casamovel.dto.usuario.AtualizarUsuarioDTO;
import br.com.casamovel.dto.usuario.NewUserDTO;
import br.com.casamovel.dto.usuario.UserDTO;
import br.com.casamovel.model.User;
import br.com.casamovel.repository.RoleRepository;
import br.com.casamovel.repository.UserRepository;
import br.com.casamovel.service.UserService;
import javassist.NotFoundException;

@RestController
@RequestMapping("/user")
public class UserEndpoint {
	@Autowired
	UserRepository usuarioRepository;
	@Autowired
	RoleRepository roleRepository;
	@Autowired
	UserService usuarioService;

	@GetMapping
	public ResponseEntity<Page<UserDTO>> listaUsuario(Pageable pagination) {
		return usuarioService.findAll(pagination);
	}

	@GetMapping("/{id}")
	public ResponseEntity<UserDTO> usuarioPorId(@PathVariable(value = "id") Long id) throws NotFoundException {
		return usuarioService.findById(id);
	}
	
	@GetMapping("/email")
	public ResponseEntity<?> findByEmail(@RequestParam(value="username") String username) {
		 return usuarioService.findByEmail(username);
	}
	
	@PostMapping
	public ResponseEntity<?> save(@RequestBody @Valid NewUserDTO NovoUsuarioDTO,
			UriComponentsBuilder uriBuilder) throws Exception {
		return usuarioService.save(NovoUsuarioDTO, uriBuilder);

	} 
	
	@PutMapping("/{id}")
	@Transactional
	// Atualização é feita em memória, e ao término do método jpa dispara commit para atualizar no banco
	public ResponseEntity<UserDTO> atualizarUsuario(@PathVariable Long id, @RequestBody @Valid AtualizarUsuarioDTO atualizarUsuarioDTO,
			UriComponentsBuilder uriBuilder){
		User usuarioAtualizado = atualizarUsuarioDTO.update(id, usuarioRepository);
		return ResponseEntity.ok(new UserDTO(usuarioAtualizado));
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
