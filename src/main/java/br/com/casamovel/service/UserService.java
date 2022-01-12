package br.com.casamovel.service;

import java.net.URI;
import java.util.Optional;
import java.util.function.Function;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.casamovel.dto.usuario.NewUserDTO;
import br.com.casamovel.dto.usuario.UpdateUserDTO;
import br.com.casamovel.dto.usuario.UserDTO;
import br.com.casamovel.model.User;
import br.com.casamovel.repository.RoleRepository;
import br.com.casamovel.repository.UserRepository;

@Service
public class UserService {

    S3StorageService s3StorageService;
    UserRepository usuarioRepository;
    RoleRepository roleRepository;
    ObjectMapper objectMapper;
    String USERS_FOLDER;

    @Autowired
    UserService(UserRepository usuarioRepository, RoleRepository roleRepository, S3StorageService s3StorageService,
            ObjectMapper objectMapper, @Value("${users.folder}") String usersFolder) {
        this.usuarioRepository = usuarioRepository;
        this.roleRepository = roleRepository;
        this.s3StorageService = s3StorageService;
        this.objectMapper = objectMapper;
        this.USERS_FOLDER = usersFolder;
    }

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

    @Transactional
    public ResponseEntity<?> update(MultipartFile image, String user, Long id) {
        try {
            var updatedUser = objectMapper.readValue(user, UpdateUserDTO.class);
            return this.usuarioRepository.findById(id)
                    .map(u -> {
                        if (!u.getEmail().equals(updatedUser.getEmail())) {
                            var findByEmail = this.usuarioRepository.findByEmail(updatedUser.getEmail());
                            if (findByEmail.isPresent())
                                throw new RuntimeException(
                                        String.format("O Email %s já está em uso", updatedUser.getEmail()));
                        }
                        String urlAvatar = null;
                        if (image != null) urlAvatar = this.s3StorageService.saveImage(image, id, this.USERS_FOLDER);
                        if (urlAvatar != null) u.setAvatar(urlAvatar);
                        u.setEmail(updatedUser.getEmail());
                        u.setPhone(updatedUser.getPhone());
                        usuarioRepository.save(u);
                        return ResponseEntity.ok().build();
                }).orElseThrow(() -> new RuntimeException(String.format("Usuário com ID %s não existe", id)));
            
        } catch (JsonMappingException e) {
            throw new RuntimeException("Erro ao converter usuário");
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao converter usuário");
        }
    }

}