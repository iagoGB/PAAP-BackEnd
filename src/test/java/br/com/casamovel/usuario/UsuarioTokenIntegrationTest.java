package br.com.casamovel.usuario;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;
import org.springframework.http.HttpStatus;

import br.com.casamovel.CasamovelApplicationIntegrationIT;

public class UsuarioTokenIntegrationTest extends CasamovelApplicationIntegrationIT {

    @Test
    public void deveRetornarRecursosDeAdministradorAoTentarAutenticar() throws Exception {
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
            );
    }

    @Test
    public void deveRetornarRecursosDeUsuarioAoTentarAutenticar() throws Exception {
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
            );
    }
    @Test
    public void deveRetornarNaoAutorizadoAoTentarAutenticar() throws Exception {
        given()
                .port(porta)
                .body("{ \"email\":\"inexistente@teste.com\", \"senha\":\"abc\"}")
                .post(URILogin)
                .then()
                .log().body().and()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }
}