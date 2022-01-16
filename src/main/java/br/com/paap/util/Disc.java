package br.com.paap.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

public class Disc {
    
     public String salvarImagem(MultipartFile imageFile, String directory)  {
        String fileName = StringUtils.cleanPath(imageFile.getOriginalFilename());
        Path basePath = Paths.get(directory);
        Path targetPath = Paths.get(directory+fileName);
            // String folder = "/CASaMovel/usuarioAvatar";
        try{
            if (!directoryExists(basePath)) 
                Files.createDirectories(basePath);  
            Files.copy(imageFile.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
                
        } catch (Exception e){
            e.printStackTrace();
            return "Erro ao tentar salvar arquivo -" + e;
        }
       
    }

    public String salvarImagem(MultipartFile imageFile, Long resourceID, String directory){
        String fileName = resourceID + ".png";
        Path basePath = Paths.get(directory);
        Path targetPath = Paths.get(directory+fileName);
            // String folder = "/CASaMovel/usuarioAvatar";
        try{
            if (!directoryExists(basePath)) 
                Files.createDirectories(basePath);  
            Files.copy(imageFile.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
                
        } catch (Exception e){
            e.printStackTrace();
            return "Erro ao tentar salvar arquivo -" + e;
        }
    }

    private boolean directoryExists(Path basePath) {
        return Files.exists(basePath);
    }
}
