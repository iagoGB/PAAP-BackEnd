package br.com.casamovel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.casamovel.model.EventoUsuario;
import br.com.casamovel.model.EventoUsuarioID;

public interface EventoUsuarioRepository extends JpaRepository<EventoUsuario, EventoUsuarioID> {
    
}