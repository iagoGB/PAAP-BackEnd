package br.com.casamovel.dto;

import java.time.LocalDate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.casamovel.model.Usuario;

public class NovoUsuarioDTO {
	@NotNull @NotEmpty @Email(message = "Não é um endereço de email válido")
	private String email;
	@NotNull
	private Long cpf;
	@NotNull @NotEmpty
	private String nome;
	@NotNull @NotEmpty
	private String senha;
	@NotNull @NotEmpty
	private String departamento;
	@NotNull @NotEmpty
	private String telefone;
	@NotNull @PastOrPresent
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX" ,timezone = "GMT-3")
	private LocalDate data_ingresso;
	
	public NovoUsuarioDTO(Usuario usuario) {
		this.nome = usuario.getNome();
		this.cpf = usuario.getCpf();
		this.email = usuario.getEmail();
		this.senha = usuario.getSenha();
		this.departamento = usuario.getDepartamento();
		this.telefone = usuario.getTelefone();
		this.data_ingresso = usuario.getDataIngresso();
	}
	
	public NovoUsuarioDTO() {
		
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Long getCpf() {
		return cpf;
	}
	public void setCpf(Long cpf) {
		this.cpf = cpf;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
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
	public LocalDate getDataIngresso() {
		return data_ingresso;
	}
	public void setDataIngresso(LocalDate dataIngresso) {
		this.data_ingresso = dataIngresso;
	}
	

	@Override
	public String toString() {
		return "NovoUsuarioDTO [email=" + email + ", cpf=" + cpf + ", nome=" + nome + ", senha=" + senha
				+ ", departamento=" + departamento + ", telefone=" + telefone + ", dataIngresso=" + data_ingresso + "]";
	}

	
	
	
	
	
}
