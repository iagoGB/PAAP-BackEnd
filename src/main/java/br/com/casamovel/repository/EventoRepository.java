package br.com.casamovel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.casamovel.model.Evento;

public interface EventoRepository extends JpaRepository<Evento, Long>{
	List<Evento> findByEstaAberto(Boolean estaAberto);
}
