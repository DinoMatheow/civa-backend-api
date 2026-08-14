package com.civa.app.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class DriverRequestDto {
    
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")    
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "El formato del email no es valido")
    @Size(min = 5, max = 100, message = "Email must be between 5 and 100 characters")
    private String email;

    @Size(max = 500, message = "Bio must be less than 500 characters")
    private String bio;
    
}
