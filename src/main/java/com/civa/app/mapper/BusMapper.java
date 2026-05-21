package com.civa.app.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.civa.app.domain.Bus;
import com.civa.app.dto.BusRequestDto;
import com.civa.app.dto.BusResponseDTO;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BusMapper {

    Bus toEntity(BusRequestDto busRequestDto);

    @Mapping(source = "marcaBus.name", target = "marcaBus")
    BusResponseDTO toBusResponseDTO(Bus bus);
    
    
    List<BusResponseDTO> toBusResponseDTOList(List<Bus> busList);
     
    void updateBusFromDTO(BusRequestDto busRequestDto, @MappingTarget Bus bus);

    
}