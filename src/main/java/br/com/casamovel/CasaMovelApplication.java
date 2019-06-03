package br.com.casamovel;

import java.util.ArrayList;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.casamovel.model.Evento;
import br.com.casamovel.modelDAO.EventoDAO;

@SpringBootApplication
public class CasaMovelApplication {

	public static void main(String[] args) {
		SpringApplication.run(CasaMovelApplication.class, args);
		
		/*EventoDAO dao = new EventoDAO();
		ArrayList<Evento> lista = dao.getEvento();
		
		for  (Evento a: lista) {
			System.out.println(a.getId());
			System.out.println(a.getTitulo());
			System.out.println(a.getCargaHoraria());
			System.out.println("----------------");
		} */
	}

}
