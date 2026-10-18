package com.civa.app.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum BusStatusEnum {
    ACTIVO,
    INACTIVO;

    @JsonCreator
    public static BusStatusEnum fromValue(String value){
        if(value == null){ return null; }
        
        return BusStatusEnum.valueOf(value.toUpperCase().trim());
    }
  
}
