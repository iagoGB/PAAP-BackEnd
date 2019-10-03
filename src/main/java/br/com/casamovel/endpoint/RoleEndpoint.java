package br.com.casamovel.endpoint;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.casamovel.model.Role;
import br.com.casamovel.repository.RoleRepository;

@RestController
public class RoleEndpoint {
	@Autowired
	 RoleRepository roleRepository;
	
	@GetMapping("/role")
	public List<Role> listaCategoria() {
		return roleRepository.findAll();
	}
	
	@GetMapping("/role/{id}")
	public Optional<Role> categoriaPorId(@PathVariable(value="id") String id) {
		return roleRepository.findById(id);
	}
	
	@PostMapping("/role")
	public Role salvarProduto(@RequestBody Role role) {
		return roleRepository.save(role);
		
	}
	
}
