package br.com.casamovel.usuario;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import br.com.casamovel.CasamovelApplicationTests;
import br.com.casamovel.endpoint.UsuarioEndpoint;
 
public class UsuarioControllerTest  extends CasamovelApplicationTests {
	@Autowired UsuarioEndpoint usuarioEndpoint;

	private MockMvc mockMvc;

	@Before
	public void setUp(){
		this.mockMvc = MockMvcBuilders.standaloneSetup(usuarioEndpoint).build();
	}
	
	@Test
	public void pegarUsuarioPorIdSucesso() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/1")).andExpect(MockMvcResultMatchers.status().isOk());
	}

}
