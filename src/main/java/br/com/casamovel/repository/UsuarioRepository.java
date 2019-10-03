package br.com.casamovel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.casamovel.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
	Usuario findByEmail(String email);
}
