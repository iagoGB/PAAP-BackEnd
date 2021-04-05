package br.com.casamovel.evento;

import br.com.casamovel.model.Categoria;
import br.com.casamovel.model.Evento;
import br.com.casamovel.model.EventoUsuario;
import br.com.casamovel.model.EventoUsuarioID;
import br.com.casamovel.model.Usuario;
import br.com.casamovel.repository.EventoUsuarioRepository;
import br.com.casamovel.service.EventoService;
import br.com.casamovel.service.S3StorageService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

public class CertificadoUnitTest {
	
    @InjectMocks
    private EventoService eventoService;
    
    @Mock
    private EventoUsuarioRepository eur;
    @Mock
    private S3StorageService s3;
    
    private EventoUsuario eventoUsuario;
    
    private EventoUsuario initEventUser() {
   	 var evento = Evento.builder()
                .id(1L)
                .cargaHoraria(120)
                .categoria(Categoria.builder().id(1L).nome("Arte").build())
                .dataHorario(LocalDateTime.now().minusDays(10))
                .estaAberto(false)
                .titulo("Evento de  teste")
                .build();
   	 
        var usuario = Usuario.builder().id(10L)
                .nome("Ygona Moura")
                .build();
        
        var eu = EventoUsuario.builder()
                .eventoID(evento)
                .usuarioID(usuario)
                .isPresent(true)
                .build();
		return eu;
	}

    @BeforeEach
    public void setup(){
        MockitoAnnotations.initMocks(this);
        eventoUsuario = initEventUser();
    }
    
    @Test
    public void deveBaixarCertificadoExistenteComSucesso(){
    	
    	eventoUsuario.setCertificate("https://image.freepik.com/vetores-gratis/modelo-de-certificado-elegante_23-2148402681.jpg");
        
        when(eur.findById(new EventoUsuarioID(eventoUsuario.getEventoID().getId(), eventoUsuario.getUsuarioID().getId())))
        .thenReturn(Optional.of(eventoUsuario));
        
        ResponseEntity<?> certificationResp = eventoService.getCertification(eventoUsuario.getEventoID().getId(), eventoUsuario.getUsuarioID().getId());
        
        assertTrue(certificationResp.hasBody());
        assertEquals(200, certificationResp.getStatusCode().value());
        assertEquals( 
        	String.format("%s_%s.pdf", eventoUsuario.getEventoID().getTitulo(), eventoUsuario.getUsuarioID().getNome()), 
        	certificationResp.getHeaders().getContentDisposition().getFilename()
        ); 
    }
    
    @Test
    public void deveLancarUmErroAoTentarBaixarCertificadoDeUsuarioSemRegistroDePresencaNoEvento(){
    	eventoUsuario.setPresent(false);
    	       
        when(eur.findById(new EventoUsuarioID(eventoUsuario.getEventoID().getId(), eventoUsuario.getUsuarioID().getId())))
        .thenReturn(Optional.of(eventoUsuario));
        
        RuntimeException exception = assertThrows(
        	RuntimeException.class,  
        	() -> eventoService.getCertification(eventoUsuario.getEventoID().getId(), eventoUsuario.getUsuarioID().getId())
        );
        assertEquals(
        	String.format("%s não possui registro de presença no evento %s", eventoUsuario.getUsuarioID().getNome(), eventoUsuario.getEventoID().getTitulo()), 
        	exception.getMessage()
        );
       
    }
}
