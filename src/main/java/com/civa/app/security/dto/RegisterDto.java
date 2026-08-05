package com.civa.app.security.dto;

import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * RegisterDto
 */
@Data
@AllArgsConstructor
@NoArgsConstructor  
public class RegisterDto {

    @NotBlank(message = "El nombre de usuario no puede estar vacio.")
    @Size(min = 4, max = 20, message = "El nombre de usuario debe tener 4 a 20 caracteres.")
    private String username;
    
    @NotBlank(message = "La clave no puede estar vacia.")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres.")
    private String password;
    
    @NotBlank(message = "El email no puede estar vacio.")
    @Email(message = "Debe ser uan direccion de correo eletronico valida.")
    private String email;
    
    @NotBlank(message = "El nombre no puede estar vacio.")
    private String name;

    private Set<String> roles;




}
