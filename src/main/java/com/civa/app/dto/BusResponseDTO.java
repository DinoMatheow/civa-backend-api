package com.civa.app.dto;

import java.time.LocalDateTime;
import java.util.Set;


import lombok.Data;

@Data
public class BusResponseDTO {
    private Long id;
    private String numberBus;
    private String plate;
    private LocalDateTime createdAt;
    private String attributes;
    private String status;
    
    private String marcaBus;
    
    private Long categoryBusId;
    private String categoryBusName;

    private Set<DriverResponseDto> driverDto;
}


