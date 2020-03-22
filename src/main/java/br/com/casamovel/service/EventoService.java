/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.casamovel.service;

import br.com.casamovel.dto.evento.DetalhesEventoDTO;
import br.com.casamovel.dto.evento.NovoEventoDTO;
import br.com.casamovel.endpoint.EventoEndpoint;
import br.com.casamovel.model.Categoria;
import br.com.casamovel.model.Evento;
import br.com.casamovel.repository.CategoriaRepository;
import br.com.casamovel.repository.EventoRepository;
import br.com.casamovel.repository.PalestranteRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author iago.barreto
 */
@Service
public class EventoService {
    
    @Autowired
    EventoRepository eventoRepository;

    @Autowired
    CategoriaRepository categoriaRepository;
    
    @Autowired
    PalestranteRepository palestranteRepository;
    
    public List<DetalhesEventoDTO> listarEventos(){
    	List<DetalhesEventoDTO> result = new ArrayList<DetalhesEventoDTO>();
    	//List<Evento> findAll = eventoRepository.findAll();
    	List<Evento> findAll = eventoRepository.findByEstaAberto(true);
    	findAll.forEach(evento -> result.add(DetalhesEventoDTO.parse(evento)));
        return result;
    }
    
    public Evento salvarEvento(NovoEventoDTO novoEventoDTO) {
    	Evento result = null;
        try 
        {
            System.out.println("evento que chegou no service: "+ novoEventoDTO);
            Categoria c = null;
            Optional<Categoria> optC;
            System.out.println("categoria Rep: "+ categoriaRepository.findById(novoEventoDTO.getCategoria()).toString());
            optC = categoriaRepository.findById(novoEventoDTO.getCategoria());
            if (optC.isPresent()){
                c = optC.get();
            }
                       
            Evento novoEventoModel = new Evento();
            novoEventoModel.parse(novoEventoDTO, c, palestranteRepository);
            result = eventoRepository.save(novoEventoModel);
            
        } catch (Exception e) 
        {
            Logger.getLogger(EventoEndpoint.class.getName()).log(Level.SEVERE, null, e);
            System.out.println("Erro ao criar evento, triste kk: "+ e);
        }
        return result;
    }
    
    public boolean deletarEvento(long id) {
        boolean deletou = false;
        try {
            this.eventoRepository.deleteById(id);
            deletou = true;
        } catch (Exception e) {
            System.out.println("Erro ao deletar"+ e);
        }
        return deletou;
    }

	public DetalhesEventoDTO findById(Long id) {
		Optional<Evento> result = eventoRepository.findById(id);
		if (result.isPresent()) {
			Evento evento = result.get();
			return DetalhesEventoDTO.parse(evento);
		}
		return null;
	}
}
