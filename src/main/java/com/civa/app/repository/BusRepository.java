package com.civa.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.civa.app.domain.Bus;

@Repository
public interface BusRepository  extends JpaRepository<Bus, Long> {
    

}   
