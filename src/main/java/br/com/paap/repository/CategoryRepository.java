package br.com.paap.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.paap.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{
    
       
}
