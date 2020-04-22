package br.com.casamovel.evento;

import org.junit.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import br.com.casamovel.CasamovelApplicationTests;
import io.restassured.http.ContentType;

/**
 * PresencaEventoTest
 */
public class PresencaEventoTest extends CasamovelApplicationTests {

    @Test
    public void deveRegistrarPresencaDeUsuarioJaInscritoEmEventoExistente() {
        given()
            .port(porta)
            .header("Authorization", usuarioAutenticado.getToken())
            .contentType(ContentType.JSON)
            .body("{\"keyword\":\"YYZZ15-20\", \"username\":\"usuario@teste.com\"}")
            .pathParam("eventoId", 20)
            .log().all()
            .put("/evento/{eventoId}/registro-presenca")
        .then()
            .statusCode(equalTo(200)) // Created
            .and()
            .log().body();

        // Checa se o evento vem nos dados do usuário, se a presença foi registrada e se a carga horária
        // foi computada
        given()
            .port(porta)
            .header("Authorization", usuarioAutenticado.getToken())
            .pathParam("username", usuarioAutenticado.getUsername())
            .get("/usuario/username/{username}")
        .then()
            .statusCode(equalTo(200)) // OK
            .and()
            .log().body()
            .and()
            .body(
                "eventos", hasSize(2),
                "eventos[1].id", equalTo(20),
                "eventos[1].presente", equalTo(true),
                "carga_horaria", equalTo("04:00:00")
            );     
    }
}