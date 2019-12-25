package br.com.casamovel.dto;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.com.casamovel.model.Usuario;
import br.com.casamovel.repository.UsuarioRepository;

public class AtualizarUsuarioDTO {
	@NotNull @NotEmpty @Email(message = "Não é um endereço de email válido")
	private String email;
	@NotNull @NotEmpty
	private String senha;
	@NotNull @NotEmpty
	private String departamento;
	@NotNull @NotEmpty
	private String telefone;
	
	
	public AtualizarUsuarioDTO(Usuario usuario) {
		this.email = usuario.getEmail();
		this.senha = usuario.getSenha();
		this.departamento = usuario.getDepartamento();
		this.telefone = usuario.getTelefone();
	}
	
	public AtualizarUsuarioDTO() {
		
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public String getDepartamento() {
		return departamento;
	}
	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}
	public String getTelefone() {
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	
	//Traz usuário no banco e atualiza informações
	public Usuario atualizar(Long id ,UsuarioRepository usuarioRepository) {
		
		Usuario usuario = usuarioRepository.getOne(id);
		usuario.setEmail(this.email);
		usuario.setSenha(new BCryptPasswordEncoder().encode(this.senha));
		usuario.setDepartamento(this.departamento);
		usuario.setTelefone(this.telefone);
		return usuario;
	} 

	@Override
	public String toString() {
		return "AtualizarUsuarioDTO [email=" + email + ", senha=" + senha + ", departamento=" + departamento
				+ ", telefone=" + telefone + "]";
	}

	

}
