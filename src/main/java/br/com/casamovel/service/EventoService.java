/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.casamovel.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import br.com.casamovel.dto.evento.DetalhesEventoDTO;
import br.com.casamovel.dto.evento.NovoEventoDTO;
import br.com.casamovel.dto.evento.RegistroPresencaDTO;
import br.com.casamovel.endpoint.EventoEndpoint;
import br.com.casamovel.model.Categoria;
import br.com.casamovel.model.Evento;
import br.com.casamovel.model.EventoUsuario;
import br.com.casamovel.model.EventoUsuarioID;
import br.com.casamovel.model.Usuario;
import br.com.casamovel.repository.CategoriaRepository;
import br.com.casamovel.repository.EventoRepository;
import br.com.casamovel.repository.EventoUsuarioRepository;
import br.com.casamovel.repository.PalestranteRepository;
import br.com.casamovel.repository.UsuarioRepository;
import br.com.casamovel.util.Disco;


/**
 *
 * @author iago.barreto
 */
@Service
public class EventoService {
    
    private static final Disco disco = new Disco();

    @Autowired
    EventoRepository eventoRepository;

    @Autowired
    CategoriaRepository categoriaRepository;
    
    @Autowired
    PalestranteRepository palestranteRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    EventoUsuarioRepository eventoUsuarioRepository;

    @Autowired
    ObjectMapper objectMapper;
    
    public List<DetalhesEventoDTO> listarEventos(){
    	List<DetalhesEventoDTO> result = new ArrayList<DetalhesEventoDTO>();
    	//List<Evento> findAll = eventoRepository.findAll();
    	System.out.println("Data do Brasil: "+ LocalDateTime.now(ZoneId.of("America/Sao_Paulo")));
    	List<Evento> findAll = eventoRepository.findAllOpen(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")));
    	findAll.forEach(evento -> result.add(DetalhesEventoDTO.parse(evento)));
        return result;
    }
    
    public Evento salvarEvento(NovoEventoDTO novoEventoDTO) {
    	Evento result = null;
        try 
        {
            Categoria c = null;
            Optional<Categoria> optC;
            System.out.println("categoria Rep: "+ categoriaRepository.findById(novoEventoDTO.getCategoria()).toString());
            optC = categoriaRepository.findById(novoEventoDTO.getCategoria());
            if (optC.isPresent()){
                c = optC.get();
            }
                       
            Evento novoEventoModel = new Evento();
            novoEventoModel.parse(novoEventoDTO, c, palestranteRepository);
            result = eventoRepository.save(novoEventoModel);
            
        } catch (Exception e) 
        {
            Logger.getLogger(EventoEndpoint.class.getName()).log(Level.SEVERE, null, e);
            System.out.println("Erro ao criar evento, triste kk: "+ e);
        }
        return result;
    }
    
    public boolean deletarEvento(long id) {
        boolean deletou = false;
        try {
            this.eventoRepository.deleteById(id);
            deletou = true;
        } catch (Exception e) {
            System.out.println("Erro ao deletar"+ e);
        }
        return deletou;
    }

	public DetalhesEventoDTO findById(Long id) {
		Optional<Evento> result = eventoRepository.findById(id);
		if (result.isPresent()) {
			Evento evento = result.get();
			return DetalhesEventoDTO.parse(evento);
		}
		return null;
	}

	public ResponseEntity<?> inscreverUsuarioNoEvento(Long eventoId, String usermail) {
        Optional<Evento> resultEvento = eventoRepository.findById(eventoId);
        Optional<Usuario> resultUsuario = usuarioRepository.findByEmail(usermail);
        Evento evento = resultEvento.get();
        Usuario usuario = resultUsuario.get();
        Optional<EventoUsuario> findById = eventoUsuarioRepository.findById(new EventoUsuarioID(eventoId, usuario.getId()));
        if (findById.isPresent()) {
            return  ResponseEntity
            .badRequest()
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .body("{\"mensagem\":\"Usuário já esta inscrito\"}");
        } else {
            EventoUsuario relacaoEventoUsuario = new EventoUsuario(
                evento, 
                usuario,
                true, 
                false
            );
            EventoUsuario save = eventoUsuarioRepository.save(relacaoEventoUsuario);
            if (save == null) {
                return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
            }
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
	}

	public ResponseEntity<?> removerInscricaoEmEvento(Long eventoId, String usermail) {
        Optional<Usuario> resultUsuario = usuarioRepository.findByEmail(usermail);
        Usuario usuario = resultUsuario.get();
        Optional<EventoUsuario> findById = eventoUsuarioRepository.findById(new EventoUsuarioID(eventoId, usuario.getId()));
        if (!findById.isPresent()){
            return  ResponseEntity
            .badRequest()
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .body("{\"mensagem\":\"O usuário não possui vínculo com evento\"}");
        } else {
            eventoUsuarioRepository.deleteById(new EventoUsuarioID(eventoId, usuario.getId()));
            return ResponseEntity.status(HttpStatus.OK).build();
        }
	}

    @Transactional
	public ResponseEntity<?> registrarPresenca(Long eventoId, RegistroPresencaDTO data) {
        EventoUsuario eventoUsuario = null;
        Usuario usuario = findUsuarioById(data.username);
        // Checa se usuário se inscreveu
        Optional<EventoUsuario> relacao = eventoUsuarioRepository.findById( new EventoUsuarioID(eventoId, usuario.getId()) );
        // Se existe a relação:
        if (relacao.isPresent()){
            //Usuário está iscrito
            eventoUsuario = relacao.get();
        } else {
            // tratamento para usuário que quer registrar presença mas não está inscrito no evento
            // Se a relação nao existe, é necessário checkar se o evento existe, se o usuário esta inscrito
            return  ResponseEntity
            .badRequest()
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .body("{\"mensagem\":\"Você não se inscreveu para o evento\"}");
        }
        // Checa se a data do evento é hoje
        isToday(eventoId);
        checaCodigoEvento(eventoUsuario,data.getKeyword());
        // Se a presença do usuário no evento já foi inserida
        if ( relacao.get().isPresent() ) {
            throw new IllegalArgumentException("Sua presença já foi registrada anteriormente");
        } else {
            relacao.get().setPresent(true);
            var value = new String();
            value = relacao.get().getEvento_id().getCargaHoraria().toString();
            var cargaHoraria =  Long.parseLong(value);
            usuario.setCargaHoraria( usuario.getCargaHoraria().plusHours(cargaHoraria));
        }
		return ResponseEntity.status(HttpStatus.OK).build();
    }
    
    private void checaCodigoEvento(EventoUsuario relacao,String keyword) {
        if (!relacao.getEvento_id().getEventKeyword().equals(keyword))
            throw new IllegalArgumentException("Código do evento inválido");
    }

    private Usuario findUsuarioById(String username) {
        Optional<Usuario> findById = usuarioRepository.findByEmail(username);
        return findById.orElseThrow( () -> new IllegalArgumentException("Usuário não encontrado"));
    }
    // Checa se o evento ocorre hoje
    private void isToday(Long eventoId){
        Optional<Evento> findById = eventoRepository.findById(eventoId);
        Evento evento = findById.get();
        if ( LocalDateTime.now().isBefore(evento.getDataHorario())){
            throw new IllegalArgumentException("O Evento ainda não está na data");
        }
    }

	public ResponseEntity<?> salvarImagemEvento(MultipartFile image) {
        String eventImageDir = "C:\\CasaMovel\\eventos\\";
        String imagemSalva = disco.salvarImagem(image, eventImageDir);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body("{\"mensagem\":\""+imagemSalva+"\"}");
	}
}
