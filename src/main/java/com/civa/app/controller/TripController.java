package com.civa.app.controller;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.civa.app.dto.TripResponseDto;
import com.civa.app.dto.TripSearchRequestDto;
import com.civa.app.mapper.TripMapper;
import com.civa.app.service.TripService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins  = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/trip")
@RequiredArgsConstructor
@Tag(name = "Trip Controller", description = "Controller for managing trips")
public class TripController {
    
    private final TripService tripService;
    private static final Logger logger = LoggerFactory.getLogger(TripController.class);
    private final TripMapper tripMapper;


    @GetMapping
    public ResponseEntity<Page<TripResponseDto>> getAllTrips(
        @Valid @ModelAttribute TripSearchRequestDto searchRequest,
        @PageableDefault(page = 0, size = 10, sort = "id")  Pageable pageable) {
         
        Page<TripResponseDto> trips = tripService.search(searchRequest, pageable);
        return ResponseEntity.ok(trips);

    } 

}