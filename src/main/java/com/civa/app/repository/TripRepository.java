package com.civa.app.repository;

import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.civa.app.domain.Trip;
import com.civa.app.domain.TripStatusEnum;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    

    @Override
    @EntityGraph(attributePaths = {"origin", "destination", "bus","bus.category"})
    List<Trip> findAll();
    
    @EntityGraph(attributePaths = {"origin", "destination", "bus", "bus.category"})
    Optional<Trip> findByTripCode(String tripCode);

    @EntityGraph(attributePaths = {"origin", "destination", "bus", "bus.category"})
    @Query("""
        SELECT t FROM Trip t
        WHERE t.origin.id = :originId
          AND t.destination.id = :destinationId
          AND t.departureTime >= :startTime
          AND t.departureTime < :endTime
          AND t.status = :status
        """)
    Page<Trip> search(@Param("originId") Long originId,
                      @Param("destinationId") Long destinationId,
                      @Param("startTime") LocalDateTime startTime,
                      @Param("endTime") LocalDateTime endTime,
                      @Param("status") TripStatusEnum status,
                      Pageable pageable);


}