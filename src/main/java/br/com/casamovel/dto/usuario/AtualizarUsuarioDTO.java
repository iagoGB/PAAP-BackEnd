package br.com.casamovel.dto.usuario;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.com.casamovel.model.User;
import br.com.casamovel.repository.UserRepository;

public class AtualizarUsuarioDTO {
	@NotNull @NotEmpty @Email(message = "Não é um endereço de email válido")
	private String email;
	@NotNull @NotEmpty
	private String phone;
	
	
	//Traz usuário no banco e atualiza informações
	public User update(Long id ,UserRepository usuarioRepository) {
		User usuario = usuarioRepository.getOne(id);
		usuario.setEmail(this.email);
		usuario.setPhone(this.phone);
		return usuario;
	} 

}
