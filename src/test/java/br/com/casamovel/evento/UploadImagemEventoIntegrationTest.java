package br.com.casamovel.evento;

import org.apache.http.HttpStatus;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;

import br.com.casamovel.CasamovelApplicationIntegrationIT;

public class UploadImagemEventoIntegrationTest extends CasamovelApplicationIntegrationIT {
    private final Path root = FileSystems.getDefault().getPath("").toAbsolutePath();
    private final String FILENAME = "eventoImagem.png";
    private Path filePath = Paths.get(root.toString(),
        "src", "test","resources", FILENAME
    );
    private final File file = new File(filePath.toString());
    
    @Test
    @DisplayName("Test if returns 200 OK from file upload")
    public void deveSalvarFotodoEventoERetornar201Created(){

        assertEquals(FILENAME,file.getName());
        assertEquals(true,file.getAbsoluteFile().exists());
        
        given()
            .port(porta)
            .param("username",usuarioAutenticado.getUsername())
            .header("Authorization", usuarioAutenticado.getToken())
            .contentType("multipart/form-data")
            .multiPart("image", file, "image/png")
        .when()
            .post("/evento/upload")
        .then()
            .statusCode(equalTo(HttpStatus.SC_OK))
            .and()
            .body("mensagem",equalTo("Seu arquivo foi salvo"))
            .log().body();

        
        
    }
}