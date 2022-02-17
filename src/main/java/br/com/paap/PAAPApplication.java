package br.com.paap;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
@RestController
public class PAAPApplication {

	public static void main(String[] args) {

		SpringApplication.run(PAAPApplication.class, args);
		// System.out.println("Senha criptografada: " + new BCryptPasswordEncoder().encode("abc"));
		
	}	
	
	@RequestMapping("/health")
	public String home() {
		return "Hello World! \n Welcome to PAAP API";
	} 
	
}
