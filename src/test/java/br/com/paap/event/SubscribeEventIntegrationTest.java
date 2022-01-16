package br.com.paap.event;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

import br.com.paap.PAAPApplicationIntegrationIT;

public class SubscribeEventIntegrationTest extends PAAPApplicationIntegrationIT {
    // O usuário já possui 2 eventos cadastrados ao inicializar o banco
    private static final String USUARIO_EMAIL_PATH = "/user/email";
    private static final String EVENTO_POR_ID_PATH = "/evento/{eventoId}";
    private static final String EVENTO_INSCRICAO_PATH = "/evento/{eventoId}/inscricao";
    private static final String REMOVER_INSCRICAO_PATH = "/evento/{eventoId}/remover-inscricao";

    @Test
    public void DeveCadastrarUsuarioExistenteEAindaNaoInscritoEmEventoExistenteERetornar201(){
        // Se inscreve com usuário comum
        given()
            .port(porta)
            .param("username",usuarioAutenticado.getUsername())
            .header("Authorization", usuarioAutenticado.getToken())
        .when()
            .pathParam("eventoId", 10)
            // .log().all()
            .put(EVENTO_INSCRICAO_PATH)
        .then()
            .statusCode(equalTo(201)) // Created
            .and()
            .log().body();
        
        // Checa com adm se evento vem com usuario incluso 
        given()
            .port(porta)
            .header("Authorization", administradorAutenticado.getToken())
            .pathParam("eventoId", 10)
        .when()
            .get(EVENTO_POR_ID_PATH)
        .then()
            .statusCode(equalTo(200)) // OK
            .and()
            .log().body();
        // Checa se o evento vem nos dados do usuário
        given()
            .port(porta)
            .header("Authorization", administradorAutenticado.getToken())
            .param("username", usuarioAutenticado.getUsername())
        .when()
            .get(USUARIO_EMAIL_PATH)
        .then()
            .statusCode(equalTo(200)) // OK
            .and()
            .log().body()
            .and()
            .body(
                "events", hasSize(3), // Agora será 3
                "events[2].id", equalTo(10)
            );     
    }

    @Test
    public void DeveLancarUmErroCasoUsuarioJaCadastradoEmEventoTenteInscricaoNovamente(){
        // Se inscreve em um evento no qual já possui cadastro
        given()
            .port(porta)
            .param("username",usuarioAutenticado.getUsername())
            .header("Authorization", usuarioAutenticado.getToken())
            .pathParam("eventoId", 20)
        .when()
            .put(EVENTO_INSCRICAO_PATH)
        .then()
            .statusCode(equalTo(400)) // BadRequest
            .and()
            .body("mensagem", equalTo("Usuário já esta inscrito"))
            .and()
            .log().body();
        
        // Checa se o evento vem nos dados do usuário
        given()
            .port(porta)
            .header("Authorization", administradorAutenticado.getToken())
            .param("username", usuarioAutenticado.getUsername())
        .when()
            .get(USUARIO_EMAIL_PATH)
        .then()
            .statusCode(equalTo(200)) // OK
            .and()
            .log().body()
            .and()
            .body(
                "events", hasSize(2)// Deve continuar dois eventos
            );      
    }

    @Test
    public void deveRemoverUsuarioDeUmEventoPreviamenteCadastrado() {
        // Deve remover inscrição de um evento ao qual tenha se cadastrado
        given()
            .port(porta)
            .param("username",usuarioAutenticado.getUsername())
            .header("Authorization", usuarioAutenticado.getToken())
            .pathParam("eventoId", 20)
        .when()
            .put(REMOVER_INSCRICAO_PATH)
        .then()
            .statusCode(equalTo(200)) // Status OK
            .and()
            .log().body();

        // Checa se o evento vem nos dados do usuário
        given()
            .port(porta)
            .header("Authorization", administradorAutenticado.getToken())
            .param("username", usuarioAutenticado.getUsername())
        .when()
            .get(USUARIO_EMAIL_PATH)
        .then()
            .statusCode(equalTo(200)) // OK
            .and()
            .body("events", hasSize(1))
            .and()
            .log().body();// Deve conter apenas um evento);  
    }

    @Test  
    public void deveLancarExcecaoAoTentarRemoverInscricaoQueNaoExiste() {
         // O usuário tenta deletar inscrição que não existe, com um evento existente.
        given()
            .port(porta)
            .param("username",usuarioAutenticado.getUsername())
            .header("Authorization", usuarioAutenticado.getToken())
            .pathParam("eventoId",10)
        .when()
            .put(REMOVER_INSCRICAO_PATH)
        .then() 
            .statusCode(equalTo(400)) // Bad Request
            .and()
            .body("mensagem", equalTo("O usuário não possui vínculo com evento"))
            .and()
            .log().body();
    }
}