package br.com.casamovel.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.web.multipart.MultipartFile;

public class Disco {
    
     public String salvarImagemUsuario(MultipartFile imageFile) throws Exception {
            String folder = "/CASaMovel/usuarioAvatar";
            try{
                byte[] bytes = imageFile.getBytes();
                Path path = Paths.get(folder, imageFile.getOriginalFilename());
                Files.write(path, bytes);
                return (folder+imageFile.getOriginalFilename());
            } catch (Exception e){
                System.out.println("Erro ao tentar salvar arquivo" + e);
                return null;
            }
        }
}
