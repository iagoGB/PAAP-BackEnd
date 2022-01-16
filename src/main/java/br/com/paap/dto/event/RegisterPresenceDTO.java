package br.com.paap.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class RegisterPresenceDTO {
    public String keyword;
    public String username;
    public Long userID;
}