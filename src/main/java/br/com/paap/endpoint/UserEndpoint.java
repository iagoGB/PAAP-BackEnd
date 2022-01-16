package br.com.paap.endpoint;

import javax.validation.Valid;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.paap.dto.user.NewUserDTO;
import br.com.paap.dto.user.UpdateUserDTO;
import br.com.paap.dto.user.UserDTO;
import br.com.paap.model.User;
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

	@PostMapping
	public ResponseEntity<?> save(@RequestBody @Valid NewUserDTO novoUsuarioDTO,
			UriComponentsBuilder uriBuilder) throws Exception {
		return usuarioService.save(novoUsuarioDTO, uriBuilder);

	}

	@PutMapping("/{id}")
	@Transactional
	// Atualização é feita em memória, e ao término do método jpa dispara commit para atualizar no banco
	public ResponseEntity<UserDTO> atualizarUsuario(@PathVariable Long id,
			@RequestBody @Valid UpdateUserDTO atualizarUsuarioDTO,
			UriComponentsBuilder uriBuilder) {
		User usuarioAtualizado = atualizarUsuarioDTO.update(id, usuarioRepository);
		return ResponseEntity.ok(new UserDTO(usuarioAtualizado));
	}

	@PutMapping("teste/{id}")
	@Transactional
	// Atualização é feita em memória, e ao término do método jpa dispara commit para atualizar no banco
	public ResponseEntity<?> updateUser(
			@PathVariable Long id,
			@RequestParam(name="image", required = false) MultipartFile image,
			@RequestParam(name = "user", required = false ) String user
	) throws JsonMappingException, JsonProcessingException {
		return usuarioService.update(image, user, id);
	}

	// @PutMapping("teste/{id}")
	// @Transactional
	// // Atualização é feita em memória, e ao término do método jpa dispara commit para atualizar no banco
	// public ResponseEntity<UserDTO> updateUser(
	// 		@PathVariable Long id,
	// 		@RequestParam("image") MultipartFile image,
	// 		@RequestPart(name = "user", required = false) UpdateUserDTO user
	// ) {
	// 	if (user == null) {
	// 		System.out.println("Não vieo usuário");
	// 	} else {
	// 		System.out.println("Atualizar informações");
	// 	}
		
	// 	return ResponseEntity.ok().build();
	// }

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletarUsuario(@PathVariable Long id) {
		usuarioRepository.deleteById(id);
		return ResponseEntity.ok().build();

	}

	public ResponseEntity<?> getCertificate(
			@RequestParam(value = "eventID") Long eventID,
			@RequestParam(value = "userID") Long userID) {
		return null;
	}
}
