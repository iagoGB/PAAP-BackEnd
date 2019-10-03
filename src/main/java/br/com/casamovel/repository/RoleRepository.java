package br.com.casamovel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.casamovel.model.Role;

public interface RoleRepository extends JpaRepository<Role, String>{

}
