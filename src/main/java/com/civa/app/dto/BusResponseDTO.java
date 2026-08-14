package com.civa.app.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.civa.app.domain.Category;

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
    private Category category;
    private List<DriverResponseDto> driverDto;
}


