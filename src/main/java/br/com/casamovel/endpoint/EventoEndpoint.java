package br.com.casamovel.endpoint;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import br.com.casamovel.model.Evento;
import br.com.casamovel.service.EventoService;

@RestController
@RequestMapping("/evento")
public class EventoEndpoint {
    @Autowired
    EventoService es;

    @GetMapping
    public List<DetalhesEventoDTO> getAll() {
        return es.listarEventos();
    }
    
    @GetMapping("/{id}")
    public DetalhesEventoDTO findById(@PathVariable(value="id") final Long id) {
    	return es.findById(id);
    }

    @PostMapping
    public ResponseEntity<DetalhesEventoDTO> salvaEvento(
    	@RequestBody final NovoEventoDTO evento,
    	final UriComponentsBuilder uriBuilder
    ) 
    {
    	final Evento salvarEvento = es.salvarEvento(evento);
        if (salvarEvento != null ) {
        	final DetalhesEventoDTO parse = DetalhesEventoDTO.parse(salvarEvento);
            return ResponseEntity.status(HttpStatus.CREATED).body(parse);
        } else {
            return ResponseEntity.badRequest().body(null);
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

    @PutMapping("/{id}/inscricao")
    public ResponseEntity<?> inscreverEmEvento
    (
        @PathVariable(value ="id") Long id, 
        @RequestParam(value ="username") String usermail
    )
    {
        return es.inscreverUsuarioNoEvento(id, usermail);
    }

    @PutMapping("/{id}/remover-inscricao")
    public ResponseEntity<?> removerInscricaoEmEvento
    (
        @PathVariable(value ="id") Long id, 
        @RequestParam(value ="username") String usermail
    )
    {
        return es.removerInscricaoEmEvento(id, usermail);
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
}
