package br.com.paap.dto.event;

import br.com.paap.model.Event;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class EventDTO {
    Long id;
	String foto;
	String titulo;
	String localizacao;
	String data_horario;
	Integer carga_horaria;
    String categoria;
    
    public EventDTO(Event evento){
        this.id = evento.getId();
        this.foto = evento.getPicture();
        this.titulo = evento.getTitle();
        this.localizacao = evento.getLocal();
        this.data_horario = evento.getDateTime().toString();
        this.carga_horaria = evento.getWorkload();
        this.categoria = evento.getCategory().getName();
    }

    public static EventDTO parse(Event evento){
        return new EventDTO(evento);
    }
}
