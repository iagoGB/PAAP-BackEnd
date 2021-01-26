package br.com.casamovel.service;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.casamovel.dto.usuario.NovoUsuarioDTO;
import br.com.casamovel.dto.usuario.UsuarioDTO;
import br.com.casamovel.model.Usuario;
import br.com.casamovel.repository.RoleRepository;
import br.com.casamovel.repository.UsuarioRepository;
import javassist.NotFoundException;

@Service
public class UsuarioService {
    @Autowired UsuarioRepository usuarioRepository;
    @Autowired RoleRepository roleRepository;

    public Usuario findById(Long id) throws NotFoundException {
        Optional<Usuario> findById = usuarioRepository.findById(id);
        if (findById.isPresent())
            return findById.get();
        else
            throw new NotFoundException("Usuário não encontrado");
    }
    
    public ResponseEntity<UsuarioDTO> findByEmail(String username) {
    	System.out.println("old " +username);
    	return usuarioRepository.findByEmail(username)
    			.map(user -> {
    				System.out.println(user.getId());
    				System.out.println(user.getNome());

    				return ResponseEntity.ok().body(UsuarioDTO.parse(user));
    			})
    			.orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<UsuarioDTO> save
    (
        final NovoUsuarioDTO NovoUsuarioDTO, 
        final UriComponentsBuilder uriBuilder
    )
    {
        try {
            final Usuario novoUsuario = new Usuario();
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