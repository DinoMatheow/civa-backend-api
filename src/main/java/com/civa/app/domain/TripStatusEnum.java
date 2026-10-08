package com.civa.app.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Status {
    PROGRAMADO,   
    EN_CURSO,     
    FINALIZADO,   
    CANCELADO;

    @JsonCreator
    public static Status fromValue(String value){
        if(value == null){ return null; }
        
        return Status.valueOf(value.toUpperCase().trim());
    }
  
}
