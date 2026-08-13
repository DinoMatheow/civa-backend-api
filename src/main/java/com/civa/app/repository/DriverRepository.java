package com.civa.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.civa.app.domain.Driver;

public interface DriverRepository  extends JpaRepository<Driver, Long>{
    
Optional<Driver> findByEmail(String email);
Boolean existsByEmail(String email); 

}
