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
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
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

    private List<Usuario> usuarios;

    private List<Usuario> initUsers() {

        var user1 = Usuario.builder().id(10L)
            .nome("Tidinha")
            .email("teste@teste.com")
            .build();
        
        var user2 = Usuario.builder().id(11L)
            .nome("Usuario Dois")
            .email("outrousuario@teste.com")
            .build();

        var user3 = Usuario.builder().id(12L)
            .nome("Terceiro Usuario")
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
        assertEquals(usuario.getNome(), response.getBody().getName()); 
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

    @Test
    public void deveTrazerUmaPaginaDeUsuariosComSucesso(){
        Pageable pagination = PageRequest.of(0,1);

        Page<Usuario> userPage = new PageImpl<Usuario>(usuarios,pagination,usuarios.size());
        PagedListHolder pagedListHolder = new PagedListHolder(usuarios);
        System.out.println(pagedListHolder.getPageSize());
        System.out.println(userPage.getContent());

        when(usuarioRepository.findAll(pagination))
            .thenReturn(userPage);
        

        ResponseEntity<Page<UsuarioDTO>> response = usuarioService.findAll(pagination);
        System.out.println(response.getBody().getContent());
        assertEquals(3L, response.getBody().getTotalElements());
        assertEquals(2, response.getBody().getContent().size());

    }
    

   

    
}