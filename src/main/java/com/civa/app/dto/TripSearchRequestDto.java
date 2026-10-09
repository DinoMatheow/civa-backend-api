package com.civa.app.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data 
public class TripSearchRequestDto {
    
    @NotNull
    private Long originCityId;

    @NotNull
    private Long destinationCityId;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate departureDate;

}
