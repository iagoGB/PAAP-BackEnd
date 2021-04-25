package br.com.casamovel.evento;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.casamovel.dto.evento.RegistroPresencaDTO;
import br.com.casamovel.dto.usuario.UsuarioDTO;
import br.com.casamovel.model.Category;
import br.com.casamovel.model.Event;
import br.com.casamovel.model.EventUser;
import br.com.casamovel.model.EventUserID;
import br.com.casamovel.model.User;
import br.com.casamovel.repository.EventRepository;
import br.com.casamovel.repository.EventUserRepository;
import br.com.casamovel.repository.UserRepository;
import br.com.casamovel.service.EventService;

/**
 * EventoUnitTest
 */
public class EventUnitTest {

    @InjectMocks
    private EventService eventoService;

    @Mock
    private EventRepository eventoRepository;

    @Mock
    private UserRepository usuarioRepository;

    @Mock
    private EventUserRepository euRepository;

    private EventUser EventUser;

    private EventUser initEventUser() {

   	    var evento = Event.builder()
                .id(1L)
                .workload(120)
                .category(Category.builder().id(1L).name("Arte").build())
                .dateTime(LocalDateTime.now().plusDays(10))
                .isOpen(false)
                .title("Evento de teste")
                .keyword("XXYYZZ-1")
                .build();
   	 
        var usuario = User.builder()
                .id(10L)
                .name("Tidinha")
                .workload(600) // 10h
                .build();
        
        
        var eu = br.com.casamovel.model.EventUser.builder()
            .event(evento)
            .user(usuario)
            .certificate(null)
            .build();

		return eu;
	}

    @BeforeEach
    public void setup(){
        MockitoAnnotations.initMocks(this);
        EventUser = initEventUser();
    }

    @Test
    public void deveInscreverUsuarioNoEventoComSucesso(){
        var evento = EventUser.getEvent();
        var usuario = EventUser.getUser();
        // Evento Existe
        when(
            eventoRepository.findById(evento.getId())
        ).thenReturn(Optional.of(evento));
        // Usuario Existe
        when(
            usuarioRepository.findById(usuario.getId())
        ).thenReturn(Optional.of(usuario));
        // Inscrição ainda não feita 
        when(
            euRepository.findById(new EventUserID(evento.getId(),usuario.getId()))
        ).thenReturn(Optional.ofNullable(null));

        when(
            euRepository.save(EventUser)
        ).thenReturn(EventUser);

        var result = eventoService.subscribe(evento.getId(),usuario.getId());

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
    }

    @Test
    public void deveLancarErroAoTentarInscreverUsuarioEmEventoJaInscrito(){
        var evento = EventUser.getEvent();
        var usuario = EventUser.getUser();

        // Evento Existe
        when(
            eventoRepository.findById(evento.getId())
        ).thenReturn(Optional.of(evento));
        // Usuário Existe
        when(
            usuarioRepository.findById(usuario.getId())
        ).thenReturn(Optional.of(usuario));
        // Inscrição já existe
        when(
            euRepository.findById(new EventUserID(evento.getId(),usuario.getId()))
        ).thenReturn(Optional.of(EventUser));
        when(
            euRepository.save(EventUser)
        ).thenReturn(EventUser);

        var result = assertThrows(
            RuntimeException.class, () ->
            eventoService.subscribe(evento.getId(),usuario.getId())
        );

        assertEquals("Usuário(a) Tidinha já inscrito no evento", result.getMessage());
    }

