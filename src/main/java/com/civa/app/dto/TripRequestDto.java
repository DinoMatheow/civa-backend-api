package com.civa.app.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data 
public class TripRequestDto {
        
 
    @NotNull
    private Long originCityId;

    @NotNull
    private Long destinationCityId;

    @NotNull
    private Long busId;

    @NotNull
    private LocalDateTime departureTime;

    private LocalDateTime arrivalTime;

    @NotNull
    private BigDecimal price;

}
