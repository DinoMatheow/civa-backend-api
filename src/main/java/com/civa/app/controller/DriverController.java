package com.civa.app.controller;

import org.springframework.web.bind.annotation.RestController;

import com.civa.app.domain.Driver;
import com.civa.app.dto.DriverResponseDto;
import com.civa.app.mapper.DriverMapper;
import com.civa.app.service.DriverService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;





@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/drivers")
public class DriverController {
    private final DriverService driverService;
    private final DriverMapper driverMapper;


    @GetMapping()
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<List<DriverResponseDto>> getAllDrivers() {
        List<Driver> drivers = driverService.findAll();
        return ResponseEntity.ok(drivers.stream()
                .map(driverMapper::toDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<DriverResponseDto> getDriverById(@Valid @PathVariable Long id) {
        Driver driver = driverService.findById(id);
        return ResponseEntity.ok(driverMapper.toDto(driver));
    }

    @PostMapping()
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<DriverResponseDto> postCreateDriver(@Valid @RequestBody DriverResponseDto driverDto) {
        Driver driverToCreate = driverMapper.toEntity(driverDto);
        Driver createdDriver = driverService.save(driverToCreate);
        return new ResponseEntity<>(driverMapper.toDto(createdDriver), HttpStatus.CREATED);        
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<DriverResponseDto> updateDriver(@PathVariable Long id, @Valid @RequestBody DriverResponseDto driverDto) {
        Driver driverToUpdate = driverMapper.toEntity(driverDto);
        Driver updatedDriver = driverService.update(id, driverToUpdate);
        return ResponseEntity.ok(driverMapper.toDto(updatedDriver));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Void> deleteDriver(@PathVariable Long id) {
        driverService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    
    


}
