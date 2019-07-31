package br.com.casamovel;



import org.apache.catalina.valves.CrawlerSessionManagerValve;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import br.com.casamovel.repositories.*;
import br.com.casamovel.models.*;


@SpringBootApplication
public class CasaMovelApplication {
	@Autowired
	public CategoriaRepository cr;
	
	public static void main(String[] args) {		
		SpringApplication.run(CasaMovelApplication.class, args);			
	}	
}
