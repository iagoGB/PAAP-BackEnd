package br.com.paap.dto;

import lombok.Getter;

@Getter
public class ValidationErrorDTO {
	private String field;
	private String error;
	
	public ValidationErrorDTO(String campo, String erro) {
		this.field = campo;
		this.error = erro;
	}
	
}
