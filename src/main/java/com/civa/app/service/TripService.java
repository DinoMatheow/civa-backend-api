package com.civa.app.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.civa.app.dto.TripResponseDto;
import com.civa.app.dto.TripSearchRequestDto;

public interface TripService {
    Page<TripResponseDto> search(TripSearchRequestDto dto, Pageable pageable);
    Page<TripResponseDto> findAll(Pageable pageable);



}
