package br.com.casamovel.evento;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import org.junit.Test;

import br.com.casamovel.CasamovelApplicationTests;

public class InscricaoEventoTest extends CasamovelApplicationTests{
    // O usuário já possui 2 eventos cadastrados ao inicializar o banco
    @Test
    public void DeveCadastrarUsuarioExistenteEAindaNaoInscritoEmEventoExistenteERetornar201(){
        // Se inscreve com usuário comum
        given()
            .port(porta)
            .param("username",usuarioAutenticado.getUsername())
            .header("Authorization", usuarioAutenticado.getToken())
            .pathParam("eventoId", 10)
            // .log().all()
            .put("/evento/{eventoId}/inscricao")
        .then()
            .statusCode(equalTo(201)) // Created
            .and()
            .log().body();
        
        // Checa com adm se evento vem com usuario incluso 
        given()
            .port(porta)
            .header("Authorization", administradorAutenticado.getToken())
            .pathParam("eventoId", 10)
            .get("/evento/{eventoId}")
        .then()
            .statusCode(equalTo(200)) // OK
            .and()
            .log().body();
        // Checa se o evento vem nos dados do usuário
        given()
            .port(porta)
            .header("Authorization", administradorAutenticado.getToken())
            .pathParam("username", usuarioAutenticado.getUsername())
            .get("/usuario/username/{username}")
        .then()
            .statusCode(equalTo(200)) // OK
            .and()
            .log().body()
            .and()
            .body(
                "eventos", hasSize(3), // Agora será 3
                "eventos[2].titulo", equalTo("Evento Spring Boot Test")
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
            .put("/evento/{eventoId}/inscricao")
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
            .pathParam("username", usuarioAutenticado.getUsername())
            .get("/usuario/username/{username}")
        .then()
            .statusCode(equalTo(200)) // OK
            .and()
            .log().body()
            .and()
            .body(
                "eventos", hasSize(2), // Deve continuar dois eventos
                "eventos[0].titulo", equalTo("Treinamento de Educação a Distância Para Professores")
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
            .put("/evento/{eventoId}/remover-inscricao")
        .then()
            .statusCode(equalTo(200)) // BadRequest
            .and()
            .log().body();

        // Checa se o evento vem nos dados do usuário
        given()
            .port(porta)
            .header("Authorization", administradorAutenticado.getToken())
            .pathParam("username", usuarioAutenticado.getUsername())
            .get("/usuario/username/{username}")
        .then()
            .statusCode(equalTo(200)) // OK
            .and()
            .body("eventos", hasSize(1))
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
            .put("/evento/{eventoId}/remover-inscricao")
        .then() 
            .statusCode(equalTo(500)) // Erro interno
            .and()
            .body("message", equalTo("O usuário não possui vínculo com evento"))
            .and()
            .log().body();
    }
}