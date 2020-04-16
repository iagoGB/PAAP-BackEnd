package br.com.casamovel.usuario;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import br.com.casamovel.CasamovelApplicationTests;

@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class UsuarioResourceTest extends CasamovelApplicationTests {

    private final String URI = "/login";
    private String adminToken = new String();
    private String userToken = new String();

    @Before
    public void setUp(){

    }

    @Test
    public void deveRetornarRecursosDeAdministradorAoTentarAutenticar() throws Exception {
        adminToken = given()
            .port(porta)
            .body("{ \"email\":\"admin\", \"senha\":\"abc\"}")
        .post(URI)
        .then()
            .log().body().and()
            .statusCode(HttpStatus.OK.value())
            .and()
            .body(
                "role", equalTo("ADMIN"),
                "username", equalTo("admin")
            )
        .extract()
            .jsonPath().get("token");
        System.out.println("-------------------ADMIN-------------------:"+ this.adminToken );
    }

    @Test
    public void deveRetornarRecursosDeUsuarioAoTentarAutenticar() throws Exception {
        userToken = given()
            .port(porta)
            .body("{ \"email\":\"usuario@teste.com\", \"senha\":\"abc\"}")
        .post(URI)
        .then()
            .log().body().and()
            .statusCode(HttpStatus.OK.value())
            .and()
            .body(
                "role", equalTo("USER"),
                "username", equalTo("usuario@teste.com")
            )
        .extract()
            .jsonPath().get("token");
        
        System.out.println("-------------------USER-------------------"+ this.userToken );

    }
}