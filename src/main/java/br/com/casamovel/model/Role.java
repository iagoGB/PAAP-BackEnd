package br.com.casamovel.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.springframework.security.core.GrantedAuthority;
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
public class Role implements GrantedAuthority {
	private static final long serialVersionUID = 1L;
	
	@Id
	private String roleName;
	@ManyToMany (mappedBy = "roles", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}) //antes estava por role
	private List<Usuario> usuarios;

	@Override
	public String toString() {
		return "Role [roleName=" + roleName + "]";
	}
	@Override
	public String getAuthority() {
		return this.roleName;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
        @JsonBackReference
	public List<Usuario> getUsuarios() {
		return usuarios;
	}
	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}
	public Role(String roleName, List<Usuario> usuarios) {
		super();
		this.roleName = roleName;
		this.usuarios = usuarios;
	}
	public Role() {
	}
	
	

}
