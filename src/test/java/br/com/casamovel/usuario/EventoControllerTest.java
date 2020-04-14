package br.com.casamovel.usuario;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import br.com.casamovel.endpoint.EventoEndpoint;
import br.com.casamovel.service.EventoService;

@RunWith(SpringRunner.class)
@WebMvcTest(value=EventoEndpoint.class, secure = false)
public class EventoControllerTest {

    private final String uri = "/evento/1/inscricao/";
    @Autowired private MockMvc mockMvc;
    @MockBean public EventoService es;
    @Autowired private ObjectMapper objectMapper;

    @Test
    public void dadoUsuarioExistenteEAindaNaoInscritoEEventoExisteEntaoRetornar201Created() throws Exception {
		// Dado 
		// Quando 
		this.mockMvc.perform( post(uri)
            .contentType("application/json")
            .param("usermail","usuario@usuario.com"))
			.andExpect(status().isCreated())
			.andDo(print())
            .andReturn();
    }
}