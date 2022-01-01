package br.com.casamovel.service;

import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.casamovel.dto.usuario.NewUserDTO;
import br.com.casamovel.dto.usuario.UserDTO;
import br.com.casamovel.model.User;
import br.com.casamovel.repository.RoleRepository;
import br.com.casamovel.repository.UserRepository;

@Service
public class UserService {

    @Autowired UserRepository usuarioRepository;
    @Autowired RoleRepository roleRepository;

    public ResponseEntity<Page<UserDTO>> findAll(Pageable pagination){
        var userPage = usuarioRepository.findAll(pagination);
		return  ResponseEntity.ok().body(UserDTO.parse(userPage));
    }
    

    public ResponseEntity<UserDTO> findById(Long id) {
        return usuarioRepository.findById(id)
            .map( user -> ResponseEntity.ok().body(UserDTO.parse(user)))
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }
    
    public ResponseEntity<UserDTO> findByEmail(String username) {
    	return usuarioRepository.findByEmail(username)
    			.map(user -> ResponseEntity.ok().body(UserDTO.parse(user)))
    			.orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<UserDTO> save
    (
        final NewUserDTO NovoUsuarioDTO, 
        final UriComponentsBuilder uriBuilder
    )
    {
        try {
            final User novoUsuario = new User();
            novoUsuario.parse(NovoUsuarioDTO, roleRepository);
            // Salvar
            usuarioRepository.save(novoUsuario);
            final UserDTO userDTO = UserDTO.parse(novoUsuario);
            // Caminho do novo recurso criado
            final URI uri = uriBuilder.path("/usuario/{id}").buildAndExpand(novoUsuario.getId()).toUri();
            return ResponseEntity.created(uri).body(userDTO);
        } catch (final Exception ex) {
            return ResponseEntity.badRequest().build();
        }
       
    }

}