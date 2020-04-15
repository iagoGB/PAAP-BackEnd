package br.com.casamovel.dto.autenticacao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * RespostaAutenticacao
 */
@Builder
@AllArgsConstructor
@Getter
public class RespostaAutenticacao {
	private String token;
	private String role;
	private String username;
}