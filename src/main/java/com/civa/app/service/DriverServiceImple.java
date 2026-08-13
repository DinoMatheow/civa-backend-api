package com.civa.app.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.civa.app.domain.Driver;
import com.civa.app.exception.ResourceNotFoundException;
import com.civa.app.repository.DriverRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DriverServiceImple implements DriverService {
    
    private final DriverRepository driverRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Driver> findAll() {
        return driverRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Driver findById(Long id) {
        return driverRepository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("Driver not found with id: " + id));
    }

    @Override
    @Transactional
    public Driver save(Driver driver) {
        return driverRepository.save(driver);
    }

    @Override
    @Transactional
    public Driver update(Long id, Driver driver) {
        Driver existingDriver = driverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found with id: " + id));
        existingDriver.setName(driver.getName());
        existingDriver.setEmail(driver.getEmail());
        existingDriver.setBio(driver.getBio());
        return driverRepository.save(existingDriver);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if(!driverRepository.existsById(id)) {
            throw new ResourceNotFoundException("Driver not found with id: " + id);
        }
        driverRepository.deleteById(id);
    }
    
}
