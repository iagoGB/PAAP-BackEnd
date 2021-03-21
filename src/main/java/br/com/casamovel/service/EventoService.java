/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.casamovel.service;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
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

import com.amazonaws.util.IOUtils;

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

    private final EventoRepository eventoRepository;

    private final CategoriaRepository categoriaRepository;
    
    private final PalestranteRepository palestranteRepository;

    private final UsuarioRepository usuarioRepository;

    private final EventoUsuarioRepository eventoUsuarioRepository;

    private final QRCodeGenerator qrCodeGenerator;

    private final S3StorageService s3StorageService;

    private  final String EVENTS_FOLDER;

    private  final String QRCODES_FOLDER;

    private final String  eventImageDir = "C:\\CasaMovel\\eventos\\";

    @Autowired
    public EventoService(EventoRepository eventoRepository, CategoriaRepository categoriaRepository, PalestranteRepository palestranteRepository, UsuarioRepository usuarioRepository, EventoUsuarioRepository eventoUsuarioRepository, QRCodeGenerator qrCodeGenerator, S3StorageService s3StorageService, @Value("${events.folder}")String events_folder,@Value("${qrcodes.folder}") String qrcodes_folder) {
        this.eventoRepository = eventoRepository;
        this.categoriaRepository = categoriaRepository;
        this.palestranteRepository = palestranteRepository;
        this.usuarioRepository = usuarioRepository;
        this.eventoUsuarioRepository = eventoUsuarioRepository;
        this.qrCodeGenerator = qrCodeGenerator;
        this.s3StorageService = s3StorageService;
        this.EVENTS_FOLDER = events_folder;
        this.QRCODES_FOLDER = qrcodes_folder;
    }


    public List<DetalhesEventoDTO> listarEventos(){
    	System.out.println("Data do Brasil: "+ LocalDateTime.now(ZoneId.of("America/Sao_Paulo")));

    	return eventoRepository.findAllOpen(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")))
            .stream().map(DetalhesEventoDTO::parse).collect(Collectors.toList());
    	
    }

    @Transactional
    public ResponseEntity<?> salvarEvento(NovoEventoDTO novoEventoDTO) {
            // TODO - Tratar erro para categoria invalida e nome do palestrante inexistente
            return categoriaRepository.findById(novoEventoDTO.getCategoria())
            .map(categoria -> Evento.parseFrom(novoEventoDTO, categoria, palestranteRepository))
            .map(evento -> {
                evento = eventoRepository.save(evento);
                evento.setKeyword(this.createKeyword(evento));
                var qrCodeURL = this.generateQRCodeEvent(evento);
                evento.setQrCode(qrCodeURL);
                evento = eventoRepository.save(evento);
                var response = DetalhesEventoDTO.parse(evento);
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            })
            .orElseThrow(() -> new RuntimeException(String.format("Categoria com ID %s não existe", novoEventoDTO.getCategoria())));
    }

    private String createKeyword(Evento evento){
        return  String.format("%s_%s", RandomString.make(),evento.getId());
    }
    
    private String generateQRCodeEvent(Evento newEvent) {
        String resourceURL = null;
        try {
            var file = File.createTempFile( String.format("qrcode_%d", newEvent.getId()), ".png");
            file = qrCodeGenerator.create(newEvent.getKeyword(), 200, 200, file);
            resourceURL = s3StorageService.saveImage(file, newEvent.getId(), QRCODES_FOLDER);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar QRCode: " + e.getMessage());
        }
        return resourceURL;
    }

    public ResponseEntity<?> deletarEvento(long id) {
        return this.eventoRepository.findById(id)
                .map( evento -> {
                    this.eventoRepository.deleteById(id);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new RuntimeException(String.format("Evento com ID %s não existe", id)));
        
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
        var resultEvento = eventoRepository.findById(eventoId);
        var resultUsuario = usuarioRepository.findByEmail(usermail);
        var evento = resultEvento.get();
        var usuario = resultUsuario.get();
        var findById = eventoUsuarioRepository.findById(new EventoUsuarioID(eventoId, usuario.getId()));
        if (findById.isPresent()) {
            return  ResponseEntity
            .badRequest()
            .contentType(MediaType.APPLICATION_JSON)
            .body("{\"mensagem\":\"Usuário já esta inscrito\"}");
        } else {
            EventoUsuario relacaoEventoUsuario = new EventoUsuario(
                evento, 
                usuario,
                null,
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
        var resultUsuario = usuarioRepository.findByEmail(usermail);
        var usuario = resultUsuario.get();
        var findById = eventoUsuarioRepository.findById(new EventoUsuarioID(eventoId, usuario.getId()));
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
            .contentType(MediaType.APPLICATION_JSON)
            .body("{\"mensagem\":\"Usuário  não inscrito para o evento\"}");
        }
        // Checa se a data do evento é hoje
        this.isToday(eventoId);
        this.checaCodigoEvento(eventoUsuario,data.getKeyword());
        // Se a presença do usuário no evento já foi inserida
        if ( relacao.get().isPresent() ) {
            throw new IllegalArgumentException("Sua presença já foi registrada anteriormente");
        } else {
            relacao.get().setPresent(true);
            var cargaHoraria = relacao.get().getEventoID().getCargaHoraria();
            usuario.setCargaHoraria( usuario.getCargaHoraria() + cargaHoraria);
            this.generateCertificate(relacao.get());
        }
		return ResponseEntity.status(HttpStatus.OK).build();
    }

    private Optional<EventoUsuario> findRelation(Long eventoID, Long userID){
        return this.eventoUsuarioRepository.findById( new EventoUsuarioID(eventoID, userID));
    }

    public ResponseEntity<?> getCertification(Long eventID, Long userID){
        return this.findRelation(eventID,userID)
                .map((relation) -> {
                	var participated = relation.isPresent();
                	if (!participated) {
                		throw new RuntimeException(String.format("%s não possui registro de presença no evento %s", relation.getUsuarioID().getNome(), relation.getEventoID().getTitulo()));
                	}
                    if (relation.getCertificate() == null) {
                        relation.setCertificate(generateCertificate(relation));
                    }
                    return ResponseEntity.ok()
    	                    .contentType(MediaType.APPLICATION_PDF) // "application/octet-stream"
    	                    .header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=\"%s_%s.pdf\"", relation.getEventoID().getTitulo(), relation.getUsuarioID().getNome()))
    	                    .body(getDocumentStream(relation.getCertificate()));
                })
                .orElseThrow(() -> new RuntimeException("Cadastro em evento inexistente"));
    }


	private byte[] getDocumentStream(String urlPath) {
		byte[] bytes = null;
		try {
		    var url = new URL(urlPath);
		    bytes = StreamUtils.copyToByteArray(url.openStream()); 
		} catch (Exception e) {
			throw new RuntimeException("Erro ao fazer download do arquivo");
		}
		return bytes;
	}

    public String generateCertificate(EventoUsuario relacao){
        try {
        	var user = relacao.getUsuarioID();
        	var event = relacao.getEventoID();
            var resource = s3StorageService.getResource("certificados", "teste_template.jrxml");
            var template = JRXmlLoader.load(resource);
            var relatorio = JasperCompileManager.compileReport( template );
            var parameter  = new HashMap<String, Object>();
			var workload = event.getCargaHoraria();
			parameter.put("name", user.getNome());
            parameter.put("event", event.getTitulo());
            parameter.put("workload", workload.toString() +" h");
            parameter.put("data", event.getDataHorario().toString());
            var jasperPrint = JasperFillManager.fillReport(relatorio, parameter, new JREmptyDataSource());
            var file = File.createTempFile(String.valueOf("user_"+user.getId()),".pdf");
            JasperExportManager.exportReportToPdfFile(jasperPrint, file.getAbsolutePath());
            var saveCertificate = s3StorageService.saveCertificate(file, relacao);
            file.deleteOnExit();
            return saveCertificate;
        } catch (JRException | FileNotFoundException  e) {
            e.printStackTrace();
            throw new RuntimeException("Deu um erro ai ó");
        } catch ( Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro inexperado");
        }
    }

    public void drawStringMultiLine(Graphics2D g, String text, int lineWidth, int x, int y) {
        FontMetrics m = g.getFontMetrics();
        if(m.stringWidth(text) < lineWidth) {
            g.drawString(text, x, y);
        } else {
            String[] words = text.split(" ");
            String currentLine = words[0];
            for(int i = 1; i < words.length; i++) {
                if(m.stringWidth(currentLine+words[i]) < lineWidth) {
                    currentLine += " "+words[i];
                } else {
                    g.drawString(currentLine, x, y);
                    y += m.getHeight();
                    currentLine = words[i];
                }
            }
            if(currentLine.trim().length() > 0) {
                g.drawString(currentLine, x, y);
            }
        }
    }

    private void checaCodigoEvento(EventoUsuario relacao,String keyword) {
        if (!relacao.getEventoID().getKeyword().equals(keyword))
            throw new IllegalArgumentException("Código do evento inválido");
    }

    private Usuario findUsuarioByEmail(String username) {
        var findById = usuarioRepository.findByEmail(username);
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
