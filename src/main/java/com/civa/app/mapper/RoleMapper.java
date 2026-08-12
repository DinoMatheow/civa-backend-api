package com.civa.app.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.civa.app.domain.Role;
import com.civa.app.dto.RoleDto;

@Mapper(componentModel = "spring")      
public interface RoleMapper {
    RoleDto toDto(Role role);
    Role toEntity(RoleDto roleDto);

    List<RoleDto> toDtoList(List<Role> roles);
}
