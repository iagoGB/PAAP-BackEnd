package br.com.paap.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.paap.model.EventUser;
import br.com.paap.model.EventUserID;

public interface EventUserRepository extends JpaRepository<EventUser, EventUserID> {
    List<EventUser> findByUserId(Long id);
}