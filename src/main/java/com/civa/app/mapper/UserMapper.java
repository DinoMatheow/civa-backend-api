package com.civa.app.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.civa.app.domain.User;
import com.civa.app.dto.RegisterDto;

@Mapper(componentModel = "spring")
public interface UserMapper {
    
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "id", ignore = true)

    User registerDtoToUser(RegisterDto registerDto);


}
