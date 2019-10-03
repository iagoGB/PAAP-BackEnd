package br.com.casamovel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.casamovel.model.Evento;

public interface EventoRepository extends JpaRepository<Evento, Long>{

}
