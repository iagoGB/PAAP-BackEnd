//package br.com.casamovel;
//
//import static org.junit.Assert.*;
//
//import java.sql.Date;
//
//
//import org.junit.Ignore;
//import org.junit.Test;
//
//import br.com.casamovel.model.Evento;
////import br.com.casamovel.modelDAO.EventoEndpoint;
//@Ignore
//public class EventoDAOTest {
//	
//	@Test
//	public void InsertTest() {
//		Date date = new Date(20150203);
//		Evento evento;
//		evento = new Evento (
//				3,
//				"Por favor funciona",
//				date,
//				"Funcionou?",
//				4,
//				date,
//				date	
//		);
//		
//		EventoEndpoint edao = new EventoEndpoint();
//		assertTrue(edao.saveEvento(evento));
//		
//	}
//	
//	@Test
//	public void DeleteTest() {
//		EventoEndpoint edao = new EventoEndpoint();
//		assertEquals(true,edao.deleteEvento(15));
//	}
//	
//	
//	
//
//}
