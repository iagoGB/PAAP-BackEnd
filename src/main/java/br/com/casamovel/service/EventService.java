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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import br.com.casamovel.dto.usuario.UserDTO;
import br.com.casamovel.model.Event;
import br.com.casamovel.model.EventUser;
import br.com.casamovel.model.EventUserID;
import br.com.casamovel.model.User;
import br.com.casamovel.repository.CategoryRepository;
import br.com.casamovel.repository.EventRepository;
import br.com.casamovel.repository.EventUserRepository;
import br.com.casamovel.repository.UserRepository;
import br.com.casamovel.util.QRCodeGenerator;
import net.bytebuddy.utility.RandomString;


/**
 *
 * @author iago.barreto
 */
@Service
public class EventService {
    
    private final EventRepository eventRepository;

    private final CategoryRepository categoryRepository;

    private final UserRepository userRepository;

    private final EventUserRepository eventUserRepository;

    private final QRCodeGenerator qrCodeGenerator;

    private final S3StorageService s3StorageService;

    private  final String EVENTS_FOLDER;

    private  final String QRCODES_FOLDER;

    private final String  eventImageDir = "C:\\CasaMovel\\eventos\\";

    @Autowired
    public EventService(EventRepository eventoRepository, CategoryRepository categoriaRepository, UserRepository usuarioRepository, EventUserRepository eventoUsuarioRepository, QRCodeGenerator qrCodeGenerator, S3StorageService s3StorageService, @Value("${events.folder}")String eventsFolder,@Value("${qrcodes.folder}") String qrcodesFolder) {
        this.eventRepository = eventoRepository;
        this.categoryRepository = categoriaRepository;
        this.userRepository = usuarioRepository;
        this.eventUserRepository = eventoUsuarioRepository;
        this.qrCodeGenerator = qrCodeGenerator;
        this.s3StorageService = s3StorageService;
        this.EVENTS_FOLDER = eventsFolder;
        this.QRCODES_FOLDER = qrcodesFolder;
    }


