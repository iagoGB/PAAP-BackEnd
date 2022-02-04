/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.paap.service;

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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import java.time.format.DateTimeFormatter;
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

import br.com.paap.dto.event.DetailsEventDTO;
import br.com.paap.dto.event.NewEventDTO;
import br.com.paap.dto.event.RegisterPresenceDTO;
import br.com.paap.dto.event.UpdateEventDTO;
import br.com.paap.dto.user.UserDTO;
import br.com.paap.model.Event;
import br.com.paap.model.EventUser;
import br.com.paap.model.EventUserID;
import br.com.paap.model.User;
import br.com.paap.repository.CategoryRepository;
import br.com.paap.repository.EventRepository;
import br.com.paap.repository.EventUserRepository;
import br.com.paap.repository.UserRepository;
import br.com.paap.util.QRCodeGenerator;
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

    private ObjectMapper objectMapper;

    private final String EVENTS_FOLDER;

    private final String QRCODES_FOLDER;

    private final String eventImageDir = "C:\\CasaMovel\\eventos\\";

    @Autowired
    public EventService(EventRepository eventoRepository, CategoryRepository categoriaRepository,
            UserRepository usuarioRepository, EventUserRepository eventoUsuarioRepository,
            QRCodeGenerator qrCodeGenerator, S3StorageService s3StorageService,
            @Value("${events.folder}") String eventsFolder, @Value("${qrcodes.folder}") String qrcodesFolder,
            ObjectMapper objectMapper) {
        this.eventRepository = eventoRepository;
        this.categoryRepository = categoriaRepository;
        this.userRepository = usuarioRepository;
        this.eventUserRepository = eventoUsuarioRepository;
        this.qrCodeGenerator = qrCodeGenerator;
        this.s3StorageService = s3StorageService;
        this.EVENTS_FOLDER = eventsFolder;
        this.QRCODES_FOLDER = qrcodesFolder;
        this.objectMapper = objectMapper;
    }

    public List<DetailsEventDTO> findAllOpen() {
        // System.out.println("Data do Brasil: "+
        // LocalDateTime.now(ZoneId.of("America/Sao_Paulo")));
        return eventRepository.findAllOpen(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")))
                .stream().map(DetailsEventDTO::parse).collect(Collectors.toList());
    }

    @Transactional
    public ResponseEntity<?> save(MultipartFile image, String event)
            throws JsonMappingException, JsonProcessingException {
        NewEventDTO newEventDTO = objectMapper.readValue(event, NewEventDTO.class);
        return categoryRepository.findById(newEventDTO.getCategory())
                .map(category -> Event.parseFrom(newEventDTO, category))
                .map(newEvent -> {
                    newEvent = eventRepository.save(newEvent);
                    String urlPicture = null;
                    if (image != null)
                        urlPicture = s3StorageService.saveImage(image, newEvent.getId(), EVENTS_FOLDER);
                    var createdKeyword = this.createKeyword(newEvent);
                    newEvent.setKeyword(createdKeyword);
                    var qrCodeURL = this.generateQRCode(newEvent);
                    if (urlPicture != null) newEvent.setPicture(urlPicture);
                    newEvent.setQrCode(qrCodeURL);
                    newEvent = eventRepository.save(newEvent);
                    var response = DetailsEventDTO.parse(newEvent);
                    return ResponseEntity.status(HttpStatus.CREATED).body(response);
                })
                .orElseThrow(() -> new RuntimeException(
                        String.format("Categoria com ID %s não existe", newEventDTO.getCategory())));
    }

    private String createKeyword(Event evento) {
        return String.format("%s_%s", RandomString.make(), evento.getId());
    }

    private String generateQRCode(Event newEvent) {
        String resourceURL = null;
        try {
            var file = File.createTempFile(String.format("qrcode_%d", newEvent.getId()), ".png");
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
            var file = File.createTempFile(String.format("event_%d", event.getId()), ".png");
            image.transferTo(file);
            resourceURL = s3StorageService.saveImage(file, event.getId(), EVENTS_FOLDER);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar link da imagem do evento: " + e.getMessage());
        }
        return resourceURL;
    }

    public ResponseEntity<?> delete(long id) {
        return this.eventRepository.findById(id)
                .map(evento -> {
                    this.eventRepository.deleteById(id);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new RuntimeException(String.format("Evento com ID %s não existe", id)));

    }

    public DetailsEventDTO findById(Long id) {
        Optional<Event> result = eventRepository.findById(id);
        if (result.isPresent()) {
            Event evento = result.get();
            return DetailsEventDTO.parse(evento);
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
     * 
     * @param eventoID ID do Evento ao qual o usuário deseja participar.
     * @param userID   Id do usuário.
     */
    public ResponseEntity<?> subscribe(Long eventoID, Long userID) {
        var event = findEvent(eventoID);
        var user = findUser(userID);
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
        var user = findUser(userID);
        var relationID = new EventUserID(event.getId(), user.getId());
        return eventUserRepository.findById(relationID)
                .map(relation -> {
                    eventUserRepository.deleteById(relationID);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new RuntimeException("Usuário não possui inscrição no evento"));
    }

    @Transactional
    public ResponseEntity<?> registerPresence(Long eventID, RegisterPresenceDTO data) {

        return findRelation(eventID, data.getUserID())
                .map(relation -> {
                    this.validateEventKeyCode(relation, data.getKeyword());
                    this.checkIfEventIsHappening(eventID);
                    if (relation.isUserPresent())
                        throw new IllegalArgumentException("Sua presença já foi registrada anteriormente");
                    relation.setUserPresent(true);
                    var eventWorkload = relation.getEvent().getWorkload();
                    var user = relation.getUser();
                    user.setWorkload(user.getWorkload() + eventWorkload);
                    return ResponseEntity.ok().body(UserDTO.parse(user));

                }).orElseThrow(() -> new RuntimeException("Relação entre evento e usuário não existe"));
    }

    private Optional<EventUser> findRelation(Long eventoID, Long userID) {
        return this.eventUserRepository.findById(new EventUserID(eventoID, userID));
    }

    public ResponseEntity<?> getCertification(Long eventID, Long userID) {
        return this.findRelation(eventID, userID)
                .map((relation) -> {
                    var participated = relation.isUserPresent();
                    if (!participated) {
                        throw new RuntimeException(String.format("%s não possui registro de presença no evento %s",
                                relation.getUser().getName(), relation.getEvent().getTitle()));
                    }
                    if (relation.getCertificate() == null) {
                        relation.setCertificate(this.generateCertificate(relation));
                    }
                    return ResponseEntity.ok()
                            .contentType(MediaType.APPLICATION_PDF) // "application/octet-stream"
                            .header(HttpHeaders.CONTENT_DISPOSITION,
                                    String.format("attachment; filename=\"%s_%s.pdf\"", relation.getEvent().getTitle(),
                                            relation.getUser().getName()))
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

    public String generateCertificate(EventUser relacao) {
        try {
            var user = relacao.getUser();
            var event = relacao.getEvent();
            var resource = s3StorageService.getResource("certificados", "teste_template.jrxml");
            var template = JRXmlLoader.load(resource);
            var relatorio = JasperCompileManager.compileReport(template);
            var parameter = new HashMap<String, Object>();
            var workload = event.getWorkload();
            parameter.put("name", user.getName());
            parameter.put("event", event.getTitle());
            parameter.put("workload", workload.toString() + " h");
            var formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy às HH:mm");
            var formatedDate = event.getDateTime().format(formatter);
            parameter.put("data", formatedDate);
            var jasperPrint = JasperFillManager.fillReport(relatorio, parameter, new JREmptyDataSource());
            var file = File.createTempFile(String.valueOf("user_" + user.getId()), ".pdf");
            JasperExportManager.exportReportToPdfFile(jasperPrint, file.getAbsolutePath());
            var saveCertificate = s3StorageService.saveCertificate(file, relacao);
            file.deleteOnExit();
            return saveCertificate;
        } catch (JRException | FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro de arquivo");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro inexperado");
        }
    }

    public void drawStringMultiLine(Graphics2D g, String text, int lineWidth, int x, int y) {
        FontMetrics m = g.getFontMetrics();
        if (m.stringWidth(text) < lineWidth) {
            g.drawString(text, x, y);
        } else {
            String[] words = text.split(" ");
            String currentLine = words[0];
            for (int i = 1; i < words.length; i++) {
                if (m.stringWidth(currentLine + words[i]) < lineWidth) {
                    currentLine += " " + words[i];
                } else {
                    g.drawString(currentLine, x, y);
                    y += m.getHeight();
                    currentLine = words[i];
                }
            }
            if (currentLine.trim().length() > 0) {
                g.drawString(currentLine, x, y);
            }
        }
    }

    private void validateEventKeyCode(EventUser relation, String keyword) {
        if (!relation.getEvent().getKeyword().equals(keyword))
            throw new IllegalArgumentException("Código do evento inválido");
    }

    @SuppressWarnings("unused")
    private User findUsuarioByEmail(String username) {
        var findById = userRepository.findByEmail(username);
        return findById.orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
    }

    private Event findEventById(Long id) {
        var findById = eventRepository.findById(id);
        return findById.orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
    }

    // Checa se o evento está acontecendo
    private void checkIfEventIsHappening(Long eventoId) {
        var event = eventRepository.findById(eventoId).get();
        if (LocalDateTime.now(ZoneId.of("America/Sao_Paulo")).isBefore(event.getDateTime())) {
            throw new IllegalArgumentException("O Evento ainda não foi iniciado");
        }
    }

    public ResponseEntity<?> uploadImage(Long id, MultipartFile image) {

        var event = findEventById(id);
        var saveImagePath = this.generateImageEvent(event, image);
        event.setPicture(saveImagePath);
        eventRepository.save(event);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> getImageEvent(String filename) {
        Path path = Paths.get(eventImageDir + filename);
        // Resource resource = null;
        try {
            var resource = new FileSystemResource(path);
            System.out.println("Path: " + path.toString());
            var bytes = StreamUtils.copyToByteArray(resource.getInputStream());
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // "application/octet-stream"
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(bytes);
        } catch (FileNotFoundException ex) {
            System.out.println("Arquivo não encontrado!!!!!!");
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.out.println("Outro erro qualquer!!!!!!");
            return ResponseEntity.status(500).build();
        }

    }

    public ResponseEntity<Page<DetailsEventDTO>> findAll(Pageable pagination) {
        var eventPage = eventRepository.findAll(pagination);
        return ResponseEntity.ok().body(DetailsEventDTO.parse(eventPage));
    }

    /**
     * Encontra eventos que usuário se inscreveu.
     * A variável SubscribeLimiteDate é a data limite para o evento ser listado na
     * aba inscritos.
     * Necessária para cobrir situações onde o usuário se inscreve mas não registra
     * presença ou
     * não cancela inscrição.
     * 
     * @param userID Id do usuário
     */
    public ResponseEntity<List<DetailsEventDTO>> findEnrolleds(Long userID) {
        var events = new ArrayList<DetailsEventDTO>();
        this.eventUserRepository.findByUserId(userID).stream()
                .forEach((eu) -> {
                    var currentDate = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
                    // Data limite para um evento ser listado na aba de inscrito
                    var subscribeLimitDate = eu.getEvent().getDateTime().plusDays(1);
                    if (currentDate.isBefore(subscribeLimitDate) && !eu.isUserPresent())
                        events.add(DetailsEventDTO.parse(eu.getEvent()));
                });
        return ResponseEntity.ok().body(events);
    }

    public ResponseEntity<List<DetailsEventDTO>> findHistoric(Long userID) {
        var events = new ArrayList<DetailsEventDTO>();
        this.eventUserRepository.findByUserId(userID).stream().forEach((eu) -> {
            if (eu.isUserPresent()) {
                events.add(DetailsEventDTO.parse(eu.getEvent()));
            }
        });
        return ResponseEntity.ok().body(events);
    }

    public ResponseEntity<?> update(MultipartFile image, String event)
            throws JsonMappingException, JsonProcessingException {
        var updatedEvent = objectMapper.readValue(event, UpdateEventDTO.class);
        return eventRepository.findById(updatedEvent.getId())
                .map(e -> {
                    var category = categoryRepository.findById(updatedEvent.getCategory()).get();
                    e.setCategory(category);
                    e.setWorkload(updatedEvent.getWorkload());
                    e.setTitle(updatedEvent.getTitle());
                    e.setDateTime(updatedEvent.getDateTime());
                    e.setDescription(updatedEvent.getDescription());
                    e.setLocal(updatedEvent.getLocation());
                    e.setSpeakers(updatedEvent.getSpeakers());
                    String eventImage = null;
                    if (image != null)
                        eventImage = this.s3StorageService.saveImage(image, e.getId(), this.EVENTS_FOLDER);
                    if (eventImage != null)
                        e.setPicture(eventImage);
                    eventRepository.save(e);
                    return ResponseEntity.ok().build();
                }).orElseThrow(
                        () -> new RuntimeException(String.format("Evento com ID %s não existe", updatedEvent.getId())));

    }

    public ResponseEntity<?> findByTitle(String query) {
        var fondEvents = this.eventRepository.findByTitleContainingIgnoreCase(query);
        var events = fondEvents.stream().map(e -> DetailsEventDTO.parse(e)).collect(Collectors.toList());
        return ResponseEntity.ok().body(events);
    }
}
