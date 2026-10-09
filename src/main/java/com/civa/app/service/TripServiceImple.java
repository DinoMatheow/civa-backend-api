package com.civa.app.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.civa.app.domain.TripStatusEnum;
import com.civa.app.dto.TripResponseDto;
import com.civa.app.dto.TripSearchRequestDto;
import com.civa.app.mapper.TripMapper;
import com.civa.app.repository.TripRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor 
public class TripServiceImple {
    
    private final TripRepository tripRepository;
    private final TripMapper tripMapper;
    private final BusService busService;


   @Transactional(readOnly = true)
    public Page<TripResponseDto> search(TripSearchRequestDto dto, Pageable pageable) {

         LocalDateTime start = dto.getDepartureDate().atStartOfDay();
        LocalDateTime end = dto.getDepartureDate().plusDays(1).atStartOfDay();

        return tripRepository
                .search(dto.getOriginCityId(), dto.getDestinationCityId(),
                        start, end, TripStatusEnum.PROGRAMADO, pageable)
                .map(tripMapper::toResponseDto);
    }

    @Transactional(readOnly = true)
    public Page<TripResponseDto> findAll(Pageable pageable) {
        return tripRepository.findAll(pageable)
                .map(tripMapper::toResponseDto);

    }

    

}
