package com.civa.app.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    

    @Override
    @EntityGraph(attributePaths = {"origin", "destination", "bus"})
    List<Trip> findAll();

    @EntityGraph(attributePaths = {"origin", "destination", "bus"})
    Optional<Trip> findByTripCode(String tripCode);

    @EntityGraph(attributePaths = {"origin", "destination", "bus"})
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
                      @Param("status") TripStatus status,
                      Pageable pageable);


}