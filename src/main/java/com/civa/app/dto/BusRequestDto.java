package com.civa.app.dto;

import java.time.LocalDateTime;

import com.civa.app.domain.MarcaBus;
import com.civa.app.domain.Status;

import lombok.Data;

@Data
public class BusRequestDto {
    private String numberBus;
    private String plate;
    private LocalDateTime createdAt;
    private String attributes;
    private Status status;
    private MarcaBus marcaBus;
}
