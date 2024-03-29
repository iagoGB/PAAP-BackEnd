package br.com.paap.endpoint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
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
import org.springframework.web.multipart.MultipartFile;

import br.com.paap.dto.user.ChangePasswordDTO;
import br.com.paap.dto.user.UserDTO;
import br.com.paap.repository.RoleRepository;
import br.com.paap.repository.UserRepository;
import br.com.paap.service.UserService;
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
	public ResponseEntity<?> findByEmail(@RequestParam(value = "username") String username) {
		return usuarioService.findByEmail(username);
	}

	@GetMapping("/findBy")
	public ResponseEntity<?> findByName(@RequestParam(value = "query") String query) {
		return usuarioService.findByName(query);
	}

	@PostMapping
	public ResponseEntity<?> save(
		@RequestParam(name="image", required = false) MultipartFile image,
		@RequestParam(name = "user", required = false) String user
	) throws Exception {
		return usuarioService.save(image, user);

	}

	@PutMapping("/{id}")
	@Transactional
	// Atualização é feita em memória, e ao término do método jpa dispara commit para atualizar no banco
	public ResponseEntity<?> updateUser(
			@PathVariable Long id,
			@RequestParam(name="image", required = false) MultipartFile image,
			@RequestParam(name = "user", required = false ) String user
	) throws JsonMappingException, JsonProcessingException {
		return usuarioService.update(image, user, id);
	}

	@PutMapping("/update/{id}")
	@Transactional
	// Atualização feita em memória, e ao término do método jpa dispara commit para atualizar no banco
	public ResponseEntity<?> updateComplete(
			@PathVariable Long id,
			@RequestParam(name="image", required = false) MultipartFile image,
			@RequestParam(name = "user", required = false ) String user
	) throws JsonMappingException, JsonProcessingException {
		return usuarioService.updateComplete(image, user, id);
	}


	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletarUsuario(@PathVariable Long id) {
		usuarioRepository.deleteById(id);
		return ResponseEntity.ok().build();

	}

	@PutMapping("/{id}/changePassword")
	public ResponseEntity<?> changePassword(
		@PathVariable Long id,
		@RequestBody ChangePasswordDTO data) {
		return usuarioService.changePassword(id, data);
	}
}
