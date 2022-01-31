package br.com.paap.model;

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

import br.com.paap.dto.user.NewUserDTO;
import br.com.paap.repository.RoleRepository;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "usuario")
public class User implements UserDetails {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Builder.Default
	private String avatar = "https://fcdocente-teste.s3.sa-east-1.amazonaws.com/usuarios/default-avatar.png";
    
	private String siape;
	
	private String cpf;

	private String name;
	
	@NotEmpty
	@Column(unique = true, nullable = false)
	private String email;
	
	private String password;
	
	private String departament;
	
	private String phone;
	
	@Builder.Default
	private Integer workload = 0;

	
	@OneToMany(mappedBy = "user", orphanRemoval = true) // Trocar pelo outro lado que referencia aqui
	@Builder.Default
	private List<EventUser> events = new ArrayList<>();
	
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
	private LocalDate entryDate; 
	
	@Builder.Default
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT-3")
	private LocalDateTime createdAt = LocalDateTime.now();
	
	@Builder.Default
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT-3")
	private LocalDateTime updatedAt = LocalDateTime.now();

	
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
	
	public void parse(NewUserDTO uDto, RoleRepository roleRepository) {
		this.name = uDto.getName();
		this.email = uDto.getEmail();
		this.siape = uDto.getSiape();
		this.password = new BCryptPasswordEncoder().encode(uDto.getPassword());
		this.cpf = uDto.getCpf();
		this.departament = uDto.getDepartament();
		this.entryDate = uDto.getEntryDate();
		this.phone = uDto.getPhone();
		
		Role defaultRole = new Role();
        defaultRole = roleRepository.getOne("ROLE_USER");
        this.roles.add(defaultRole);
	}
	
	@PreUpdate
	public void onUpdate() {
		var now = LocalDateTime.now();
		setUpdatedAt(now);
	}
	
	
	
	
}
