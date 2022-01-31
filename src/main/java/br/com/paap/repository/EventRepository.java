package br.com.paap.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.paap.model.Event;

public interface EventRepository extends JpaRepository<Event, Long>{
	@Query("select e from Event e where e.dateTime > :dateTime")
	List<Event> findAllOpen(@Param("dateTime") LocalDateTime dateTime);

	List<Event> findByTitleContainingIgnoreCase(String title);
}
