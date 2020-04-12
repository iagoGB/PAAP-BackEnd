package br.com.casamovel.service;

import java.net.URI;

import javax.xml.ws.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.casamovel.dto.usuario.NovoUsuarioDTO;
import br.com.casamovel.dto.usuario.UsuarioDTO;
import br.com.casamovel.model.Usuario;
import br.com.casamovel.repository.RoleRepository;
import br.com.casamovel.repository.UsuarioRepository;

@Service
public class UsuarioService {
    @Autowired UsuarioRepository usuarioRepository;
    @Autowired RoleRepository roleRepository; 
    
    public ResponseEntity<?> findByEmail(String username) {
        Usuario findByEmail = usuarioRepository.findByEmail(username);
        if (findByEmail == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(UsuarioDTO.parse(findByEmail));
    }

    public ResponseEntity<UsuarioDTO> save
    (
        NovoUsuarioDTO NovoUsuarioDTO, 
        UriComponentsBuilder uriBuilder
    )
    {
        try {
            Usuario novoUsuario = new Usuario();
            novoUsuario.parse(NovoUsuarioDTO, roleRepository);
            // Salvar
            usuarioRepository.save(novoUsuario);
            UsuarioDTO usuarioDTO = UsuarioDTO.parse(novoUsuario);
            // Caminho do novo recurso criado
            URI uri = uriBuilder.path("/usuario/{id}").buildAndExpand(novoUsuario.getId()).toUri();
            return ResponseEntity.created(uri).body(usuarioDTO);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
       
    }

}