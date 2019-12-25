package br.com.casamovel.dto;

import java.time.LocalDate;
import java.time.LocalTime;


import org.springframework.data.domain.Page;

import br.com.casamovel.model.Usuario;

public class UsuarioDTO {
	private Long id;
	private String email;
	private Long cpf;
	private String nome;
	private LocalTime cargaHoraria;
	private String departamento;
	private String telefone;
	private LocalDate dataIngresso;
	
	public UsuarioDTO(Usuario usuario) {
		this.id = usuario.getId();
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
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
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
	
	public static Page<UsuarioDTO> parse(Page<Usuario> usuarios) {
		return usuarios.map(UsuarioDTO::new);
	}
	
	public static UsuarioDTO parse(Usuario usuario) {
		return new UsuarioDTO(usuario);
	}

	
	
	
	
	
}
