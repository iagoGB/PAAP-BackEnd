package br.com.casamovel.endpoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.casamovel.dto.categoria.CategoriaDTO;
import br.com.casamovel.model.Category;
import br.com.casamovel.repository.CategoryRepository;

@RestController
@RequestMapping("/category")
public class CategoryEndpoint {
	@Autowired
	 CategoryRepository categoriaRepository;
	
	@GetMapping
	public List<CategoriaDTO> findAll() {
		//return categoriaRepository.findAll();
		List<CategoriaDTO> listaCategoriaDTO = new ArrayList<>();
		 List<Category> listaCategorias = categoriaRepository.findAll();
		 listaCategorias.forEach( c -> 
		 {
			 CategoriaDTO nc = new CategoriaDTO(c.getId(),c.getName());
			 listaCategoriaDTO.add(nc);
		 });
		 
		 return listaCategoriaDTO;
	}
	
	@GetMapping("/{id}")
	public Optional<Category> findById(@PathVariable(value="id") Long id) {
		return categoriaRepository.findById(id);
	}
	
	@PostMapping
	public Category save(@RequestBody Category categoria) {
		return categoriaRepository.save(categoria);
		
	}
	
}
