package br.com.casamovel.dto.usuario;

import br.com.casamovel.model.EventoUsuario;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class EventoUsuarioDTO {
    public Long id;
    public String titulo;
    public Boolean presente;

    public EventoUsuarioDTO(EventoUsuario eventoUsuario) {
        this.id = eventoUsuario.getEventoID().getId();
        this.titulo = eventoUsuario.getEventoID().getTitulo();
        this.presente = eventoUsuario.isPresent();
    }
}
