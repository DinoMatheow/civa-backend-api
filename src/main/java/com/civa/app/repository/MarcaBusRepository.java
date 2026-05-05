package com.civa.app.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.civa.app.domain.MarcaBus;

@Repository
public interface MarcaBusRepository extends JpaRepository<MarcaBus, Long> {
    
}
