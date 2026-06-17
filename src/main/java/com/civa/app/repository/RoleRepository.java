package com.civa.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.civa.app.domain.Role;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{

}