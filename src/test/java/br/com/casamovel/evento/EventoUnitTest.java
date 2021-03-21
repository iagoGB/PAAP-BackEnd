package br.com.casamovel.evento;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.casamovel.dto.evento.DetalhesEventoDTO;
import br.com.casamovel.model.Categoria;
import br.com.casamovel.model.Evento;
import br.com.casamovel.model.EventoPalestrante;
import br.com.casamovel.model.EventoUsuario;
import br.com.casamovel.model.Palestrante;
import br.com.casamovel.model.Usuario;
import br.com.casamovel.repository.EventoRepository;
import br.com.casamovel.service.EventoService;

/**
 * EventoUnitTest
 */
public class EventoUnitTest {

    @InjectMocks
    private EventoService eventoService;

    @Mock
    private EventoRepository eventoRepository;

    private EventoUsuario eventoUsuario;

    private EventoUsuario initEventUser() {
        var palestrante = Palestrante.builder()
                        .id(1L)
                        .descricao("Um palestrante top")
                        .foto("http://umenderecoqualquer.com.br")
                        .nome("Tiburcio")
                        .build();

   	    var evento = Evento.builder()
                .id(1L)
                .cargaHoraria(120)
                .categoria(Categoria.builder().id(1L).nome("Arte").build())
                .dataHorario(LocalDateTime.now().plusDays(10))
                .estaAberto(false)
                .titulo("Evento de teste")
                .build();
   	 
        var usuario = Usuario.builder().id(10L)
                .nome("Tidinha")
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
                .isPresent(false)
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
        System.out.println(resultList.get(0).getTitulo());
        
        // assertEquals(expectedList,resultList);
        assertTrue(expectedList.equals(resultList));
    }
}