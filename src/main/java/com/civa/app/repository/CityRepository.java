package com.civa.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.civa.app.domain.City;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
}