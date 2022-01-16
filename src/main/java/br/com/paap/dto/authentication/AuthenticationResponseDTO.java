package br.com.paap.dto.authentication;

import lombok.*;

/**
 * RespostaAutenticacao
 */

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Getter
public class AuthenticationResponseDTO {
	private Long id;
	private String token;
	private String role;
	private String username;
}