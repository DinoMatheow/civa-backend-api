package com.civa.app.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.civa.app.domain.Trip;
import com.civa.app.dto.TripRequestDto;
import com.civa.app.dto.TripResponseDto;

@Mapper(componentModel = "spring")
public interface TripMapper {
@Mapping(target = "id", ignore = true)
@Mapping(target = "origin", ignore = true)
@Mapping(target = "destination", ignore = true)
@Mapping(target = "bus", ignore = true)
@Mapping(target = "tripCode", ignore = true)
@Mapping(target = "status", ignore = true)
@Mapping(target = "availableSeats", ignore = true)
@Mapping(target = "createdAt", ignore = true)
Trip toEntity(TripRequestDto dto);

@Mapping(source = "origin.name", target = "origin")
@Mapping(source = "destination.name", target = "destination")
@Mapping(source = "bus.category.name", target = "categoryBusName")
TripResponseDto toResponseDto(Trip trip);



}
