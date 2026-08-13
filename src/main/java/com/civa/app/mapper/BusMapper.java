package com.civa.app.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.civa.app.domain.Bus;
import com.civa.app.dto.BusRequestDto;
import com.civa.app.dto.BusResponseDTO;
import com.civa.app.dto.BusSummaryDto;

import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BusMapper {

    //Mapeo para la entrada - Request DTO
   @Mapping(target = "id", ignore = true )
   @Mapping(target = "category", ignore = true )
   @Mapping(target = "drivers", ignore = true )
   @Mapping(target = "attendedUsers", ignore = true )

    Bus toEntity(BusRequestDto busRequestDto);

    @Mapping(source = "marcaBus.name", target = "marcaBus")
    BusResponseDTO toBusResponseDTO(Bus bus);
    List<BusResponseDTO> toBusResponseDTOList(List<Bus> busList);
     
   //Metodo para actulizar una entidad existente
   @Mapping(target = "id", ignore = true)
   @Mapping(target = "category", ignore = true)
   @Mapping(target = "drivers", ignore = true)
   @Mapping(target = "attendedUsers", ignore = true)
    void updateBusFromDTO(BusRequestDto busRequestDto, @MappingTarget Bus bus);

    BusSummaryDto toSummaryDto(Bus bus);

    List<BusSummaryDto> toSummaryDtosList(List<Bus> bus);

   
}