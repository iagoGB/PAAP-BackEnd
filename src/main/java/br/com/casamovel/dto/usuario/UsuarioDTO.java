package br.com.casamovel.dto.usuario;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

import br.com.casamovel.dto.evento.EventoDTO;
import br.com.casamovel.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class UsuarioDTO {
	public Long id;
	public String email;
	public Long cpf;
	public String nome;
	public LocalTime carga_horaria;
	public String departamento;
	public String telefone;
	public LocalDate data_ingresso;
	public String avatar;
	public List<EventoDTO> eventos = new ArrayList<>();
	
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
		usuario.getEventos().forEach(relacaoEventoUsuario ->{ 
			this.eventos.add(new EventoDTO(relacaoEventoUsuario.getEvento_id()));
		});
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
