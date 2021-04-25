package br.com.casamovel.dto.usuario;

import br.com.casamovel.model.EventUser;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class EventoUsuarioDTO {
    public Long id;
    public String titulo;
    public Boolean presente;

    public EventoUsuarioDTO(EventUser eventUser) {
        this.id = eventUser.getEvent().getId();
        this.titulo = eventUser.getEvent().getTitle();
        this.presente = eventUser.isPresent();
    }
}
