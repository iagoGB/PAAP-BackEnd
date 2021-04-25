package br.com.casamovel.endpoint;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.casamovel.dto.evento.DetalhesEventoDTO;
import br.com.casamovel.dto.evento.NovoEventoDTO;
import br.com.casamovel.dto.evento.RegistroPresencaDTO;
import br.com.casamovel.service.EventService;

@RestController
@RequestMapping("/event")
public class EventEndpoint {
    private final EventService es;
    
    @Autowired
    public EventEndpoint(EventService es) {
        this.es = es;
    }

    @GetMapping("/open")
    public List<DetalhesEventoDTO> findAllOpen() {
        return es.findAllOpen();
    }
    
    @GetMapping
    public ResponseEntity<Page<DetalhesEventoDTO>> findAll(Pageable pagination) {
        return es.findAll(pagination);
    }
    
    @GetMapping("/{id}")
    public DetalhesEventoDTO findById(@PathVariable(value="id") final Long id) {
    	return es.findById(id);
    }

    @PostMapping
    public ResponseEntity<?> save(
    	@RequestBody final NovoEventoDTO event,
    	final UriComponentsBuilder uriBuilder
    ) 
    {
    	return es.save(event);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletaEvento(@PathVariable(value = "id") long id) {
        return es.deletarEvento(id);
    }

    @PutMapping("/{id}/inscricao")
    public ResponseEntity<?> inscreverEmEvento
    (
        @PathVariable(value ="id") Long id, 
        @RequestParam(value ="username") Long userID
    )
    {
        return es.inscreverUsuarioNoEvento(id, userID);
    }

    @PutMapping("/{id}/remover-inscricao")
    public ResponseEntity<?> removerInscricaoEmEvento
    (
        @PathVariable(value ="id") Long id, 
        @RequestParam(value ="username") Long userID
    )
    {
        return es.removerInscricao(id, userID);
    }

    @PutMapping("/{id}/registro-presenca")
    public ResponseEntity<?> registrarPresenca
    (
        @PathVariable(value ="id") Long id, 
        @RequestBody RegistroPresencaDTO registroPresencaDTO
    )
    {
        return es.registrarPresenca(id, registroPresencaDTO);
    }

    @GetMapping("/imagem/{fileName:.+}")
    public ResponseEntity<?> downloadImagemEvento(@PathVariable(value ="fileName") String filename ){
        System.out.println("--------FILENAME-----------");
        System.out.println(filename);
        return es.getImageEvent(filename);
    }

    @PostMapping(value="/{id}/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE,  produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadImagemEvento(
        @PathVariable(value ="id") Long id, 
        @RequestParam("image") MultipartFile image
    ){
        return es.salvarImagemEvento(id,image);
    }
    
    @PostMapping("/certification")
    public ResponseEntity<?> getCertificate(
			@RequestParam(value="eventID") Long eventID,
			@RequestParam(value="userID") Long userID
	) {
		return es.getCertification(eventID,userID);
		
	}
}
