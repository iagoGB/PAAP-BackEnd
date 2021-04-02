package br.com.casamovel.evento;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.casamovel.dto.evento.DetalhesEventoDTO;
import br.com.casamovel.dto.evento.RegistroPresencaDTO;
import br.com.casamovel.dto.usuario.UsuarioDTO;
import br.com.casamovel.model.Categoria;
import br.com.casamovel.model.Evento;
import br.com.casamovel.model.EventoPalestrante;
import br.com.casamovel.model.EventoUsuario;
import br.com.casamovel.model.EventoUsuarioID;
import br.com.casamovel.model.Palestrante;
import br.com.casamovel.model.Usuario;
import br.com.casamovel.repository.EventoRepository;
import br.com.casamovel.repository.EventoUsuarioRepository;
import br.com.casamovel.repository.UsuarioRepository;
import br.com.casamovel.service.EventoService;

/**
 * EventoUnitTest
 */
public class EventoUnitTest {

    @InjectMocks
    private EventoService eventoService;

    @Mock
    private EventoRepository eventoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private EventoUsuarioRepository euRepository;

    private EventoUsuario eventoUsuario;

    private EventoUsuario initEventUser() {
        var palestrante = Palestrante.builder()
                        .id(1L)
                        .descricao("Um palestrante top")
                        .foto("http://umenderecoqualquer.com.br")
                        .nome("João Palestrinha")
                        .build();

   	    var evento = Evento.builder()
                .id(1L)
                .cargaHoraria(120)
                .categoria(Categoria.builder().id(1L).nome("Arte").build())
                .dataHorario(LocalDateTime.now().plusDays(10))
                .estaAberto(false)
                .titulo("Evento de teste")
                .keyword("XXYYZZ-1")
                .build();
   	 
        var usuario = Usuario.builder()
                .id(10L)
                .nome("Tidinha")
                .cargaHoraria(600) // 10h
                .build();
        
        var ep = EventoPalestrante.builder()
                .evento_id(evento)
                .nome_palestrante_id(palestrante)
                .build();
                
        ep.getEvento_id().setPalestrantes(Arrays.asList(ep));
        ep.getNome_palestrante_id().setEventos(Arrays.asList(ep));
        
        var eu = EventoUsuario.builder()
            .eventoID(evento)
            .usuarioID(usuario)
            .certificate(null)
            .build();

		return eu;
	}

    @BeforeEach
    public void setup(){
        MockitoAnnotations.initMocks(this);
        eventoUsuario = initEventUser();
    }
    
    @Test
    public void deveListarTodosOsEventosAbertos(){
        var events = Arrays.asList(eventoUsuario.getEventoID());
        var expectedList = events.stream()
            .map(DetalhesEventoDTO::parse)
            .collect(Collectors.toList());
            
        when(
            eventoRepository.findAllOpen(any(LocalDateTime.class))
        ).thenReturn(events);
        var resultList = this.eventoService.listarEventos();
        
        // assertEquals(expectedList,resultList);
        assertTrue(expectedList.equals(resultList));
    }

    @Test
    public void deveInscreverUsuarioNoEventoComSucesso(){
        var evento = eventoUsuario.getEventoID();
        var usuario = eventoUsuario.getUsuarioID();
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
            euRepository.findById(new EventoUsuarioID(evento.getId(),usuario.getId()))
        ).thenReturn(Optional.ofNullable(null));

        when(
            euRepository.save(eventoUsuario)
        ).thenReturn(eventoUsuario);

