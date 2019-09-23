package br.com.casamovel.endpoints;

import br.com.casamovel.models.Categoria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.casamovel.models.Evento;
import br.com.casamovel.repositories.CategoriaRepository;
import br.com.casamovel.repositories.EventoRepository;
import java.sql.Time;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
public class EventoEndpoint {
	@Autowired
	EventoRepository eventoRepository;
        
        @Autowired
        CategoriaRepository categoriaRepository;
	
	@GetMapping("/evento")
	public List<Evento> listaEvento(){
		return eventoRepository.findAll();
	}
	@PostMapping("/evento")
	public ResponseEntity<String> salvaEvento(@RequestBody Evento evento){
            try {
            Long l =  new Long(1);
                Categoria c = categoriaRepository.getOne(l);
                
                Evento e = new Evento();
                
                e.setFoto("Caminho da foto aqui");
                e.setTitulo(evento.getTitulo());
                e.setCategoria(c);
                e.setFoto(evento.getFoto());
                e.setTitulo(evento.getTitulo());
                e.setCarga_horaria(e.getCarga_horaria());
                e.setLocal(evento.getLocal());
                e.setData(evento.getData());
                c.getEventos().add(e);
                System.out.println(" CCCCCCCCCC: "+ c.toString());
                Evento novoEvento = eventoRepository.saveAndFlush(e);
                return ResponseEntity.status(HttpStatus.OK).body(novoEvento.toString());
                
            } catch(Exception e ){
                Logger.getLogger(EventoEndpoint.class.getName()).log(Level.SEVERE, null, e);
                System.out.println("Erro ao criar evento"); 
                return ResponseEntity.badRequest().body("Erro ao criar evento");
            }
            
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
