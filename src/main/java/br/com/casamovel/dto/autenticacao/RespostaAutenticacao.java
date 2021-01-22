package br.com.casamovel.dto.autenticacao;

import lombok.*;

/**
 * RespostaAutenticacao
 */

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Getter
public class RespostaAutenticacao {
	private String token;
	private String role;
	private String username;
}