    @Test
    public void deveCancelarInscricaoDoUsuarioEmEventoComSucesso(){
    
        var evento = EventUser.getEvent();
        var usuario = EventUser.getUser();
        // Evento Existe
        when(
            eventoRepository.findById(evento.getId())
        ).thenReturn(Optional.of(evento));
        // Usuario Existe
        when(
            usuarioRepository.findById(usuario.getId())
        ).thenReturn(Optional.of(usuario));
        // Inscrição existe
        when(
            euRepository.findById(new EventUserID(evento.getId(),usuario.getId()))
        ).thenReturn(Optional.of(EventUser));

        var result = eventoService.removeSubscribe(evento.getId(),usuario.getId());

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void deveLancarErroAoTentarCancelarInscricaoEmEventoAoQualUsuarioNaoEstaInscrito(){
    
        var evento = EventUser.getEvent();
        var usuario = EventUser.getUser();
        // Evento Existe
        when(
            eventoRepository.findById(evento.getId())
        ).thenReturn(Optional.of(evento));
        // Usuário Existe
        when(
            usuarioRepository.findById(usuario.getId())
        ).thenReturn(Optional.of(usuario));
        // Inscrição não foi realizada previamente
        when(
            euRepository.findById(new EventUserID(evento.getId(),usuario.getId()))
        ).thenReturn(Optional.ofNullable(null));

        var result =  assertThrows(
            RuntimeException.class, 
            () -> eventoService.removeSubscribe(evento.getId(),usuario.getId())
        );

        assertEquals("Usuário não possui inscrição no evento", result.getMessage());
    }

    @Test
    public void deveRegistrarPresencaComSucesso(){
        var evento = EventUser.getEvent();
        var usuario = EventUser.getUser();
        var presencaDTO = RegistroPresencaDTO.builder()
            .keyword(evento.getKeyword())
            .userID(usuario.getId())
            .build();
        var cargaHorariaEsperada = usuario.getWorkload() + evento.getWorkload();

        // Muda a data do evento para o passado
        evento.setDateTime(LocalDateTime.now().minusHours(2));

        // Usuario Existe
        when(
            eventoRepository.findById(evento.getId())
        ).thenReturn(Optional.of(evento));

        // Usuario Existe
        when(
            usuarioRepository.findById(usuario.getId())
        ).thenReturn(Optional.of(usuario));
        // Usuario Inscrito
        when(
            euRepository.findById(new EventUserID(evento.getId(),usuario.getId()))
        ).thenReturn(Optional.of(EventUser));


        @SuppressWarnings("unchecked")
		ResponseEntity<UsuarioDTO>  result = (ResponseEntity<UsuarioDTO>) eventoService.registerPresence(evento.getId(), presencaDTO);
        var eventoParticipado = result.getBody().getEvents().stream().filter(e -> e.titulo == evento.getTitle()).findFirst().orElse(null);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(cargaHorariaEsperada, result.getBody().getWorkload());
        assertEquals(true, eventoParticipado.presente);
    }

    @Test
    public void deveLancarUmErroAoTentarRegistrarPresencaEmEventoNaoInscrito(){
        var evento = EventUser.getEvent();
        var usuario = EventUser.getUser();
        var presencaDTO = RegistroPresencaDTO.builder()
            .keyword(evento.getKeyword())
            .userID(usuario.getId())
            .build();

        // Evento Existe
        when(
            eventoRepository.findById(evento.getId())
        ).thenReturn(Optional.of(evento));

        // Usuario Existe
        when(
            usuarioRepository.findById(usuario.getId())
        ).thenReturn(Optional.of(usuario));
        // Inscrição não existe
        when(
            euRepository.findById(new EventUserID(evento.getId(),usuario.getId()))
        ).thenReturn(Optional.ofNullable(null));


        var result =  assertThrows(RuntimeException.class, ()-> eventoService.registerPresence(evento.getId(), presencaDTO));
     
        assertEquals("Usuário  não inscrito para o evento", result.getMessage());

    }

    @Test
    public void deveLancarUmErroAoTentarRegistrarPresencaAntesDoHorarioDoEvento(){
        var evento = EventUser.getEvent();
        var usuario = EventUser.getUser();
        var presencaDTO = RegistroPresencaDTO.builder()
            .keyword(evento.getKeyword())
            .userID(usuario.getId())
            .build();

        // Evento Existe
        when(
            eventoRepository.findById(evento.getId())
        ).thenReturn(Optional.of(evento));

        // Usuario Existe
        when(
            usuarioRepository.findById(usuario.getId())
        ).thenReturn(Optional.of(usuario));

        // Evento existe
        when(
            euRepository.findById(new EventUserID(evento.getId(),usuario.getId()))
        ).thenReturn(Optional.ofNullable(EventUser));


        var result =  assertThrows(RuntimeException.class, ()-> eventoService.registerPresence(evento.getId(), presencaDTO));
     
        assertEquals("O Evento ainda não está na data", result.getMessage());     
    }

    @Test
    public void deveLancarUmErroAoPassarUmCodigoDeEventoInvalido(){
        var evento = EventUser.getEvent();
        var usuario = EventUser.getUser();
        var presencaDTO = RegistroPresencaDTO.builder()
            .keyword("AAABBBCCC-1") // chave de evento errada
            .userID(usuario.getId())
            .build();
        /**
            O evento passa primeiro pelo teste de data, 
            então para passar é necessário deixar a data do evento como se já tivesse acontecido 
        **/
        evento.setDateTime(LocalDateTime.now().minusHours(2));

        // Evento Existe
        when(
            eventoRepository.findById(evento.getId())
        ).thenReturn(Optional.of(evento));

        // Usuario Existe
        when(
            usuarioRepository.findById(usuario.getId())
        ).thenReturn(Optional.of(usuario));

        // Evento existe
        when(
            euRepository.findById(new EventUserID(evento.getId(),usuario.getId()))
        ).thenReturn(Optional.ofNullable(EventUser));


        var result =  assertThrows(RuntimeException.class, ()-> eventoService.registerPresence(evento.getId(), presencaDTO));
     
        assertEquals("Código do evento inválido", result.getMessage());     
    }


}