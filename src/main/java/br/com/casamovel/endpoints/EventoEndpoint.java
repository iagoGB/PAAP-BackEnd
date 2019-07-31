package br.com.casamovel.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.casamovel.models.Evento;
import br.com.casamovel.repositories.EventoRepository;

import java.util.List;

@RestController
public class EventoEndpoint {
	@Autowired
	EventoRepository eventoRepository;
	
	@GetMapping("/evento")
	public List<Evento> listaEvento(){
		return eventoRepository.findAll();
	}
	@PostMapping("/evento")
	public Evento salvaEvento(@RequestBody Evento evento){
		return eventoRepository.save(evento);
	}
	@DeleteMapping("/evento/{id}")
	public boolean deletaEvento(@PathVariable(value="id") long id) {
		boolean deletou = false;
		try {
			this.eventoRepository.deleteById(id);
			deletou = true;
		} catch (Exception e) {
			System.out.println("Erro ao deletar");
		} 
		return deletou;
	}
	
	
}
