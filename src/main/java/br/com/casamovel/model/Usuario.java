package br.com.casamovel.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.com.casamovel.dto.usuario.NovoUsuarioDTO;
import br.com.casamovel.repository.RoleRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
// @JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name="usuario")
public class Usuario implements UserDetails{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
    private String avatar = "../assets/images/default_avatar.png";
    
	private long cpf;
	
//	@Column(name = "carga_horaria", nullable = false, columnDefinition = "double default 0")
	private String nome;
	
	@NotEmpty
	@Column(unique = true, nullable = false)
	private String email;
	
	private String senha;
	
	private String departamento;
	
	private String telefone;
	
	@OneToMany(mappedBy = "evento_id")
	private List<EventoUsuario> eventos = new ArrayList<>();
	
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
	@JoinTable(
		name = "usuario_role",
		joinColumns = @JoinColumn(
				name="usuario_id",
				referencedColumnName = "id",
				foreignKey = @ForeignKey(name = "fk_usuario_id")
		),
		inverseJoinColumns = @JoinColumn(
				name="role_id",
				referencedColumnName = "roleName",
				foreignKey = @ForeignKey(name = "fk_role_id")
		)
	)
	@JsonBackReference
	private List<Role> roles = new ArrayList<>();
	
	
	private LocalTime cargaHoraria = LocalTime.of(0, 0);
	
	@JsonFormat(pattern="yyyy-MM-dd", timezone="GMT-3")
	private LocalDate dataIngresso; 
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT-3")
	private LocalDateTime criadoEm = LocalDateTime.now();
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT-3")
	private LocalDateTime atualizadoEm = LocalDateTime.now();
	
	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
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


	public LocalTime getCargaHoraria() {
		return cargaHoraria;
	}


	public void setCargaHoraria(LocalTime cargaHoraria) {
		this.cargaHoraria = cargaHoraria;
	}


	public LocalDate getDataIngresso() {
		return dataIngresso;
	}


	public void setDataIngresso(LocalDate dataIngresso) {
		this.dataIngresso = dataIngresso;
	}


	public LocalDateTime getCriadoEm() {
		return criadoEm;
	}


	public void setCriadoEm(LocalDateTime criadoEm) {
		this.criadoEm = criadoEm;
	}


	public LocalDateTime getAtualizadoEm() {
		return atualizadoEm;
	}


	public void setAtualizadoEm(LocalDateTime atualizadoEm) {
		this.atualizadoEm = atualizadoEm;
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
		this.id = usuario_id;
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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((atualizadoEm == null) ? 0 : atualizadoEm.hashCode());
		result = prime * result + ((avatar == null) ? 0 : avatar.hashCode());
		result = prime * result + ((cargaHoraria == null) ? 0 : cargaHoraria.hashCode());
		result = prime * result + (int) (cpf ^ (cpf >>> 32));
		result = prime * result + ((criadoEm == null) ? 0 : criadoEm.hashCode());
		result = prime * result + ((dataIngresso == null) ? 0 : dataIngresso.hashCode());
		result = prime * result + ((departamento == null) ? 0 : departamento.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((eventos == null) ? 0 : eventos.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((roles == null) ? 0 : roles.hashCode());
		result = prime * result + ((senha == null) ? 0 : senha.hashCode());
		result = prime * result + ((telefone == null) ? 0 : telefone.hashCode());
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
		if (atualizadoEm == null) {
			if (other.atualizadoEm != null)
				return false;
		} else if (!atualizadoEm.equals(other.atualizadoEm))
			return false;
		if (avatar == null) {
			if (other.avatar != null)
				return false;
		} else if (!avatar.equals(other.avatar))
			return false;
		if (cargaHoraria == null) {
			if (other.cargaHoraria != null)
				return false;
		} else if (!cargaHoraria.equals(other.cargaHoraria))
			return false;
		if (cpf != other.cpf)
			return false;
		if (criadoEm == null) {
			if (other.criadoEm != null)
				return false;
		} else if (!criadoEm.equals(other.criadoEm))
			return false;
		if (dataIngresso == null) {
			if (other.dataIngresso != null)
				return false;
		} else if (!dataIngresso.equals(other.dataIngresso))
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
		if (id != other.id)
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
		return true;
	}
	
	public void parse(NovoUsuarioDTO uDto, RoleRepository roleRepository) {
		this.nome = uDto.getNome();
		this.email = uDto.getEmail();
		this.senha = new BCryptPasswordEncoder().encode(uDto.getSenha());
		this.cpf = uDto.getCpf();
		this.departamento = uDto.getDepartamento();
		this.dataIngresso = uDto.getData_ingresso();
		this.telefone = uDto.getTelefone();
		
		Role roleDefault = new Role();
        roleDefault = roleRepository.getOne("ROLE_USER");
        this.roles.add(roleDefault);	
	}
	
	@PreUpdate
	public void commitarAtualizacao() {
		LocalDateTime now = LocalDateTime.now();
		System.out.println("------------------------ATUALIZAÇÃO COMMITADA ÀS "+ now.toString()+" -------------------------------------------------------");
		setAtualizadoEm(now);
	}
	
	
	
	
}
