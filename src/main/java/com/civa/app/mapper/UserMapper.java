package com.civa.app.mapper;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import com.civa.app.domain.Role;
import com.civa.app.domain.User;
import com.civa.app.exception.ResourceNotFoundException;
import com.civa.app.repository.RoleRepository;
import com.civa.app.security.dto.RegisterDto;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    @Autowired  
    protected RoleRepository roleRepository;
    
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", source = "registerDto.roles", qualifiedByName = "mapRoleStringToRoles" )
    public abstract User registerDtoToUser(RegisterDto registerDto);


    @Named("mapRoleStringToRoles")
    public Set<Role> mapRoleStringToRoles(Set<String> rolesNames){

        if(rolesNames ==null || rolesNames.isEmpty()){
            return roleRepository.findByName("ROLE_USER")
                .map(Collections::singleton)
                .orElseThrow( ()-> new ResourceNotFoundException("Error: Rol 'ROLE_USER' no encontrado en la base de datos." ) );
        }

        return rolesNames.stream()
                .map( roleName -> roleRepository.findByName(roleName) 
                            .orElseThrow( ()-> new ResourceNotFoundException("Error: Rol no encontrado" + roleName )  ))
                .collect(Collectors.toSet());

    }
}