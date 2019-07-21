package br.com.casamovel.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.casamovel.models.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long>{
	
}
