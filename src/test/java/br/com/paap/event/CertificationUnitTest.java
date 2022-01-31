package br.com.paap.event;

import static org.junit.Assert.assertTrue;
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
import org.springframework.http.ResponseEntity;

import br.com.paap.model.Category;
import br.com.paap.model.Event;
import br.com.paap.model.EventUser;
import br.com.paap.model.EventUserID;
import br.com.paap.model.User;
import br.com.paap.repository.EventUserRepository;
import br.com.paap.service.EventService;
import br.com.paap.service.S3StorageService;

public class CertificationUnitTest {
	
    @InjectMocks
    private EventService eventoService;
    
    @Mock
    private EventUserRepository eur;
    @Mock
    private S3StorageService s3;
    
    private EventUser eventoUsuario;
    
    private EventUser initEventUser() {
   	 var event = Event.builder()
                .id(1L)
                .workload(120)
                .category(Category.builder().id(1L).name("Arte").build())
                .dateTime(LocalDateTime.now().minusDays(10))
                .title("Evento de  teste")
                .build();
   	 
        var user = User.builder().id(10L)
                .name("Ygona Moura")
                .build();
        
        var eu = EventUser.builder()
                .event(event)
                .user(user)
                .isUserPresent(true)
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
        
        when(eur.findById(new EventUserID(eventoUsuario.getEvent().getId(), eventoUsuario.getUser().getId())))
        .thenReturn(Optional.of(eventoUsuario));
        
        ResponseEntity<?> certificationResp = eventoService.getCertification(eventoUsuario.getEvent().getId(), eventoUsuario.getUser().getId());
        
        assertTrue(certificationResp.hasBody());
        assertEquals(200, certificationResp.getStatusCode().value());
        assertEquals( 
        	String.format("%s_%s.pdf", eventoUsuario.getEvent().getTitle(), eventoUsuario.getUser().getName()), 
        	certificationResp.getHeaders().getContentDisposition().getFilename()
        ); 
    }
    
    @Test
    public void deveLancarUmErroAoTentarBaixarCertificadoDeUsuarioSemRegistroDePresencaNoEvento(){
    	eventoUsuario.setUserPresent(false);
    	       
        when(eur.findById(new EventUserID(eventoUsuario.getEvent().getId(), eventoUsuario.getUser().getId())))
        .thenReturn(Optional.of(eventoUsuario));
        
        RuntimeException exception = assertThrows(
        	RuntimeException.class,  
        	() -> eventoService.getCertification(eventoUsuario.getEvent().getId(), eventoUsuario.getUser().getId())
        );
        assertEquals(
        	String.format("%s não possui registro de presença no evento %s", eventoUsuario.getUser().getName(), eventoUsuario.getEvent().getTitle()), 
        	exception.getMessage()
        );
       
    }
}
