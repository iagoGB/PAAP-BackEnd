package br.com.paap.service;

import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import br.com.paap.dto.user.ChangePasswordDTO;
import br.com.paap.dto.user.NewUserDTO;
import br.com.paap.dto.user.UpdateUserDTO;
import br.com.paap.dto.user.UserDTO;
import br.com.paap.model.User;
import br.com.paap.repository.RoleRepository;
import br.com.paap.repository.UserRepository;

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

    public ResponseEntity<?> save
    (
        MultipartFile image,
        final String user
    ) throws JsonMappingException, JsonProcessingException
    {
    
        var newUserDTO = objectMapper.readValue(user, NewUserDTO.class);
        final User newUserModel = new User();
        var findByEmail = this.usuarioRepository.findByEmail(newUserDTO.getEmail());
        if (findByEmail.isPresent())
            throw new RuntimeException(
                    String.format("O Email %s já está em uso", newUserDTO.getEmail()));
        
        newUserModel.parse(newUserDTO, roleRepository);
        usuarioRepository.save(newUserModel);
        String urlAvatar = null;
        if (image != null)
            urlAvatar = this.s3StorageService.saveImage(image, newUserModel.getId(), this.USERS_FOLDER);
        if (urlAvatar != null)
            newUserModel.setAvatar(urlAvatar);
        usuarioRepository.save(newUserModel);
        return ResponseEntity.ok().build();
       
    }

    @Transactional
    public ResponseEntity<?> update(MultipartFile image, String user, Long id) {
        try {
            var updatedUser = objectMapper.readValue(user, UpdateUserDTO.class);
            return this.usuarioRepository.findById(id)
                    .map(u -> {
                        this.checkIfEmailIsUsed(updatedUser, u);
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

    private void checkIfEmailIsUsed(UpdateUserDTO updatedUser, User u) {
        if (!u.getEmail().equals(updatedUser.getEmail())) {
            var findByEmail = this.usuarioRepository.findByEmail(updatedUser.getEmail());
            if (findByEmail.isPresent())
                throw new RuntimeException(
                        String.format("O Email %s já está em uso", updatedUser.getEmail()));
        }
    }

    private void checkIfEmailIsUsed(NewUserDTO updatedUser, User u) {
        if (!u.getEmail().equals(updatedUser.getEmail())) {
            var findByEmail = this.usuarioRepository.findByEmail(updatedUser.getEmail());
            if (findByEmail.isPresent())
                throw new RuntimeException(
                        String.format("O Email %s já está em uso", updatedUser.getEmail()));
        }
    }

    public ResponseEntity<?> updateComplete(MultipartFile image, String user, Long id) {
        try {
            var updatedUser = objectMapper.readValue(user, NewUserDTO.class);
            return usuarioRepository.findById(id).map(u -> {
                this.checkIfEmailIsUsed(updatedUser, u);
                String urlAvatar = null;
                if (image != null)
                    urlAvatar = this.s3StorageService.saveImage(image, id, this.USERS_FOLDER);
                if (urlAvatar != null)
                    u.setAvatar(urlAvatar);
                u.setEmail(updatedUser.getEmail());
                u.setCpf(updatedUser.getCpf());
                u.setDepartament(updatedUser.getDepartament());
                u.setWorkload(updatedUser.getWorkload());
                u.setEntryDate(updatedUser.getEntryDate());
                u.setName(updatedUser.getName());
                u.setEmail(updatedUser.getEmail());
                u.setPhone(updatedUser.getPhone());
            
                return ResponseEntity.ok().build();
            }).orElseThrow(()-> new RuntimeException(String.format("Usuário com ID %s não existe", id)));

        } catch (JsonMappingException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao converter usuário");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao converter usuário");
        }

    }

    public ResponseEntity<?> findByName(String query) {
        var foundUsers = this.usuarioRepository.findByNameContainingIgnoreCase(query);
        var users = foundUsers.stream().map(u-> UserDTO.parse(u)).collect(Collectors.toList());
        return ResponseEntity.ok().body(users);
    }

    public ResponseEntity<?> changePassword(Long id, ChangePasswordDTO data) {
        return this.usuarioRepository.findById(id).map(u -> {
            var passwordEncoder = new BCryptPasswordEncoder();
            if (passwordEncoder.matches(data.getCurrentPassword(), u.getPassword())) {
                var encodedNewPassword = new BCryptPasswordEncoder().encode(data.getNewPassword());
                u.setPassword(encodedNewPassword);
                this.usuarioRepository.save(u);
                return ResponseEntity.ok().build();
            } else 
                throw new RuntimeException("Senha atual não confere");
        }).orElseThrow(()->  new RuntimeException("Usuário não encontrado"));
    }

}