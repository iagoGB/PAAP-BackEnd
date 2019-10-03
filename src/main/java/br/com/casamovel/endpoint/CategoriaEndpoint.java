package br.com.casamovel.endpoint;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.casamovel.model.Categoria;
import br.com.casamovel.repository.CategoriaRepository;

@RestController
public class CategoriaEndpoint {
	@Autowired
	 CategoriaRepository categoriaRepository;
	
	@GetMapping("/categoria")
	public List<Categoria> listaCategoria() {
		return categoriaRepository.findAll();
	}
	
	@GetMapping("/categoria/{id}")
	public Optional<Categoria> categoriaPorId(@PathVariable(value="id") Long id) {
		return categoriaRepository.findById(id);
	}
	
	@PostMapping("/categoria")
	public Categoria salvarProduto(@RequestBody Categoria categoria) {
		return categoriaRepository.save(categoria);
		
	}
	
}
