package br.com.casamovel.dto.usuario;

import java.time.LocalDate;
import java.time.LocalTime;


import org.springframework.data.domain.Page;

import br.com.casamovel.model.Usuario;

public class UsuarioDTO {
	private Long id;
	private String email;
	private Long cpf;
	private String nome;
	private LocalTime carga_horaria;
	private String departamento;
	private String telefone;
	private LocalDate data_ingresso;
	private String avatar;
	
	public UsuarioDTO(Usuario usuario) {
		this.id = usuario.getId();
		this.nome = usuario.getNome();
		this.cpf = usuario.getCpf();
		this.email = usuario.getEmail();
		this.setCarga_horaria(usuario.getCargaHoraria());
		this.departamento = usuario.getDepartamento();
		this.telefone = usuario.getTelefone();
		this.setData_ingresso(usuario.getDataIngresso());
		this.setAvatar(usuario.getAvatar());
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
	
	public static Page<UsuarioDTO> parse(Page<Usuario> usuarios) {
		return usuarios.map(UsuarioDTO::new);
	}
	
	public static UsuarioDTO parse(Usuario usuario) {
		return new UsuarioDTO(usuario);
	}

	public LocalTime getCarga_horaria() {
		return carga_horaria;
	}

	public void setCarga_horaria(LocalTime carga_horaria) {
		this.carga_horaria = carga_horaria;
	}

	public LocalDate getData_ingresso() {
		return data_ingresso;
	}

	public void setData_ingresso(LocalDate data_ingresso) {
		this.data_ingresso = data_ingresso;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	
	
	
	
	
}
