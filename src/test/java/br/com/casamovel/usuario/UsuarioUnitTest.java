package br.com.casamovel.usuario;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.casamovel.dto.usuario.UsuarioDTO;
import br.com.casamovel.model.Usuario;
import br.com.casamovel.repository.UsuarioRepository;
import br.com.casamovel.service.UsuarioService;

/**
 * UsuarioUnitTest
 */
public class UsuarioUnitTest {

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    private Usuario usuario;

    private Usuario initUser() {
        return Usuario.builder().id(10L)
            .nome("Tidinha")
            .email("teste@teste.com")
            .build();
       
	}

    @BeforeEach
    public void setup(){
        MockitoAnnotations.initMocks(this);
        usuario = initUser();
    }
    
    @Test
    public void deveEncontrarUsuarioAPartirDoIDComSucesso() {
            
        when(usuarioRepository.findById(usuario.getId()))
            .thenReturn(Optional.of(usuario));

        var response = usuarioService.findById(usuario.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode()); 
        assertEquals(UsuarioDTO.class, response.getBody().getClass());
        assertEquals(usuario.getNome(), response.getBody().getName()); 
    }

    
    @Test
    public void deveLancarUmaExcecaoAoTentarBuscarUsuarioPorIDQueNaoExiste() {
            
        when(usuarioRepository.findById(usuario.getId()))
            .thenReturn(Optional.ofNullable(null));

        var result = assertThrows(RuntimeException.class, () -> usuarioService.findById(usuario.getId()));

        assertEquals("Usuário não encontrado", result.getMessage());   
    }

    @Test
    public void deveBuscarUsuarioPorEmailComSucesso() {
        when(usuarioRepository.findById(usuario.getId()))
            .thenReturn(Optional.ofNullable(usuario));

        var result = usuarioService.findById(usuario.getId());
        
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(UsuarioDTO.class, result.getBody().getClass()); 
        assertEquals(usuario.getEmail(), result.getBody().getEmail());  
    }

    @Test
    public void deveRetornarStatus404AoTentarBuscarUsuarioPorEmailQueNaoExiste() {
            
        when(usuarioRepository.findByEmail(usuario.getEmail()))
            .thenReturn(Optional.ofNullable(null));

        var result = usuarioService.findByEmail(usuario.getEmail());

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());   
    }

   

    
}