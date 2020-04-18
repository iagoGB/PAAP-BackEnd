package br.com.casamovel.evento;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import org.junit.Test;

import br.com.casamovel.CasamovelApplicationTests;

public class InscricaoEventoTeste extends CasamovelApplicationTests{
    @Test
    public void deveCadastrarUsuarioExistenteEAindaNaoInscritoEmEventoExistenteERetornar201(){
        // Se inscreve com usuário comum
        given()
            .port(porta)
            .param("username",usuarioAutenticado.getUsername())
            .header("Authorization", usuarioAutenticado.getToken())
            .pathParam("eventoId", 10)
            .log().all()
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
            .log().all()
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
            .log().all()
            .get("/usuario/username/{username}")
        .then()
            .statusCode(equalTo(200)) // OK
            .and()
            .log().body()
            .and()
            .body(
                "eventos", hasSize(1),
                "eventos[0].titulo", equalTo("Evento Spring Boot Test")
            );
            
    }

}