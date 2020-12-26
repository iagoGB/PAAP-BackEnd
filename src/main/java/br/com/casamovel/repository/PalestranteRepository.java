package br.com.casamovel.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.casamovel.model.Palestrante;

public interface PalestranteRepository extends JpaRepository<Palestrante, Long>{

	Optional<Palestrante> findByNome(String nome);

}
