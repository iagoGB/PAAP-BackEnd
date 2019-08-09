package br.com.casamovel.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.casamovel.models.Role;

public interface RoleRepository extends JpaRepository<Role, String>{

}
