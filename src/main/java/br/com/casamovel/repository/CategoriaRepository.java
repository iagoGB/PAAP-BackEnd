package br.com.casamovel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.casamovel.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long>{
    
       
}
