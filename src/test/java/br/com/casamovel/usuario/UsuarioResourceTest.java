package br.com.casamovel.usuario;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import br.com.casamovel.CasamovelApplicationTests;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@Sql(value="../resources/load-data.sql",executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class UsuarioResourceTest extends CasamovelApplicationTests {

    private final String URI = "/login";

    @Before
    public void setUp(){

    }

    @Test
    public void deveRetornarRecursosDeAutenticacao() throws Exception {
        given()
            .port(porta)
            .body("{ \"username\":\"admin\", \"senha\":\"abc\"}")
        .get(URI)
        .then()
            .log().body().and()
            .statusCode(HttpStatus.OK.value())
            .and()
            .body(
                "role", equalTo("ADMIN"),
                "username", equalTo("admin")
        );
    }
}