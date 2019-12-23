package br.com.casamovel.dto;

import java.time.LocalDateTime;

public class NovoUsuarioDTO {
	
	private String email;
	private Long cpf;
	private String nome;
	private String senha;
	private String departamento;
	private String telefone;
	private LocalDateTime dataIngresso;
	
	public NovoUsuarioDTO(String email, Long cpf, String nome, String senha, String departamento, String telefone,
			LocalDateTime dataIngresso) {
		this.email = email;
		this.cpf = cpf;
		this.nome = nome;
		this.senha = senha;
		this.departamento = departamento;
		this.telefone = telefone;
		this.dataIngresso = dataIngresso;
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
	public LocalDateTime getDataIngresso() {
		return dataIngresso;
	}
	public void setDataIngresso(LocalDateTime dataIngresso) {
		this.dataIngresso = dataIngresso;
	}
	
	
}
