package br.com.casamovel.dto.usuario;

import br.com.casamovel.model.EventoUsuario;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EventoUsuarioDTO {
    public Long id;
    public Boolean inscrito;
    public Boolean presente;

    public EventoUsuarioDTO(EventoUsuario eventoUsuario) {
        this.id = eventoUsuario.getEvento_id().getId();
        this.inscrito = eventoUsuario.isSubscribed();
        this.presente = eventoUsuario.isPresent();
    }
}
