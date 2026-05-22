package com.civa.app.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.civa.app.domain.Bus;
import com.civa.app.dto.BusResponseDTO;


public interface IBusService {
    
    Page<BusResponseDTO> findAll(String numberBus, Pageable pageable);
    Bus findById(Long id);
    Bus save(Bus bus);
    void deleteById(Long id); 
}
