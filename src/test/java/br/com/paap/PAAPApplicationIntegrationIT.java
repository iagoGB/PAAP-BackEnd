package br.com.paap;

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

import br.com.paap.dto.authentication.AuthenticationResponseDTO;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@TestPropertySource(locations="classpath:/test.properties")
public abstract class PAAPApplicationIntegrationIT {
	
	@Value("${local.server.port}")
	protected  int porta;
	protected  final String URILogin = "/login";
	protected  AuthenticationResponseDTO administradorAutenticado;
	protected  AuthenticationResponseDTO usuarioAutenticado;
	protected AuthenticationResponseDTO usuarioAutenticadoTres;

	// @Test
	// public void contextLoads() {
		
	// }

	@Before
	public void setUp(){
		administradorAutenticado = 
			given()
				.port(porta)
				.body("{ \"email\":\"admin\", \"password\":\"abc\"}")
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
				.as(AuthenticationResponseDTO.class);

		usuarioAutenticado = 
			given()
				.port(porta)
				.body("{ \"email\":\"usuario@teste.com\", \"password\":\"abc\"}")
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
				.as(AuthenticationResponseDTO.class);
		
				usuarioAutenticadoTres = 
				given()
					.port(porta)
					.body("{ \"email\":\"tres@usuario.com\", \"password\":\"abc\"}")
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
					.as(AuthenticationResponseDTO.class);
	}
}
