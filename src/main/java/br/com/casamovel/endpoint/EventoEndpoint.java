package br.com.casamovel.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.casamovel.dto.evento.DetalhesEventoDTO;
import br.com.casamovel.dto.evento.NovoEventoDTO;
import br.com.casamovel.model.Evento;
import br.com.casamovel.service.EventoService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/evento")
public class EventoEndpoint {
    @Autowired
    EventoService es;

    @GetMapping
    public List<Evento> getAll() {
    	System.out.println("Passou por aqui");
        return es.listarEventos();
    }
    
    @GetMapping("/{id}")
    public DetalhesEventoDTO findById(@PathVariable(value="id") Long id) {
    	System.out.println("Passou com id");
    	return es.findById(id);
    }

    @PostMapping
    public ResponseEntity<String> salvaEvento(@RequestBody NovoEventoDTO evento) {
        if (es.salvarEvento(evento)) {
            return ResponseEntity.status(HttpStatus.OK).body("Evento salvo!");
        } else {
            return ResponseEntity.badRequest().body("Erro ao criar evento");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletaEvento(@PathVariable(value = "id") long id) {
        if (es.deletarEvento(id)) {
            return ResponseEntity.status(HttpStatus.OK).body("Evento excluido!");
        } else {
            return ResponseEntity.badRequest().body("Erro ao excluir evento");
        }
    }
}
