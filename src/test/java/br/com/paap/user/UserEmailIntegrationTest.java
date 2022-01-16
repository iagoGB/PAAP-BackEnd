package br.com.paap.user;

import org.junit.Test;
import org.springframework.http.HttpStatus;

import br.com.paap.PAAPApplicationIntegrationIT;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class UserEmailIntegrationTest extends PAAPApplicationIntegrationIT {
    @Test
    public void deveRetornarUmUsuarioExistenteEStatus200OK() throws Exception {
        given()
            .port(porta)
            .header("Authorization", usuarioAutenticado.getToken())
            .param("username", usuarioAutenticado.getUsername())
        .get("/user/email")
        .then()
            .log().all().and()
            .statusCode(HttpStatus.OK.value())
            .and()
            .body(
                "email", equalTo("usuario@teste.com")
            );
    }
    @Test
    public void deveRetornarErroAoConsultarEmailInexistenteEStatusNotFound() throws Exception {
        given()
            .port(porta)
            .header("Authorization", usuarioAutenticado.getToken())
            .param("username", "emailinexistente@email.com")
        .get("/user/email")
        .then()
            .log().all().and()
            .statusCode(HttpStatus.NOT_FOUND.value());
    }
}