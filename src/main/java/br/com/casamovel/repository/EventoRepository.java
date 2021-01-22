package br.com.casamovel.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;

import br.com.casamovel.model.Evento;

public interface EventoRepository extends JpaRepository<Evento, Long>{
	List<Evento> findByEstaAberto(Boolean estaAberto);
	
	@Query("select e from Evento e where e.dataHorario > :dataHorario")
	List<Evento> findAllOpen(@Param("dataHorario") LocalDateTime dataHorario);
}
