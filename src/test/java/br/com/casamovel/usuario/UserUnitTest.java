package br.com.casamovel.usuario;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;


import br.com.casamovel.dto.usuario.UsuarioDTO;
import br.com.casamovel.model.User;
import br.com.casamovel.repository.UserRepository;
import br.com.casamovel.service.UserService;

/**
 * UsuarioUnitTest
 */
public class UserUnitTest {

    @InjectMocks
    private UserService usuarioService;

    @Mock
    private UserRepository usuarioRepository;

    private List<User> usuarios;

    private List<User> initUsers() {

        var user1 = User.builder().id(10L)
            .name("Tidinha")
            .email("teste@teste.com")
            .build();
        
        var user2 = User.builder().id(11L)
            .name("Usuario Dois")
            .email("outrousuario@teste.com")
            .build();

        var user3 = User.builder().id(12L)
            .name("Terceiro Usuario")
            .email("usuariotres@teste.com")
            .build();
        
        return Arrays.asList(user1,user2,user3);
       
	}

    @BeforeEach
    public void setup(){
        MockitoAnnotations.initMocks(this);
        usuarios = initUsers();
    }
    
    @Test
    public void deveEncontrarUsuarioAPartirDoIDComSucesso() {
        var usuario = usuarios.get(0);
        when(usuarioRepository.findById(usuario.getId()))
            .thenReturn(Optional.of(usuario));

        var response = usuarioService.findById(usuario.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode()); 
        assertEquals(UsuarioDTO.class, response.getBody().getClass());
        assertEquals(usuario.getName(), response.getBody().getName()); 
    }

    
    @Test
    public void deveLancarUmaExcecaoAoTentarBuscarUsuarioPorIDQueNaoExiste() {
        var usuario = usuarios.get(0); 
        when(usuarioRepository.findById(usuario.getId()))
            .thenReturn(Optional.ofNullable(null));

        var result = assertThrows(RuntimeException.class, () -> usuarioService.findById(usuario.getId()));

        assertEquals("Usuário não encontrado", result.getMessage());   
    }

    @Test
    public void deveBuscarUsuarioPorEmailComSucesso() {
        var usuario = usuarios.get(0);
        when(usuarioRepository.findById(usuario.getId()))
            .thenReturn(Optional.ofNullable(usuario));

        var result = usuarioService.findById(usuario.getId());
        
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(UsuarioDTO.class, result.getBody().getClass()); 
        assertEquals(usuario.getEmail(), result.getBody().getEmail());  
    }

    @Test
    public void deveRetornarStatus404AoTentarBuscarUsuarioPorEmailQueNaoExiste() {
        var usuario = usuarios.get(0);
        when(usuarioRepository.findByEmail(usuario.getEmail()))
            .thenReturn(Optional.ofNullable(null));

        var result = usuarioService.findByEmail(usuario.getEmail());

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());   
    }
        
}