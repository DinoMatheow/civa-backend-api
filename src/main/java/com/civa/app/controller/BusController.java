package com.civa.app.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ResponseEntity<Page<BusResponseDTO>> getAllBuses(
        @RequestParam(required = false)String name,
        @PageableDefault(page = 0, size = 5, sort = "name")Pageable pageable
    ) {
        
        Page<BusResponseDTO> buses = busService.findAll(name, pageable);
        return ResponseEntity.ok(buses);
    }

    @GetMapping("/bus/{id}")
    public ResponseEntity<BusResponseDTO> getBusById(@PathVariable Long id) {
        Bus bus = busService.findById(id);
        return ResponseEntity.ok(busMapper.toBusResponseDTO(bus));
        
    }


}