        var result = eventoService.inscreverUsuarioNoEvento(evento.getId(),usuario.getId());

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
    }

    @Test
    public void deveLancarErroAoTentarInscreverUsuarioEmEventoQueJaInscrito(){
        var evento = eventoUsuario.getEventoID();
        var usuario = eventoUsuario.getUsuarioID();

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
            euRepository.findById(new EventoUsuarioID(evento.getId(),usuario.getId()))
        ).thenReturn(Optional.of(eventoUsuario));
        when(
            euRepository.save(eventoUsuario)
        ).thenReturn(eventoUsuario);

        var result = assertThrows(
            RuntimeException.class, () ->
            eventoService.inscreverUsuarioNoEvento(evento.getId(),usuario.getId())
        );

        assertEquals("Usuário(a) Tidinha já inscrito no evento", result.getMessage());
    }

    @Test
    public void deveCancelarInscricaoDoUsuarioEmEventoComSucesso(){
    
        var evento = eventoUsuario.getEventoID();
        var usuario = eventoUsuario.getUsuarioID();
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
            euRepository.findById(new EventoUsuarioID(evento.getId(),usuario.getId()))
        ).thenReturn(Optional.of(eventoUsuario));

        var result = eventoService.removerInscricao(evento.getId(),usuario.getId());

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void deveLancarErroAoTentarCancelarInscricaoEmEventoAoQualUsuarioNaoEstaInscrito(){
    
        var evento = eventoUsuario.getEventoID();
        var usuario = eventoUsuario.getUsuarioID();
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
            euRepository.findById(new EventoUsuarioID(evento.getId(),usuario.getId()))
        ).thenReturn(Optional.ofNullable(null));

        var result =  assertThrows(
            RuntimeException.class, 
            () -> eventoService.removerInscricao(evento.getId(),usuario.getId())
        );

        assertEquals("Usuário não possui inscrição no evento", result.getMessage());
    }

    @Test
    public void deveRegistrarPresencaComSucesso(){
        var evento = eventoUsuario.getEventoID();
        var usuario = eventoUsuario.getUsuarioID();
        var presencaDTO = RegistroPresencaDTO.builder()
            .keyword(evento.getKeyword())
            .userID(usuario.getId())
            .build();
        var cargaHorariaEsperada = usuario.getCargaHoraria() + evento.getCargaHoraria();

        // Muda a data do evento para o passado
        evento.setDataHorario(LocalDateTime.now().minusHours(2));

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
            euRepository.findById(new EventoUsuarioID(evento.getId(),usuario.getId()))
        ).thenReturn(Optional.of(eventoUsuario));


        ResponseEntity<UsuarioDTO>  result = (ResponseEntity<UsuarioDTO>) eventoService.registrarPresenca(evento.getId(), presencaDTO);
        var eventoParticipado = result.getBody().getEvents().stream().filter(e -> e.titulo == evento.getTitulo()).findFirst().orElse(null);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(cargaHorariaEsperada, result.getBody().getWorkload());
        assertEquals(true, eventoParticipado.presente);
    }

    @Test
    public void deveLancarUmErroAoTentarRegistrarPresencaEmEventoNaoInscrito(){
        var evento = eventoUsuario.getEventoID();
        var usuario = eventoUsuario.getUsuarioID();
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
            euRepository.findById(new EventoUsuarioID(evento.getId(),usuario.getId()))
        ).thenReturn(Optional.ofNullable(null));


        var result =  assertThrows(RuntimeException.class, ()-> eventoService.registrarPresenca(evento.getId(), presencaDTO));
     
        assertEquals("Usuário  não inscrito para o evento", result.getMessage());

    }

    @Test
    public void deveLancarUmErroAoTentarRegistrarPresencaAntesDoHorarioDoEvento(){
        var evento = eventoUsuario.getEventoID();
        var usuario = eventoUsuario.getUsuarioID();
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
            euRepository.findById(new EventoUsuarioID(evento.getId(),usuario.getId()))
        ).thenReturn(Optional.ofNullable(eventoUsuario));


        var result =  assertThrows(RuntimeException.class, ()-> eventoService.registrarPresenca(evento.getId(), presencaDTO));
     
        assertEquals("O Evento ainda não está na data", result.getMessage());     
    }

    @Test
    public void deveLancarUmErroAoPassarUmCodigoDeEventoInvalido(){
        var evento = eventoUsuario.getEventoID();
        var usuario = eventoUsuario.getUsuarioID();
        var presencaDTO = RegistroPresencaDTO.builder()
            .keyword("AAABBBCCC-1") // chave de evento errada
            .userID(usuario.getId())
            .build();
        /**
            O evento passa primeiro pelo teste de data, 
            então para passar é necessário deixar a data do evento como se já tivesse acontecido 
        **/
        evento.setDataHorario(LocalDateTime.now().minusHours(2));

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
            euRepository.findById(new EventoUsuarioID(evento.getId(),usuario.getId()))
        ).thenReturn(Optional.ofNullable(eventoUsuario));


        var result =  assertThrows(RuntimeException.class, ()-> eventoService.registrarPresenca(evento.getId(), presencaDTO));
     
        assertEquals("Código do evento inválido", result.getMessage());     
    }


}