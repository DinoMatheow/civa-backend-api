package com.civa.app.service;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.civa.app.domain.Bus;
import com.civa.app.dto.BusRequestDto;
import com.civa.app.dto.BusResponseDTO;


public interface IBusService {
    
    Page<BusResponseDTO> findAll(String numberBus, Pageable pageable);
    Bus findById(Long id);
    Bus save(BusRequestDto busRequestDto);
    Bus update(Long id, BusRequestDto busRequestDto);
    void deleteById(Long id); 
    List<Bus> getAllBusAndTheirDetailsProblematic();

    List<Bus> getAllBusAndTheirDetailsOptimizeWithJoinFetch();
}
