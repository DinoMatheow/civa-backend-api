package com.civa.app.service;

import java.util.List;

import com.civa.app.domain.Driver;
import com.civa.app.dto.DriverRequestDto;

public interface DriverService {
    List<Driver> findAll();
    Driver findById(Long id);
    Driver save(DriverRequestDto requestDto);
    Driver update(Long id, DriverRequestDto requestDto);
    void deleteById(Long id);
}
