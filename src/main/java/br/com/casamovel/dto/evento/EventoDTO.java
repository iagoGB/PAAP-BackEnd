package br.com.casamovel.dto.evento;

import br.com.casamovel.model.Evento;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class EventoDTO {
    Long id;
	String foto;
	String titulo;
	String localizacao;
	String data_horario;
	Integer carga_horaria;
    String categoria;
    
    public EventoDTO(Evento evento){
        this.id = evento.getId();
        this.foto = evento.getFoto();
        this.titulo = evento.getTitulo();
        this.localizacao = evento.getLocal();
        this.data_horario = evento.getDataHorario().toString();
        this.carga_horaria = evento.getCargaHoraria();
        this.categoria = evento.getCategoria().getNome();
    }

    public static EventoDTO parse(Evento evento){
        return new EventoDTO(evento);
    }
}
