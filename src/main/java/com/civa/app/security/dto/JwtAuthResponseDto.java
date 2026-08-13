package com.civa.app.security.dto;

import lombok.Data;

@Data
public class JwtAuthResponseDto {
    private String accessToken; 
    private String tokenTpye = "Bearer";

    public JwtAuthResponseDto(String accessToken){
        this.accessToken = accessToken;
    }
}
