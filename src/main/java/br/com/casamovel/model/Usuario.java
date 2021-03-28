package br.com.casamovel.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.com.casamovel.dto.usuario.NovoUsuarioDTO;
import br.com.casamovel.repository.RoleRepository;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name="usuario")
public class Usuario implements UserDetails{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Builder.Default
    private String avatar = "../assets/images/default_avatar.png";
    
	private long cpf;

	private String nome;
	
	@NotEmpty
	@Column(unique = true, nullable = false)
	private String email;
	
	private String password;
	
	private String departamento;
	
	private String telefone;
	
	@Builder.Default
	private Integer cargaHoraria = 0;

	
	@OneToMany(mappedBy = "usuarioID") // Trocar pelo outro lado que referencia aqui
	@Builder.Default
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
	@Builder.Default
	private List<Role> roles = new ArrayList<>();

	
	@JsonFormat(pattern="yyyy-MM-dd", timezone="GMT-3")
	private LocalDate dataIngresso; 
	
	@Builder.Default
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT-3")
	private LocalDateTime criadoEm = LocalDateTime.now();
	
	@Builder.Default
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT-3")
	private LocalDateTime atualizadoEm = LocalDateTime.now();

	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.roles;
	}
	
	@Override
	public String getPassword() {
		//Método sobrescrito da classe do Spring UserDetails
		return this.password;
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
	
	public void parse(NovoUsuarioDTO uDto, RoleRepository roleRepository) {
		this.nome = uDto.getNome();
		this.email = uDto.getEmail();
		this.password = new BCryptPasswordEncoder().encode(uDto.getPassword());
		this.cpf = uDto.getCpf();
		this.departamento = uDto.getDepartamento();
		this.dataIngresso = uDto.getData_ingresso();
		this.telefone = uDto.getTelefone();
		
		Role defaultRole = new Role();
        defaultRole = roleRepository.getOne("ROLE_USER");
        this.roles.add(defaultRole);
	}
	
	@PreUpdate
	public void commitarAtualizacao() {
		LocalDateTime now = LocalDateTime.now();
		System.out.println("------------------------ATUALIZAÇÃO COMMITADA ÀS "+ now.toString()+" -------------------------------------------------------");
		setAtualizadoEm(now);
	}
	
	
	
	
}
