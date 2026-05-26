package com.civa.app.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

import com.civa.app.domain.Bus;
import com.civa.app.dto.BusRequestDto;
import com.civa.app.dto.BusResponseDTO;
import com.civa.app.mapper.BusMapper;
import com.civa.app.service.IBusService;

import lombok.RequiredArgsConstructor;


@CrossOrigin(origins  = "http://localhost:5173")
@RestController
@RequestMapping("/bus")
@RequiredArgsConstructor
public class BusController {
    
    private final IBusService busService;
    private final BusMapper busMapper;

    @GetMapping
    public ResponseEntity<Page<BusResponseDTO>> getAllBuses(
        @RequestParam(required = false)String numberBus,
        @PageableDefault(page = 0, size = 5, sort = "numberBus")Pageable pageable
    ) {
        
        Page<BusResponseDTO> buses = busService.findAll(numberBus, pageable);
        return ResponseEntity.ok(buses);
    }

    @GetMapping("/bus/{id}")
    public ResponseEntity<BusResponseDTO> getBusById(@PathVariable Long id) {
        Bus bus = busService.findById(id);
        return ResponseEntity.ok(busMapper.toBusResponseDTO(bus));
        
    }

    @PostMapping
    public ResponseEntity<BusResponseDTO> createBus(@Valid @RequestBody BusRequestDto busRequestDto) {
        Bus busToSave = busMapper.toEntity(busRequestDto);
        Bus savedBus = busService.save(busToSave);

        BusResponseDTO busResponseDTO = busMapper.toBusResponseDTO(savedBus);
        return new ResponseEntity<>(busResponseDTO, HttpStatus.CREATED);
    
    }

    @PutMapping("/{id}")
    public ResponseEntity<BusResponseDTO> 
    updateBus(@PathVariable Long id, @Valid @RequestBody BusRequestDto busRequestDto){
            Bus busToUpdate = busService.findById(id);
            busMapper.updateBusFromDTO(busRequestDto, busToUpdate);
            Bus updateBus = busService.save(busToUpdate);
            return ResponseEntity.ok(busMapper.toBusResponseDTO(updateBus));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBus(@PathVariable Long id){
        busService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
