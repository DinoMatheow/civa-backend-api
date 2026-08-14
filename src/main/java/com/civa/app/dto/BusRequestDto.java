 package com.civa.app.dto;


import java.util.Set;

import com.civa.app.domain.MarcaBus;
import com.civa.app.domain.Status;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BusRequestDto {
    @NotBlank(message = "El número del bus no puede estar vacío")
    private String numberBus;

    @NotBlank(message = "La placa del bus no puede estar vacía")
    private String plate;
    
    @NotBlank(message = "Los atributos del bus no pueden estar vacíos")
    private String attributes;
    
    @NotNull(message = "El estado del bus no puede ser nulo")
    private Status status;
    
    @NotNull(message = "La marca del bus no puede ser nula")
    @Valid
    private MarcaBus marcaBus;

    @NotNull(message = "La categoría del bus no puede ser nula")
    private Long categoryBusId;

    private Set<Long> driversIds;

}
