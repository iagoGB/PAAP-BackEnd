package br.com.paap.util;

import com.google.zxing.BarcodeFormat;
//import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import org.springframework.stereotype.Component;
import java.io.File;
// import java.io.IOException;
// import java.nio.file.FileSystems;
// import java.nio.file.Path;

@Component
public class QRCodeGenerator {
    public  File create(String text, int width, int height, File file) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", file.toPath());
        } catch (Exception e) {
            System.out.println("Ocorreu um erro ao tentar escrever no arquivo temporário");
        }
        return file;
        
    }
}
