/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.casamovel.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import br.com.casamovel.dto.evento.DetalhesEventoDTO;
import br.com.casamovel.dto.evento.NovoEventoDTO;
import br.com.casamovel.dto.evento.RegistroPresencaDTO;
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
import br.com.casamovel.util.QRCodeGenerator;
import net.bytebuddy.utility.RandomString;


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

    @Autowired
    QRCodeGenerator qrCodeGenerator;

    @Autowired
    S3StorageService s3StorageService;
    
    @Value("${events.folder}")
    private String EVENTS_FOLDER;
    @Value("${qrcodes.folder}")
    private String QRCODES_FOLDER;

    private String eventImageDir = "C:\\CasaMovel\\eventos\\";
    
    public List<DetalhesEventoDTO> listarEventos(){
    	List<DetalhesEventoDTO> result = new ArrayList<DetalhesEventoDTO>();
    	//List<Evento> findAll = eventoRepository.findAll();
    	System.out.println("Data do Brasil: "+ LocalDateTime.now(ZoneId.of("America/Sao_Paulo")));
    	List<Evento> findAll = eventoRepository.findAllOpen(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")));
    	findAll.forEach(evento -> result.add(DetalhesEventoDTO.parse(evento)));
        return result;
    }
    
    public ResponseEntity<?> salvarEvento(NovoEventoDTO novoEventoDTO) {
            // TODO - Tratar erro para categoria invalida e nome do palestrante inexistente
            return categoriaRepository.findById(novoEventoDTO.getCategoria())
            .map(findedCategory -> {
                var newEventoModel = new Evento();
                newEventoModel.parse(novoEventoDTO, findedCategory, palestranteRepository);
                newEventoModel = eventoRepository.save(newEventoModel);
                var keyword = RandomString.make() + newEventoModel.getId();
                newEventoModel.setKeyword(keyword);
                var qrCodeURL = this.generateQRCodeEvent(newEventoModel);
                newEventoModel.setQrCode(qrCodeURL);
                newEventoModel = eventoRepository.save(newEventoModel);
                var response = DetalhesEventoDTO.parse(newEventoModel);
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            }).orElse(ResponseEntity.badRequest().body(null));
    }
    
    private String generateQRCodeEvent(Evento newEvent) {
        String resourceURL = null;
        try {
            var file = File.createTempFile( String.format("qrcode_%d", newEvent.getId()), ".png");
            file = qrCodeGenerator.create(newEvent.getKeyword(), 200, 200, file);
            resourceURL = s3StorageService.saveImage(file, newEvent.getId(), QRCODES_FOLDER);
        } catch (Exception e) {
            System.out.println("OCORREU UM ERRO AO TENTAR SALVAR QRCODE: "+e);
        }
        return resourceURL;
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
        Usuario usuario = findUsuarioByEmail(data.username);
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
        System.out.println("Keyword:  "+keyword+", eventoKeyWord: "+ relacao.getEvento_id().getKeyword());
        if (!relacao.getEvento_id().getKeyword().equals(keyword))
            throw new IllegalArgumentException("Código do evento inválido");
    }

    private Usuario findUsuarioByEmail(String username) {
        Optional<Usuario> findById = usuarioRepository.findByEmail(username);
        return findById.orElseThrow( () -> new IllegalArgumentException("Usuário não encontrado"));
    }

    private Evento findEventoById(Long id) {
        var findById = eventoRepository.findById(id);
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

	public ResponseEntity<?> salvarImagemEvento(Long id, MultipartFile image) {
        
        var saveImagePath = disco.salvarImagem(image, eventImageDir);
        var eventoFound = findEventoById(id);
        eventoFound.setFoto(saveImagePath);
        eventoRepository.save(eventoFound);
        return ResponseEntity.ok().build();
    }
    
    public ResponseEntity<?> getImageEvent(String filename){
        Path path = Paths.get(eventImageDir + filename);
	    // Resource resource = null;
        try {
            var resource = new FileSystemResource(path);
            System.out.println("Path: "+ path.toString());
            var bytes = StreamUtils.copyToByteArray(resource.getInputStream());
            return ResponseEntity.ok()
            .contentType(MediaType.IMAGE_JPEG) // "application/octet-stream"
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
            .body(bytes);
        } 
        catch(FileNotFoundException ex){
            System.out.println("Arquivo não encontrado!!!!!!");
            return ResponseEntity.notFound().build();
        } 
        catch (Exception e) {
            System.out.println("Outro erro qualquer!!!!!!");
            return ResponseEntity.status(500).build();
        }
        
    }
}
