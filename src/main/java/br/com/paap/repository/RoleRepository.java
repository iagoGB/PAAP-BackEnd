package br.com.paap.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.paap.model.Role;

public interface RoleRepository extends JpaRepository<Role, String>{

}
