package br.com.paap.dto.user;

import br.com.paap.model.EventUser;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class EventUserDTO {
    public Long id;
    public String title;
    public Boolean isPresent;

    public EventUserDTO(EventUser eventUser) {
        this.id = eventUser.getEvent().getId();
        this.title = eventUser.getEvent().getTitle();
        this.isPresent = eventUser.isUserPresent();
    }
}
