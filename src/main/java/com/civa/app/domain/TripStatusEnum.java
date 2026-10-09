package com.civa.app.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum TripStatusEnum {
    PROGRAMADO,   
    EN_CURSO,     
    FINALIZADO,   
    CANCELADO;

    @JsonCreator
    public static TripStatusEnum fromValue(String value){
        if(value == null){ return null; }
        
        return TripStatusEnum.valueOf(value.toUpperCase().trim());
    }
  
}
