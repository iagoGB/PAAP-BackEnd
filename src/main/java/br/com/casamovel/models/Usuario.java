package br.com.casamovel.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.io.Serializable;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;


@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "usuario_id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name="usuario")
public class Usuario implements Serializable,UserDetails{
	private static final long serialVersionUID = 1L;
	
	@Override
	public String toString() {
		return "Usuario [usuario_id=" + usuario_id + ", cpf=" + cpf + ", nome=" + nome + ", email=" + email + ", senha="
				+ senha + ", departamento=" + departamento + ", telefone=" + telefone + ", roles=" + roles
				+ ", carga_horaria=" + carga_horaria + ", data_ingresso=" + data_ingresso + ", criado_em=" + criado_em
				+ ", atualizado_em=" + atualizado_em + ", eventos=" + eventos + "]";
	}
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long usuario_id;
	
    private String avatar; // Caminho para a foto
    
	private long cpf;
	
//	@Column(name = "carga_horaria", nullable = false, columnDefinition = "double default 0")
	private String nome;
	
	@NotEmpty
	@Column(unique = true)
	private String email;
	
	private String senha;
	
	private String departamento;
	
	private String telefone;
	
	@OneToMany(mappedBy = "evento_id")
	private List<EventoUsuario> eventos;
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(
		name = "usuario_role",
		joinColumns = @JoinColumn(name="usuario_id", referencedColumnName = "usuario_id"),
		inverseJoinColumns = @JoinColumn(name="role_id",referencedColumnName = "roleName")
	)
	
    @JsonBackReference
	private List<Role> roles;
	
	
	private Time carga_horaria;
	
	@JsonFormat(pattern="yyyy-MM-dd", timezone="GMT-3")
	private Date data_ingresso;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT-3")
	private Date criado_em;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT-3")
	private Date atualizado_em;
	
	
	
	public Usuario() {
		
	}
	
	public long getUsuario_id() {
		return usuario_id;
	} 
	
	public String getAvatar() {
		return avatar;
	}
	
	public void setAvatar(String avatar) {
		this.avatar = avatar;
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
	
	public List<Role> getRoles() {
		return roles;
	}
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	public List<EventoUsuario> getEventos() {
		return eventos;
	}
	public void setEventos(List<EventoUsuario> eventos) {
		this.eventos = eventos;
	}
	public void setUsuario_id(long usuario_id) {
		this.usuario_id = usuario_id;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.roles;
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
	
    //Retirada id e lista de roles, verificar depois;
	public Usuario( String avatar,long cpf, String nome, @NotEmpty String email, String senha, String departamento,
			String telefone,List<Role>roles, Time carga_horaria, Date data_ingresso, Date criado_em,
			Date atualizado_em, List<EventoUsuario> eventos) {
		super();
        this.avatar = avatar;
		//this.usuario_id = usuario_id;
		this.cpf = cpf;
		this.nome = nome;
		this.email = email;
		this.senha = senha;
		this.departamento = departamento;
		this.telefone = telefone;
		this.roles = roles;
		this.carga_horaria = carga_horaria;
		this.data_ingresso = data_ingresso;
		this.criado_em = criado_em;
		this.atualizado_em = atualizado_em;
		this.eventos = eventos;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((atualizado_em == null) ? 0 : atualizado_em.hashCode());
		result = prime * result + ((avatar == null) ? 0 : avatar.hashCode());
		result = prime * result + ((carga_horaria == null) ? 0 : carga_horaria.hashCode());
		result = prime * result + (int) (cpf ^ (cpf >>> 32));
		result = prime * result + ((criado_em == null) ? 0 : criado_em.hashCode());
		result = prime * result + ((data_ingresso == null) ? 0 : data_ingresso.hashCode());
		result = prime * result + ((departamento == null) ? 0 : departamento.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((eventos == null) ? 0 : eventos.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((roles == null) ? 0 : roles.hashCode());
		result = prime * result + ((senha == null) ? 0 : senha.hashCode());
		result = prime * result + ((telefone == null) ? 0 : telefone.hashCode());
		result = prime * result + (int) (usuario_id ^ (usuario_id >>> 32));
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		if (atualizado_em == null) {
			if (other.atualizado_em != null)
				return false;
		} else if (!atualizado_em.equals(other.atualizado_em))
			return false;
		if (avatar == null) {
			if (other.avatar != null)
				return false;
		} else if (!avatar.equals(other.avatar))
			return false;
		if (carga_horaria == null) {
			if (other.carga_horaria != null)
				return false;
		} else if (!carga_horaria.equals(other.carga_horaria))
			return false;
		if (cpf != other.cpf)
			return false;
		if (criado_em == null) {
			if (other.criado_em != null)
				return false;
		} else if (!criado_em.equals(other.criado_em))
			return false;
		if (data_ingresso == null) {
			if (other.data_ingresso != null)
				return false;
		} else if (!data_ingresso.equals(other.data_ingresso))
			return false;
		if (departamento == null) {
			if (other.departamento != null)
				return false;
		} else if (!departamento.equals(other.departamento))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (eventos == null) {
			if (other.eventos != null)
				return false;
		} else if (!eventos.equals(other.eventos))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (roles == null) {
			if (other.roles != null)
				return false;
		} else if (!roles.equals(other.roles))
			return false;
		if (senha == null) {
			if (other.senha != null)
				return false;
		} else if (!senha.equals(other.senha))
			return false;
		if (telefone == null) {
			if (other.telefone != null)
				return false;
		} else if (!telefone.equals(other.telefone))
			return false;
		if (usuario_id != other.usuario_id)
			return false;
		return true;
	}
	
	
}
