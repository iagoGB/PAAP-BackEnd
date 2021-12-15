package br.com.casamovel.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import br.com.casamovel.model.EventUser;
import br.com.casamovel.model.EventUserID;

public interface EventUserRepository extends JpaRepository<EventUser, EventUserID> {
    List<EventUser> findByUserId(Long id);
}