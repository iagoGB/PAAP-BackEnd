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
    public void deveRegistrarPresencaECargaHorariaDeUsuarioJaInscritoEmEventoExistente() {
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

    @Test
    public void deveRetornarUmErroCasoUsuarioTenteRegistrarPresencaNovamente() {
        given()
            .port(porta)
            .header("Authorization", usuarioAutenticadoTres.getToken())
            .contentType(ContentType.JSON)
            .body("{\"keyword\":\"ZZRot-50\", \"username\":\"tres@usuario.com\"}")
            .pathParam("eventoId", 50)
            .log().all()
            .put("/evento/{eventoId}/registro-presenca")
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
            .pathParam("username", usuarioAutenticadoTres.getUsername())
            .get("/usuario/username/{username}")
        .then()
            .statusCode(equalTo(200)) // OK
            .and()
            .log().body()
            .and()
            .body(
                "eventos", hasSize(2),
                "eventos[0].id", equalTo(50),
                "eventos[0].presente", equalTo(true),
                "carga_horaria", equalTo("03:00:00")
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
            .put("/evento/{eventoId}/registro-presenca")
        .then()
            .statusCode(equalTo(400)) // Bad Request
            .and()
            .body("erro", equalTo("Código do evento inválido"))
            .log().body();
    }
}