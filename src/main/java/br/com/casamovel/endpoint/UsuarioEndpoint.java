package br.com.casamovel.endpoint;

import br.com.casamovel.dto.AtualizarUsuarioDTO;
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

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

@RestController
@RequestMapping("/usuario")
public class UsuarioEndpoint {
	@Autowired
	UsuarioRepository usuarioRepository;
    @Autowired
    RoleRepository roleRepository;       
	
	@GetMapping
	public Page<UsuarioDTO> listaUsuario(
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
	public Optional<Usuario> usuarioPorId(@PathVariable(value="id") Long id) {
		return usuarioRepository.findById(id);
	}
	
	@PostMapping
	public ResponseEntity<UsuarioDTO> salvarUsuario(@RequestBody @Valid NovoUsuarioDTO NovoUsuarioDTO,
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
	
	@PutMapping("/{id}")
	@Transactional
	//Atualização é feita em memória, e ao término do método jpa dispara commit para atualizar no banco
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
}
