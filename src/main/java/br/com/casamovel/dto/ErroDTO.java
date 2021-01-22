package br.com.casamovel.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErroDTO {
    private Integer status;
    private String error;
    private String message;
}
