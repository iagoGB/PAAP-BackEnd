package br.com.casamovel.dto.autenticacao;

import br.com.casamovel.dto.usuario.UsuarioDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * RespostaAutenticacao
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RespostaAutenticacao {

    public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	private String token;
    private UsuarioDTO usuario;
}