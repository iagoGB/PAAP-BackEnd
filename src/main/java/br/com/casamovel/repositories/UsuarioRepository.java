package br.com.casamovel.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.casamovel.models.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

}
