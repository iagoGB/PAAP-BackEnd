package br.com.casamovel.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import br.com.casamovel.model.Usuario;

public class UsuarioDTO {
	
	private String email;
	private Long cpf;
	private String nome;
	private LocalTime cargaHoraria;
	private String departamento;
	private String telefone;
	private LocalDate dataIngresso;
	
	public UsuarioDTO(Usuario usuario) {
		this.nome = usuario.getNome();
		this.cpf = usuario.getCpf();
		this.email = usuario.getEmail();
		this.cargaHoraria = usuario.getCargaHoraria();
		this.departamento = usuario.getDepartamento();
		this.telefone = usuario.getTelefone();
		this.dataIngresso = usuario.getDataIngresso();
	}
	
	public UsuarioDTO() {
		
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
	
	public LocalTime getCargaHoraria() {
		return cargaHoraria;
	}

	public void setCargaHoraria(LocalTime cargaHoraria) {
		this.cargaHoraria = cargaHoraria;
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
		return dataIngresso;
	}
	public void setDataIngresso(LocalDate dataIngresso) {
		this.dataIngresso = dataIngresso;
	}

	
	
	
	
}
