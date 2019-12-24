package br.com.casamovel.dto;

import java.time.LocalDate;
import java.util.List;

import br.com.casamovel.model.Usuario;

public class NovoUsuarioDTO {
	
	private String email;
	private Long cpf;
	private String nome;
	private String senha;
	private String departamento;
	private String telefone;
	private LocalDate dataIngresso;
	private List<EventoDTO> eventos;
	
	public NovoUsuarioDTO(Usuario usuario) {
		this.nome = usuario.getNome();
		this.cpf = usuario.getCpf();
		this.email = usuario.getEmail();
		this.senha = usuario.getSenha();
		this.departamento = usuario.getDepartamento();
		this.telefone = usuario.getTelefone();
		this.dataIngresso = usuario.getDataIngresso();
		//Fazer a conversão dos eventos pra dto e dps inserir aqui
		//this.eventos = usuario.getEventos();
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
		return dataIngresso;
	}
	public void setDataIngresso(LocalDate dataIngresso) {
		this.dataIngresso = dataIngresso;
	}
	public List<EventoDTO> getEventos() {
		return eventos;
	}

	public void setEventos(List<EventoDTO> eventos) {
		this.eventos = eventos;
	}

	@Override
	public String toString() {
		return "NovoUsuarioDTO [email=" + email + ", cpf=" + cpf + ", nome=" + nome + ", senha=" + senha
				+ ", departamento=" + departamento + ", telefone=" + telefone + ", dataIngresso=" + dataIngresso + "]";
	}

	
	
	
	
	
}
