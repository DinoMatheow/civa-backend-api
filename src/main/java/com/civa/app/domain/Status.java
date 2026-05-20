package com.civa.app.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Status {
    ACTIVO,
    INACTIVO;

    @JsonCreator
    public static Status fromValue(String value){
        if(value == null){ return null; }
        
        return Status.valueOf(value.toUpperCase().trim());
    }
  
}
