package br.com.casamovel.endpoints;

import br.com.casamovel.models.Role;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.casamovel.models.Usuario;
import br.com.casamovel.repositories.RoleRepository;
import br.com.casamovel.repositories.UsuarioRepository;
import br.com.casamovel.util.Disco;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
	ResponseEntity<String> salvarUsuario(@RequestBody Usuario usuario) throws Exception{
            Usuario novoUsuario;
            String defaultImage = "C:/CASaMovel/usuarioAvatar/default.jpg";
            try {
                    //Por default todos os usuários iniciam-se como user
                   String encriptSenha = (new BCryptPasswordEncoder().encode(usuario.getSenha()));
                   Role rDefault = new Role();
                   rDefault = roleRepository.getOne("ROLE_USER");
                   //Lista de role que carrega user
                   List a = new ArrayList<Role>();
                   a.add(rDefault);
                   novoUsuario = new Usuario
                    (   
                        defaultImage,
                        usuario.getCpf(),
                        usuario.getNome(),
                        usuario.getEmail(),
                        encriptSenha,
                        usuario.getDepartamento(),
                        usuario.getTelefone(),
                        //Adiciona role user para o novo usuário
                        a,
                        usuario.getCarga_horaria(),
                        usuario.getData_ingresso(),
                        usuario.getCriado_em(),
                        usuario.getAtualizado_em(),
                        usuario.getEventos()
                    ); 
                   //Conecta usuario com role
                   rDefault.getUsuarios().add(novoUsuario);
                   System.out.println("novo Usuario:" + novoUsuario);
                   //Salvar
                   usuarioRepository.saveAndFlush(novoUsuario);
                   return ResponseEntity.status(HttpStatus.OK).body(novoUsuario.toString());
            } catch (Exception ex) {
                Logger.getLogger(UsuarioEndpoint.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Erro ao criar usuario"); 
                return ResponseEntity.badRequest().body("Erro ao criar usuário");
            }   
	}     
}
