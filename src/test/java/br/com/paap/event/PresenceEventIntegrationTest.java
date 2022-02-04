package br.com.paap.event;

import org.junit.Test;

import br.com.paap.PAAPApplicationIntegrationIT;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import io.restassured.http.ContentType;

/**
 * PresencaEventoTest
 */
public class PresenceEventIntegrationTest extends PAAPApplicationIntegrationIT  {
    private static final String USUARIO_EMAIL_PATH = "/user/email";

    @Test
    public void deveRegistrarPresencaECargaHorariaDeUsuarioJaInscritoEmEventoExistente() {
        given()
            .port(porta)
            .header("Authorization", usuarioAutenticado.getToken())
            .contentType(ContentType.JSON)
            .body("{\"keyword\":\"YYZZ15-20\", \"username\":\"usuario@teste.com\"}")
            .pathParam("eventoId", 20)
            .log().all()
        .when()
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
            .param("username", usuarioAutenticado.getUsername())
        .when()
            .get(USUARIO_EMAIL_PATH)
        .then()
            .statusCode(equalTo(200)) // OK
            .and()
            .log().body()
            .and()
            .body(
                "events", hasSize(2),
                "events[0].id", equalTo(20),
                "events[0].presente", equalTo(true),
                //1500 o usuário já possuia, O evento registrado possui 4h
                "workload", equalTo(1500+(4*60))
            );     
    }

    @Test
    public void deveRetornarUmErroCasoUsuarioTenteRegistrarPresencaNovamente() {
        given()
            .port(porta)
            .header("Authorization", usuarioAutenticadoTres.getToken())
            .contentType(ContentType.JSON)
            .body("{\"keyword\":\"ZZRot-50\", \"username\":\"tres@usuario.com\"}")
            .pathParam("eventoId", 50)
            .log().all()
        .when()
            .put("/evento/{eventoId}/regi-presenca")
        .then()
            .statusCode(equalTo(400)) // Bad Request
            .and()
            .body("erro", equalTo("Sua presença já foi registrada anteriormente"))
            .log().body();

        // Checa se o evento vem nos dados do usuário, se já havia presença e se a carga horária
        // não foi computada
        given()
            .port(porta)
            .header("Authorization", usuarioAutenticadoTres.getToken())
            .param("username", usuarioAutenticadoTres.getUsername())
        .when()
            .get(USUARIO_EMAIL_PATH)
        .then()
            .statusCode(equalTo(200)) // OK
            .and()
            .log().body()
            .and()
            .body(
                "events", hasSize(2),
                "events[0].id", equalTo(50),
                "events[0].presente", equalTo(true),
                "workload", equalTo(600)
            );     
    }

    @Test
    public void naoDeveCadastrarCasoCodigoNaoBataComRegistradoDoEvento(){
        given()
            .port(porta)
            .header("Authorization", usuarioAutenticadoTres.getToken())
            .contentType(ContentType.JSON)
            .body("{\"keyword\":\"CodigoInvalido-10\", \"username\":\"tres@usuario.com\"}")
            .pathParam("eventoId", 10)
            .log().all()
        .when()
            .put("/evento/{eventoId}/registro-presenca")
        .then()
            .statusCode(equalTo(400)) // Bad Request
            .and()
            .body("erro", equalTo("Código do evento inválido"))
            .log().body();
    }
}