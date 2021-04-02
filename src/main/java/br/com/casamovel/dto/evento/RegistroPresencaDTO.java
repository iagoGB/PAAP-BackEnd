package br.com.casamovel.dto.evento;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class RegistroPresencaDTO {
    public String keyword;
    public String username;
    public Long userID;
}