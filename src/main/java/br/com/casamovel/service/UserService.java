package br.com.casamovel.service;

import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.casamovel.dto.usuario.NewUserDTO;
import br.com.casamovel.dto.usuario.UsuarioDTO;
import br.com.casamovel.model.User;
import br.com.casamovel.repository.RoleRepository;
import br.com.casamovel.repository.UserRepository;

@Service
public class UserService {

    @Autowired UserRepository usuarioRepository;
    @Autowired RoleRepository roleRepository;

    public ResponseEntity<Page<UsuarioDTO>> findAll(Pageable pagination){
        var userPage = usuarioRepository.findAll(pagination);
		return  ResponseEntity.ok().body(UsuarioDTO.parse(userPage));
    }
    

    public ResponseEntity<UsuarioDTO> findById(Long id) {
        return usuarioRepository.findById(id)
            .map( user -> ResponseEntity.ok().body(UsuarioDTO.parse(user)))
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }
    
    public ResponseEntity<UsuarioDTO> findByEmail(String username) {
    	return usuarioRepository.findByEmail(username)
    			.map(user -> ResponseEntity.ok().body(UsuarioDTO.parse(user)))
    			.orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<UsuarioDTO> save
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
            final UsuarioDTO usuarioDTO = UsuarioDTO.parse(novoUsuario);
            // Caminho do novo recurso criado
            final URI uri = uriBuilder.path("/usuario/{id}").buildAndExpand(novoUsuario.getId()).toUri();
            return ResponseEntity.created(uri).body(usuarioDTO);
        } catch (final Exception ex) {
            return ResponseEntity.badRequest().build();
        }
       
    }

}