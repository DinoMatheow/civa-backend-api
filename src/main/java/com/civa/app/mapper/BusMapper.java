package com.civa.app.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.civa.app.domain.Bus;
import com.civa.app.dto.BusResponseDTO;

@Mapper(componentModel = "spring")
public interface BusMapper {
    List<BusResponseDTO> toBusResponseDTOList(List<Bus> busList);

    @Mapping(source = "marcaBus.name", target = "marcaBus")
    BusResponseDTO toBusResponseDTO(Bus bus);
    
}