    public List<DetalhesEventoDTO> findAllOpen(){
    	// System.out.println("Data do Brasil: "+ LocalDateTime.now(ZoneId.of("America/Sao_Paulo")));
    	return eventRepository.findAllOpen(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")))
            .stream().map(DetalhesEventoDTO::parse).collect(Collectors.toList());
    }

    @Transactional
    public ResponseEntity<?> save(NovoEventoDTO novoEventoDTO) {
            // TODO - Tratar erro para categoria invalida e nome do palestrante inexistente
            return categoryRepository.findById(novoEventoDTO.getCategory())
            .map(categoria -> Event.parseFrom(novoEventoDTO, categoria))
            .map(evento -> {
                evento = eventRepository.save(evento);
                evento.setKeyword(this.createKeyword(evento));
                var qrCodeURL = this.generateQRCodeEvent(evento);
                evento.setQrCode(qrCodeURL);
                evento = eventRepository.save(evento);
                var response = DetalhesEventoDTO.parse(evento);
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            })
            .orElseThrow(() -> new RuntimeException(String.format("Categoria com ID %s não existe", novoEventoDTO.getCategory())));
    }

    private String createKeyword(Event evento){
        return  String.format("%s_%s", RandomString.make(),evento.getId());
    }
    
    private String generateQRCodeEvent(Event newEvent) {
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
    
    private String generateImageEvent(Event event, MultipartFile image) {
    	String resourceURL = null;
        try {
            var file = File.createTempFile( String.format("event_%d", event.getId()), ".png");
            image.transferTo(file);
            resourceURL = s3StorageService.saveImage(file, event.getId(), EVENTS_FOLDER);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar link da imagem do evento: " + e.getMessage());
        }
        return resourceURL;
    }

    public ResponseEntity<?> delete(long id) {
        return this.eventRepository.findById(id)
                .map( evento -> {
                    this.eventRepository.deleteById(id);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new RuntimeException(String.format("Evento com ID %s não existe", id)));
        
    }

	public DetalhesEventoDTO findById(Long id) {
		Optional<Event> result = eventRepository.findById(id);
		if (result.isPresent()) {
			Event evento = result.get();
			return DetalhesEventoDTO.parse(evento);
		}
		return null;
	}

    public Event findEvent(Long id) {
        return eventRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Evento não encontrado"));
    }

    public User findUser(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    /**
   * Inscreve um usuário em um evento.
   * @param eventoID ID do Evento ao qual o usuário deseja participar.  
   * @param userID String unica que identifica o email do usuário.  
   */
	public ResponseEntity<?> subscribe(Long eventoID, Long userID) {
        var event = findEvent(eventoID);
        var user  = findUser(userID);
        var findRelation = findRelation(event.getId(), user.getId());
        if (findRelation.isPresent())
            throw new RuntimeException(String.format("Usuário(a) %s já inscrito no evento", user.getName()));
        var relation = EventUser.builder()
            .event(event)
            .user(user)
            .build();
        eventUserRepository.save(relation);
        return ResponseEntity.ok().build();
	}

	public ResponseEntity<?> removeSubscribe(Long eventoID, Long userID) {
        var event = findEvent(eventoID);
        var user  = findUser(userID);
        var relationID = new EventUserID(event.getId(), user.getId());
        return eventUserRepository.findById(relationID)
            .map(relation -> {
                eventUserRepository.deleteById(relationID);
                return ResponseEntity.ok().build();
            }).orElseThrow( () -> new RuntimeException("Usuário não possui inscrição no evento"));
	}

    @Transactional
	public ResponseEntity<?> registerPresence(Long eventID, RegistroPresencaDTO data) {
       
        return findRelation(eventID, data.getUserID())
        		.map(relation -> {
        			this.validateEventKeyCode(relation, data.getKeyword());
        			this.isToday(eventID);
        			if (relation.isUserPresent())
        				throw new IllegalArgumentException("Sua presença já foi registrada anteriormente");
        			relation.setUserPresent(true);
        	        var eventWorkload = relation.getEvent().getWorkload();
        	        var user = relation.getUser();
        	        user.setWorkload( user.getWorkload() + eventWorkload);
        	        return ResponseEntity.ok().body(UserDTO.parse(user));
        				
        		}).orElseThrow(() -> new RuntimeException("Relação entre evento e usuário não existe"));
    }

    private Optional<EventUser> findRelation(Long eventoID, Long userID){
        return this.eventUserRepository.findById( new EventUserID(eventoID, userID));
    }

    public ResponseEntity<?> getCertification(Long eventID, Long userID){
        return this.findRelation(eventID,userID)
                .map((relation) -> {
                	var participated = relation.isUserPresent();
                	if (!participated) {
                		throw new RuntimeException(String.format("%s não possui registro de presença no evento %s", relation.getUser().getName(), relation.getEvent().getTitle()));
                	}
                    if (relation.getCertificate() == null) {
                        relation.setCertificate(generateCertificate(relation));
                    }
                    return ResponseEntity.ok()
    	                    .contentType(MediaType.APPLICATION_PDF) // "application/octet-stream"
    	                    .header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=\"%s_%s.pdf\"", relation.getEvent().getTitle(), relation.getUser().getName()))
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

    public String generateCertificate(EventUser relacao){
        try {
        	var user = relacao.getUser();
        	var event = relacao.getEvent();
            var resource = s3StorageService.getResource("certificados", "teste_template.jrxml");
            var template = JRXmlLoader.load(resource);
            var relatorio = JasperCompileManager.compileReport( template );
            var parameter  = new HashMap<String, Object>();
			var workload = event.getWorkload();
			parameter.put("name", user.getName());
            parameter.put("event", event.getTitle());
            parameter.put("workload", workload.toString() +" h");
            parameter.put("data", event.getDateTime().toString());
            var jasperPrint = JasperFillManager.fillReport(relatorio, parameter, new JREmptyDataSource());
            var file = File.createTempFile(String.valueOf("user_"+user.getId()),".pdf");
            JasperExportManager.exportReportToPdfFile(jasperPrint, file.getAbsolutePath());
            var saveCertificate = s3StorageService.saveCertificate(file, relacao);
            file.deleteOnExit();
            return saveCertificate;
        } catch (JRException | FileNotFoundException  e) {
            e.printStackTrace();
            throw new RuntimeException("Erro de arquivo");
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

    private void validateEventKeyCode(EventUser relation,String keyword) {
        if (!relation.getEvent().getKeyword().equals(keyword))
            throw new IllegalArgumentException("Código do evento inválido");
    }

    @SuppressWarnings("unused")
	private User findUsuarioByEmail(String username) {
        var findById = userRepository.findByEmail(username);
        return findById.orElseThrow( () -> new IllegalArgumentException("Usuário não encontrado"));
    }

    private Event findEventById(Long id) {
        var findById = eventRepository.findById(id);
        return findById.orElseThrow( () -> new IllegalArgumentException("Usuário não encontrado"));
    }
    // Checa se o evento ocorre hoje
    private void isToday(Long eventoId){
        var event = eventRepository.findById(eventoId).get();
        if ( LocalDateTime.now(ZoneId.of("America/Sao_Paulo")).isBefore(event.getDateTime())){
            throw new IllegalArgumentException("O Evento ainda não está na data");
        }
    }

	public ResponseEntity<?> uploadImage(Long id, MultipartFile image) {
		
        var event = findEventById(id);
        var saveImagePath = this.generateImageEvent(event, image);
        event.setPicture(saveImagePath);
        eventRepository.save(event);
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


	public ResponseEntity<Page<DetalhesEventoDTO>> findAll(Pageable pagination) {
		  var eventPage = eventRepository.findAll(pagination);
			return  ResponseEntity.ok().body(DetalhesEventoDTO.parse(eventPage));
	}

    /**
   * Encontra eventos que usuário se inscreveu.
   * A variável SubscribeLimiteDate é a data limite para o evento ser listado na aba inscritos.
   * Necessária para cobrir situações onde o usuário se inscreve mas não registra presença ou 
   * não cancela inscrição.
   * @param userID Id do usuário
   */
    public ResponseEntity<List<DetalhesEventoDTO>> findEnrolleds(Long userID) {
        var events = new ArrayList<DetalhesEventoDTO>();
        this.eventUserRepository.findByUserId(userID).stream()
        .forEach((eu) -> {
            var currentDate = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
            // Data limite para um evento ser listado na aba de inscrito
            var subscribeLimitDate = eu.getEvent().getDateTime().plusDays(1);
            if (currentDate.isBefore(subscribeLimitDate) && !eu.isUserPresent()) 
                events.add(DetalhesEventoDTO.parse(eu.getEvent()));
        });
        return ResponseEntity.ok().body(events);
    }


    public ResponseEntity<List<DetalhesEventoDTO>> findHistoric(Long userID) {
        var events = new ArrayList<DetalhesEventoDTO>();
        this.eventUserRepository.findByUserId(userID).stream().forEach((eu) -> {
            if (eu.isUserPresent()) {
                events.add(DetalhesEventoDTO.parse(eu.getEvent()));
            }
        });
        return ResponseEntity.ok().body(events);
    }
}
