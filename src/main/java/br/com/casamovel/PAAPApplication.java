package br.com.casamovel;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
@RestController
public class PAAPApplication {

	public static void main(String[] args) {

		SpringApplication.run(PAAPApplication.class, args);
		System.out.println("Senha criptografada: " + new BCryptPasswordEncoder().encode("abc"));
		
	}	
	
	@RequestMapping("/login")
	public String home() {
		return "Hello World! \n Welcome to CASa Móvel API";
		
	} 
	
}
