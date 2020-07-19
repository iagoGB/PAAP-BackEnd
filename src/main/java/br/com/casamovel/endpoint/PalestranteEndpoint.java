package br.com.casamovel.endpoint;

import java.util.List;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.casamovel.dto.palestrante.DetalhePalestranteDTO;
import br.com.casamovel.dto.palestrante.NovoPalestranteDTO;
import br.com.casamovel.dto.palestrante.PalestranteDTO;
import br.com.casamovel.service.PalestranteService;

@RestController
@RequestMapping("/palestrante")
public class PalestranteEndpoint {
	@Autowired
	PalestranteService palestranteService;
	
	@GetMapping
	public List<PalestranteDTO> getAll() 
	{
		 return palestranteService.getAll();
	}
	
	
	@GetMapping("/{id}")
	public DetalhePalestranteDTO usuarioPorId(@PathVariable(value="id") Long id) 
	{
		return palestranteService.findById(id);
	}
	

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,  produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PalestranteDTO> salvarUsuario
	(
		@RequestParam("imagem") MultipartFile image,
		@RequestParam("descricao") @Valid String desc,
		@RequestParam("nome") @Valid String nome,

		UriComponentsBuilder uriBuilder
	) 
	throws Exception 
	{
		var novoPalestranteDTO = new NovoPalestranteDTO(nome, desc);
		return palestranteService.save(novoPalestranteDTO, image, uriBuilder);
	} 
	
}
