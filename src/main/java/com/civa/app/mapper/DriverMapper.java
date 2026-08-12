package com.civa.app.mapper;

import org.mapstruct.Mapper;

import com.civa.app.domain.Driver;
import com.civa.app.dto.DriverDto;

@Mapper(componentModel = "spring")
public interface DriverMapper {
        DriverDto toDto(Driver driver);
        Driver toEntity(DriverDto driverDto);


}
