package br.com.casamovel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.casamovel.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{
    
       
}
