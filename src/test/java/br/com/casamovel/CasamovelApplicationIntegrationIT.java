package br.com.casamovel;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.casamovel.dto.autenticacao.RespostaAutenticacao;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@TestPropertySource(value = "resources/application-test.properties")
public abstract class CasamovelApplicationIntegrationIT {
	
	@Value("${local.server.port}")
	protected  int porta;
	protected  final String URILogin = "/login";
	protected  RespostaAutenticacao administradorAutenticado;
	protected  RespostaAutenticacao usuarioAutenticado;
	protected RespostaAutenticacao usuarioAutenticadoTres;

	// @Test
	// public void contextLoads() {
		
	// }

	@Before
	public void setUp(){
		administradorAutenticado = 
			given()
				.port(porta)
				.body("{ \"email\":\"admin\", \"senha\":\"abc\"}")
				.post(URILogin)
			.then()
				.log().body().and()
				.statusCode(HttpStatus.OK.value())
				.and()
				.body(
					"role", equalTo("ADMIN"),
					"username", equalTo("admin")
				)
			.extract()
				.body()
				.as(RespostaAutenticacao.class);

		usuarioAutenticado = 
			given()
				.port(porta)
				.body("{ \"email\":\"usuario@teste.com\", \"senha\":\"abc\"}")
				.post(URILogin)
			.then()
				.log().body().and()
				.statusCode(HttpStatus.OK.value())
				.and()
				.body(
					"role", equalTo("USER"),
					"username", equalTo("usuario@teste.com")
				)
			.extract()
				.body()
				.as(RespostaAutenticacao.class);
		
				usuarioAutenticadoTres = 
				given()
					.port(porta)
					.body("{ \"email\":\"tres@usuario.com\", \"senha\":\"abc\"}")
					.post(URILogin)
				.then()
					.log().body().and()
					.statusCode(HttpStatus.OK.value())
					.and()
					.body(
						"role", equalTo("USER"),
						"username", equalTo("tres@usuario.com")
					)
				.extract()
					.body()
					.as(RespostaAutenticacao.class);
	}
}
