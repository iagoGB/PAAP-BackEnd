package br.com.casamovel.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.casamovel.models.Evento;

public interface EventoRepository extends JpaRepository<Evento, Long>{

}
