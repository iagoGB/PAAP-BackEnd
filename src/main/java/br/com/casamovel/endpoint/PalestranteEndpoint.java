package br.com.casamovel.endpoint;

import java.net.URI;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.casamovel.dto.NovoPalestranteDTO;
import br.com.casamovel.dto.PalestranteDTO;
import br.com.casamovel.model.Palestrante;
import br.com.casamovel.repository.PalestranteRepository;

@RestController
@RequestMapping("/palestrante")
public class PalestranteEndpoint {
	@Autowired
	PalestranteRepository palestranteRepository;
	
	@GetMapping("/{id}")
	public Optional<Palestrante> usuarioPorId(@PathVariable(value="id") Long id) {
		return palestranteRepository.findById(id);
	}
	

	@PostMapping
	public ResponseEntity<PalestranteDTO> salvarUsuario(@RequestBody @Valid NovoPalestranteDTO novoPalestranteDTO,
			UriComponentsBuilder uriBuilder) throws Exception {
		Palestrante novoPalestranteModel = new Palestrante();
		novoPalestranteModel.parse(novoPalestranteDTO);
		// Salvar
		palestranteRepository.save(novoPalestranteModel);
		PalestranteDTO palestranteDTO = PalestranteDTO.parse(novoPalestranteModel);
		// Caminho do novo recurso criado
		URI uri = uriBuilder.path("/palestrante/{id}").buildAndExpand(novoPalestranteModel.getId()).toUri();
		return ResponseEntity.created(uri).body(palestranteDTO);

	} 
	
}
