package com.civa.app.mapper;

import org.mapstruct.Mapper;

import com.civa.app.domain.User;
import com.civa.app.dto.RegisterDto;

@Mapper(componentModel = "spring")
public interface UserMapper {
    
    User toEntity(RegisterDto registerDto);

    

}
