package com.civa.app.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class BusSummaryDto {
    private Long id;
    private String name;
    private LocalDate date;
    private String location;
    

}
