package br.com.paap.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorDTO {
    private Integer status;
    private String error;
    private String message;
}
