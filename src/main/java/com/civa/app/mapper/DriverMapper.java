package com.civa.app.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.civa.app.domain.Driver;
import com.civa.app.dto.DriverRequestDto;
import com.civa.app.dto.DriverResponseDto;

@Mapper(componentModel = "spring")
public interface DriverMapper {


        // @Mapping(target = "buses", ignore = true)
        DriverResponseDto toDto(Driver driver);

        @Mapping(target = "id", ignore = true)
        @Mapping(target = "buses", ignore = true)
        Driver toEntity(DriverRequestDto requestDto);

        List<DriverResponseDto> toResponseToList(List<Driver> driver);

        @Mapping(target = "id", ignore = true)
        @Mapping(target = "buses", ignore = true)
        void updateDriverFromDto(DriverRequestDto requestDto, @MappingTarget Driver driver);


}
