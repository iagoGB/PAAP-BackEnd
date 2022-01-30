package br.com.paap.endpoint;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.paap.dto.category.CategoryDTO;
import br.com.paap.model.Category;
import br.com.paap.repository.CategoryRepository;

@RestController
@RequestMapping("/category")
public class CategoryEndpoint {
	@Autowired
	 CategoryRepository categoryRepository;
	
	@GetMapping
	public List<CategoryDTO> findAll() {
		 return categoryRepository.findAll().stream().map(c ->{
				return CategoryDTO.builder()
						.id(c.getId())	
						.name(c.getName())
						.urlImage(c.getUrlImage())
						.build();
			}).collect(Collectors.toList());	
	}
	
	@GetMapping("/{id}")
	public Optional<Category> findById(@PathVariable(value="id") Long id) {
		return categoryRepository.findById(id);
	}
	
	@PostMapping
	public Category save(@RequestBody Category categoria) {
		return categoryRepository.save(categoria);
		
	}
	
}
