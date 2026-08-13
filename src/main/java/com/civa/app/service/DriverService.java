package com.civa.app.service;

import java.util.List;

import com.civa.app.domain.Driver;

public interface DriverService {
    List<Driver> findAll();
    Driver findById(Long id);
    Driver save(Driver driver);
    Driver update(Long id, Driver driver);
    void deleteById(Long id);
}
