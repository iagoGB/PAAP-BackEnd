package br.com.casamovel.evento;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

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
        
        // Checa com usuario adm
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

    }

}