package com.civa.app.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.civa.app.domain.Bus;
import com.civa.app.dto.BusResponseDTO;
import com.civa.app.mapper.BusMapper;
import com.civa.app.repository.BusRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class BusService implements IBusService {
    
    private final BusRepository busRepository;
    private final BusMapper busMapper;

    @Override
    public Page<BusResponseDTO> findAll(String name, Pageable pageable) {
        Page<Bus> busPage;
        if(name !=null && !name.trim().isEmpty()){
            busPage = busRepository.findByNameContainingIgnoreCase(name, pageable);
        }else {
            busPage = busRepository.findAll(pageable);
        }

        List<BusResponseDTO> dtos = busPage.getContent().stream()
        .map(busMapper::toBusResponseDTO)
        .toList();

        return new PageImpl<>(dtos, pageable, busPage.getTotalElements());
        // return busRepository.findAll();
    }
    @Override
    public Bus findById(Long id) {
        return busRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("El ID: " + id  +" no se encontro " ));
    }


}
