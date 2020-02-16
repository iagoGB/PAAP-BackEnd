package br.com.casamovel.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.casamovel.dto.palestrante.DetalhePalestranteDTO;
import br.com.casamovel.dto.palestrante.NovoPalestranteDTO;
import br.com.casamovel.dto.palestrante.PalestranteDTO;
import br.com.casamovel.model.Palestrante;
import br.com.casamovel.repository.PalestranteRepository;

@Service
public class PalestranteService {
	@Autowired
	PalestranteRepository palestranteRepository;
	
	public List<PalestranteDTO> getAll()
	{
		
		List<Palestrante> palestranteModel = palestranteRepository.findAll();
		List<PalestranteDTO> palestrantesDTOList = new ArrayList<>();
		palestranteModel.forEach(p ->  
		{
			PalestranteDTO pdto;
		 	pdto = new PalestranteDTO(p.getId(), p.getNome(), p.getDescricao(),p.getFoto());
		 	palestrantesDTOList.add(pdto);
		 	
		});
		return palestrantesDTOList;
	}

	public DetalhePalestranteDTO findById(Long id)
	{
		
		Optional<Palestrante> result = palestranteRepository.findById(id);
		
		if (result.isPresent())
		{
			
			Palestrante palestranteModel = null;
            palestranteModel = result.get();
            return DetalhePalestranteDTO.parse(palestranteModel);
        
        }
		
		return null;
	}
	
	public ResponseEntity<PalestranteDTO> save
	(
		NovoPalestranteDTO novoPalestranteDTO, 
		UriComponentsBuilder uriBuilder
	)
	{
		
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
