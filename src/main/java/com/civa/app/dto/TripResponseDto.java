package com.civa.app.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;


@Data 
public class TripResponseDto {

    private Long id;
    private String tripCode;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private String status;
    private String origin;
    private String destination;
    private BigDecimal price;
    private Integer availableSeats;
    private String categoryBusName; 

    
}
