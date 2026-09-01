        package com.civa.app.repository;

        import java.util.List;
        import java.util.Optional;

        import org.springframework.data.domain.Page;
        import org.springframework.data.jpa.repository.JpaRepository;
        import org.springframework.data.jpa.repository.Query;
        import org.springframework.stereotype.Repository;

        import com.civa.app.domain.Bus;

        @Repository
        public interface BusRepository  extends JpaRepository<Bus, Long> {
                Page<Bus> findByNumberBusContainingIgnoreCase(String numberBus,  org.springframework.data.domain.Pageable pageable);




                @Query("Select  e From Bus e LEFT JOIN FETCH e.category LEFT JOIN FETCH e.drivers")
                List<Bus> findAllWithCategoryAndDrivers();

                @Query("Select e FROM Bus e LEFT JOIN FETCH e.category LEFT JOIN FETCH e.drivers WHERE e.id = :id")
                Optional<Bus> findByIdWithCategoryAndDrivers(Long id);
        }   
        
