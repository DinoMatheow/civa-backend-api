package com.civa.app.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.civa.app.domain.Bus;
import com.civa.app.dto.BusResponseDTO;
import com.civa.app.mapper.BusMapper;
import com.civa.app.service.IBusService;

import lombok.RequiredArgsConstructor;



@CrossOrigin(origins  = "http://localhost:5173")
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class BusController {
    
    private final IBusService busService;
    private final BusMapper busMapper;

    @GetMapping("/bus")
    public List<BusResponseDTO> getAllBuses() {
        List<Bus> buses = busService.findAll();
        return busMapper.toBusResponseDTOList(buses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BusResponseDTO> getBusById(@PathVariable Long id) {
        Bus bus = busService.findById(id);
        return ResponseEntity.ok(busMapper.toBusResponseDTO(bus));
        
    }


}
