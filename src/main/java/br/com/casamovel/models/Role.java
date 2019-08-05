package br.com.casamovel.models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.springframework.security.core.GrantedAuthority;
@Entity
public class Role implements GrantedAuthority{
	private static final long serialVersionUID = 1L;
	
	@Id
	private String roleName;
	@ManyToMany (mappedBy = "roles")
	private List<Usuario> usuarios;

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
	public List<Usuario> getUsuarios() {
		return usuarios;
	}
	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}
	
	

}
