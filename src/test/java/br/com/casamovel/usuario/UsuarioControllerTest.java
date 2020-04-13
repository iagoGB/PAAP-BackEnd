package br.com.casamovel.usuario;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.casamovel.dto.usuario.NovoUsuarioDTO;
import br.com.casamovel.dto.usuario.UsuarioDTO;
import br.com.casamovel.endpoint.UsuarioEndpoint;
import br.com.casamovel.model.Usuario;
import br.com.casamovel.repository.RoleRepository;
import br.com.casamovel.repository.UsuarioRepository;
import br.com.casamovel.service.UsuarioService;

@RunWith(SpringRunner.class)
@WebMvcTest(value=UsuarioEndpoint.class, secure = false)
public class UsuarioControllerTest {

	@Autowired private MockMvc mockMvc;
	@MockBean private UsuarioService usuarioService;
	@Autowired private ObjectMapper objectMapper;
	@MockBean private RoleRepository RoleRepository;
	@MockBean private UsuarioRepository usuarioRepository;

	private final String uri = "/usuario";
	private final Long id = 1L;
	private Usuario usuario;
	private NovoUsuarioDTO novoUsuarioDTO;
	private UsuarioDTO UsuarioDTO;
	// Em produção quem serve a dependência variavel é o proprio spring boot
	private final UriComponentsBuilder uriBuilder = null;

	@Before
	public void setUp(){
		// Usuario Modelo
		this.usuario = Usuario.builder()
			.id(id)
			.nome("José")
			.email("usuario@usuario.com")
			.cargaHoraria( LocalTime.of(0, 0))
			.build();
		// Usuario DTO 
		this.UsuarioDTO = new UsuarioDTO(usuario);
		// Novo Usuário para cadastro com todos os campos preenchidos
		this.novoUsuarioDTO =  NovoUsuarioDTO.builder()
			.cpf(9999L)
			.data_ingresso(LocalDate.of(2011, 9, 10))
			.departamento("Ciências")
			.email("usuario@email.com")
			.nome("Ingrid Guimarães")
			.senha("abc")
			.telefone("33339097")
			.build();
		
	}
	
	@Test
	public void pegarUsuarioPorIdSucesso() throws Exception {
		// Dado 
		when(usuarioService.findById(id)).thenReturn(usuario);
		// Quando 
		this.mockMvc.perform(get(uri+"/1")
			.accept(MediaType.APPLICATION_JSON))
			// Então		
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(status().isOk())
			.andDo(print())
			.andExpect(jsonPath("$.nome",is("José")));		
	}
	
	@Test
	public void dadoNovoUsuarioQuandoCadastrarComCamposNulosResponderCom400BadRequest() throws Exception {
		novoUsuarioDTO.setCpf(null);
		// Dado 
		when(usuarioService.save(novoUsuarioDTO, uriBuilder)).thenReturn(new ResponseEntity<UsuarioDTO>(UsuarioDTO, HttpStatus.CREATED));
		// Quando 
		this.mockMvc.perform(post(uri)
			.contentType("application/json")
			.content(objectMapper.writeValueAsString(novoUsuarioDTO)))
			.andExpect(status().isBadRequest())
			.andDo(print())
			.andReturn();
	}

	@Test
	public void dadoNovoUsuarioQuandoCadastrarComCamposPreenchidosCorretamenteResponderCom201Created() throws Exception {
		// Dado 
		when(usuarioService.save(novoUsuarioDTO, uriBuilder)).thenReturn(new ResponseEntity<UsuarioDTO>(UsuarioDTO, HttpStatus.CREATED));
		// Quando 
		this.mockMvc.perform(post(uri)
			.contentType("application/json")
			.content(objectMapper.writeValueAsString(novoUsuarioDTO)))
			// Então
			.andExpect(status().isOk())
			.andDo(print())
			.andReturn();
	}

}
