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
	private Long id;
	private String token;
	private String role;
	private String username;
}