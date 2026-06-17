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
import  com.civa.app.exception.ResourceNotFoundException;

@Service
@RequiredArgsConstructor
public class BusService implements IBusService {
    
    private final BusRepository busRepository;
    private final BusMapper busMapper;

    @Override
    public Page<BusResponseDTO> findAll(String numberBus, Pageable pageable) {
        Page<Bus> busPage;
        if(numberBus !=null && !numberBus.trim().isEmpty()){
            busPage = busRepository.findByNumberBusContainingIgnoreCase(numberBus, pageable);
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
                .orElseThrow(() -> new ResourceNotFoundException("El ID: " + id  +" no se encontro " ));
    }
    @Override
    public void deleteById(Long id) {
        Bus busToDelete = this.findById(id);
        busRepository.delete(busToDelete);
        // throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
    }
    @Override
    public Bus save(Bus bus) {
        return busRepository.save(bus);
        // throw new UnsupportedOperationException("Unimplemented method 'save'");
    }


}
