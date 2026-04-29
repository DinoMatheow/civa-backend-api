package com.civa.app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.civa.app.domain.Bus;
import com.civa.app.repository.BusRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class BusService implements IBusService {
    
    private final BusRepository busRepository;

    @Override
    public List<Bus> findAll() {

        return busRepository.findAll();
    }
    @Override
    public Bus findById(Long id) {
        return busRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bus no encontrado"));
    }


}
