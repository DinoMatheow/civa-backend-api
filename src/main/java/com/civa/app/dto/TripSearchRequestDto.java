package com.civa.app.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data 
public class TripSearchRequestDto {
    
    @NotNull
    private Long originCityId;

    @NotNull
    private Long destinationCityId;

    @NotNull
    private LocalDate departureDate;

    private LocalDate returnDate; 
    
}
