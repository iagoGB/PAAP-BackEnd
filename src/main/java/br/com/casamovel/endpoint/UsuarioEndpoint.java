package br.com.casamovel.endpoint;

import br.com.casamovel.dto.NovoUsuarioDTO;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.casamovel.model.Usuario;
import br.com.casamovel.repository.RoleRepository;
import br.com.casamovel.repository.UsuarioRepository;
import br.com.casamovel.util.Disco;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
public class UsuarioEndpoint {
	@Autowired
	UsuarioRepository usuarioRepository;
    @Autowired
    RoleRepository roleRepository;       
    Disco disco = new Disco();
	
	@GetMapping("/usuario")
	List<Usuario> listaUsuario() {
		return usuarioRepository.findAll();
	}
	
	@GetMapping("/usuario/{id}")
	Optional<Usuario> usuarioPorId(@PathVariable(value="id") Long id) {
		return usuarioRepository.findById(id);
	}
	
	@PostMapping("/usuario")
	ResponseEntity<String> salvarUsuario(@RequestBody NovoUsuarioDTO usuarioDTO) throws Exception {
            System.out.println("DTO: "+ usuarioDTO.toString());
            try {
            	Usuario novoUsuario = new Usuario();
                novoUsuario.parse(usuarioDTO, roleRepository);
                //Salvar
                System.out.println("Novo Usuario" + novoUsuario.toString());
                usuarioRepository.saveAndFlush(novoUsuario);
                return ResponseEntity.status(HttpStatus.OK).body(novoUsuario.toString());
            } catch (Exception ex) {
                Logger.getLogger(UsuarioEndpoint.class.getName()).log(Level.SEVERE, null, ex);
                return ResponseEntity.badRequest().body("Erro ao criar usuário");
            }   
	}     
}
