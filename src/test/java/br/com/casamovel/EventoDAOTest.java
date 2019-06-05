package br.com.casamovel;

import static org.junit.Assert.*;

import java.sql.Date;


import org.junit.Ignore;
import org.junit.Test;

import br.com.casamovel.model.Evento;
import br.com.casamovel.modelDAO.EventoDAO;
@Ignore
public class EventoDAOTest {
	
	@Test
	public void InsertTest() {
		Date date = new Date(20150203);
		Evento evento;
		evento = new Evento (
				3,
				"Por favor funciona",
				date,
				"Funcionou?",
				4,
				date,
				date	
		);
		
		EventoDAO edao = new EventoDAO();
		assertTrue(edao.saveEvento(evento));
		
	}
	
	@Test
	public void DeleteTest() {
		EventoDAO edao = new EventoDAO();
		assertEquals(true,edao.deleteEvento(15));
	}
	
	
	

}
