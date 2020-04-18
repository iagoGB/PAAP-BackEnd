package br.com.casamovel.dto.autenticacao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * RespostaAutenticacao
 */

@AllArgsConstructor
@ToString
@Builder
@Getter
public class RespostaAutenticacao {
	private String token;
	private String role;
	private String username;
}