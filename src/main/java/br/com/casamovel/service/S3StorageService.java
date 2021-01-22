package br.com.casamovel.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;

import br.com.casamovel.model.EventoUsuario;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class S3StorageService  {
    @Value("${aws.s3.accesskey}")
    private String ACCESSKEY;
    @Value("${aws.s3.secretkey}")
    private String SECRETKEY;
    @Value("${aws.s3.region}")
    private String REGION;
    @Value("${aws.s3.bucket}")
    private String BUCKET;


    public String saveImage(MultipartFile imageFile, Long resourceID, String directory) {
        String resourceUrl = null;
        try {
            AWSCredentials credentials = new BasicAWSCredentials(
                this.ACCESSKEY,
                this.SECRETKEY
            );
            AmazonS3Client s3client = (AmazonS3Client) AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(this.REGION)
                .build();
            
            List<Bucket> buckets = s3client.listBuckets();
            var bucketName = buckets.get(0).getName();
            var metadata = new ObjectMetadata();
            var fileInputStream = imageFile.getInputStream();
            var targetPath = directory+"/"+resourceID+"/"+imageFile.getOriginalFilename();
            metadata.setContentType(imageFile.getContentType());
            metadata.setContentLength(imageFile.getSize());
            var por = new PutObjectRequest( 
                bucketName, 
                targetPath,
                fileInputStream,
                metadata
            );
            s3client.putObject(por.withCannedAcl(CannedAccessControlList.PublicRead));
            resourceUrl = s3client.getResourceUrl(bucketName,targetPath);

        } catch (Exception e) {
            System.err.println(e);
        }
        return resourceUrl;
    }

    public String saveImage(File imageFile, Long resourceID, String directory) {
        String resourceUrl = null;
        System.out.println(this.REGION);
        try {
            AWSCredentials credentials = new BasicAWSCredentials(
                this.ACCESSKEY,
                this.SECRETKEY
            );
            AmazonS3Client s3client = (AmazonS3Client) AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(this.REGION)
                .build();
            var bucketName = this.BUCKET;
            var targetPath = directory+"/"+resourceID+"/"+imageFile.getName();
            var por = new PutObjectRequest( 
                bucketName, 
                targetPath,
                imageFile
            );  
            s3client.putObject(por.withCannedAcl(CannedAccessControlList.PublicRead));
            resourceUrl = s3client.getResourceUrl(bucketName,targetPath);
            System.out.println("Url do recurso: "+resourceUrl);

        } catch (Exception e) {
            System.err.println(e);
            throw new RuntimeException("Erro ao salvar imagem no servidor");
        }
        return resourceUrl;
    }
    
    public String saveCertificate(File imageFile, EventoUsuario eu) {
        String resourceUrl = null;
        System.out.println(this.REGION);
        try {
            AWSCredentials credentials = new BasicAWSCredentials(
                this.ACCESSKEY,
                this.SECRETKEY
            );
            AmazonS3Client s3client = (AmazonS3Client) AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(this.REGION)
                .build();
            var bucketName = this.BUCKET;
            var targetPath = "certificados"+"/"+eu.getEvento_id().getId()+"/"+eu.getUsuario_id().getId();
            var por = new PutObjectRequest( 
                bucketName, 
                targetPath,
                imageFile
            );  
            s3client.putObject(por.withCannedAcl(CannedAccessControlList.PublicRead));
            resourceUrl = s3client.getResourceUrl(bucketName,targetPath);
            System.out.println("Url do recurso: "+resourceUrl);

        } catch (Exception e) {
            System.err.println(e);
            throw new RuntimeException("Erro ao salvar imagem no servidor");
        }
        return resourceUrl;
    }


    public ResponseEntity<?> getResource(Long portfolioID, String filename, String directory) {
        Path targetPath = Paths.get(directory + "\\" + portfolioID + "\\" + filename);
        // Resource resource = null;
        try {
            var resource = new FileSystemResource(targetPath);
            System.out.println("Path: " + targetPath.toString());
            var bytes = StreamUtils.copyToByteArray(resource.getInputStream());
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG) // "application/octet-stream"
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(bytes);
        } catch (FileNotFoundException ex) {
            System.out.println("Arquivo não encontrado!!!!!!");
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.out.println("Outro erro qualquer!!!!!!");
            return ResponseEntity.status(500).build();
        }
    }

    public ByteArrayInputStream getResource(String directory, String filename) {

        AWSCredentials credentials = new BasicAWSCredentials(
                this.ACCESSKEY,
                this.SECRETKEY
        );
        AmazonS3Client s3client = (AmazonS3Client) AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(this.REGION)
                .build();
        var key = String.format("%s/%s",directory,filename);
        System.out.println("KEY : "+ key);
        var s3object = s3client.getObject(BUCKET, key);
        try {
            return new ByteArrayInputStream(s3object.getObjectContent().readAllBytes());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao recuperar arquivos do certificado no repositório");
        }
    }
}
