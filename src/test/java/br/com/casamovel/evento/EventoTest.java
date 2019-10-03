package br.com.casamovel.evento;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.casamovel.model.Evento;
import br.com.casamovel.repository.EventoRepository;

public class EventoTest {
	
	@Autowired
	public EventoRepository er;

	@Test
	public void InsereEventoTeste() {
		//Categoria c = new Categoria();
		Evento e = new Evento();
		e.setTitulo("titulo do evento");
		//Evento resposta = er.save(e);
		assertEquals("a", Evento.class);
	}

}
