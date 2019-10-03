package br.com.casamovel.usuario;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.casamovel.model.Role;
import br.com.casamovel.model.Usuario;
import br.com.casamovel.repository.RoleRepository;
import br.com.casamovel.repository.UsuarioRepository;


public class AdicionarUsuarioTeste {
	

	public static void main(String[] args) {
		
		Role role1 = new Role();
		
		List<Role> l = new ArrayList<Role>();
		role1 = rr.getOne("ROLE_ADMIN");	
		l.add(role1);
		Usuario u = new Usuario();
		u.setCpf(9999);
		u.setNome("Testando");
		u.setRoles(l);
		System.out.println(u.toString());
		System.out.println(ur.save(u));

	}
	
	@Autowired
	static
	UsuarioRepository ur;
	@Autowired
	static
	RoleRepository rr;

}



