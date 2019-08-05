package br.com.casamovel.models;

import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name="usuario")
public class Usuario implements Serializable,UserDetails{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long usuario_id;
	private long cpf;
//	@Column(name = "carga_horaria", nullable = false, columnDefinition = "double default 0")
	private String nome;
	private String email;
	private String senha;
	private String departamento;
	private String telefone;
	@ManyToMany
	@JoinTable(
		name = "usuario_role",
		joinColumns = @JoinColumn(name="usuario_id", referencedColumnName = "usuario_id"),
		inverseJoinColumns = @JoinColumn(name="role_id",referencedColumnName = "roleName")
	)
	private List<Role> roles;
	
	public List<Evento> getEventos() {
		return eventos;
	}
	public void setEventos(List<Evento> eventos) {
		this.eventos = eventos;
	}
	public void setUsuario_id(long usuario_id) {
		this.usuario_id = usuario_id;
	}
	private Time carga_horaria;
	
	@JsonFormat(pattern="yyyy-MM-dd", timezone="GMT-3")
	private Date data_ingresso;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT-3")
	private Date criado_em;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT-3")
	private Date atualizado_em;
	
	@ManyToMany(mappedBy="usuarios", cascade = CascadeType.ALL)
	List<Evento> eventos = new ArrayList<Evento>();
	
	public Usuario() {
		
	}
	public long getUsuario_id() {
		return usuario_id;
	}
	public long getCpf() {
		return cpf;
	}
	public void setCpf(long cpf) {
		this.cpf = cpf;
	}
	public Time getCarga_horaria() {
		return carga_horaria;
	}
	public void setCarga_horaria(Time carga_horaria) {
		this.carga_horaria = carga_horaria;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	public Date getData_ingresso() {
		return data_ingresso;
	}
	public void setData_ingresso(Date data_ingresso) {
		this.data_ingresso = data_ingresso;
	}
	public Date getCriado_em() {
		return criado_em;
	}
	public void setCriado_em(Date criado_em) {
		this.criado_em = criado_em;
	}
	public Date getAtualizado_em() {
		return atualizado_em;
	}
	public void setAtualizado_em(Date atualizado_em) {
		this.atualizado_em = atualizado_em;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}
	@Override
	public String getPassword() {
		//Método sobrescrito da classe do Spring UserDetails
		return this.senha;
	}
	@Override
	public String getUsername() {
		//Método sobrescrito da classe do Spring UserDetails
		return this.email;
	}
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	@Override
	public boolean isEnabled() {
		
		return true;
	}
	public List<Role> getRoles() {
		return roles;
	}
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
}
