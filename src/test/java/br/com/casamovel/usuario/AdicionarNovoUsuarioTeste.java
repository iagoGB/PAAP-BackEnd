package br.com.casamovel.usuario;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.casamovel.models.Role;
import br.com.casamovel.models.Usuario;
import br.com.casamovel.repositories.RoleRepository;
import br.com.casamovel.repositories.UsuarioRepository;

public class AdicionarNovoUsuarioTeste {
	@Autowired
	UsuarioRepository ur;
	@Autowired
	RoleRepository rr;
	
	@Test
	public void test() {
		List<Role> l = new ArrayList<Role>();
		Role role1 = rr.getOne("ROLE_ADMIN");	
		l.add(role1);
		Usuario u = new Usuario();
		u.setCpf(9999);
		u.setNome("Testando");
		u.setRoles(l);
		System.out.println(u.toString());
		assertEquals(Usuario.class, ur.save(u));
	}

}
