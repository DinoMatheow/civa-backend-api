package com.civa.app.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Primary;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.civa.app.domain.Bus;
import com.civa.app.domain.Category;
import com.civa.app.domain.Driver;
import com.civa.app.domain.MarcaBus;
import com.civa.app.domain.Status;
import com.civa.app.dto.BusResponseDTO;
import com.civa.app.dto.DriverResponseDto;
import com.civa.app.mapper.BusMapper;
import com.civa.app.security.jwt.JwtAuthEntryPoint;
import com.civa.app.security.jwt.JwtAuthenticationFilter;
import com.civa.app.security.jwt.JwtGenerator;
import com.civa.app.service.BusService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(
    controllers = BusController.class,
    excludeAutoConfiguration = {
        SecurityAutoConfiguration.class,
        UserDetailsServiceAutoConfiguration.class
    },
    excludeFilters = @ComponentScan .Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
        JwtAuthenticationFilter.class,
        JwtGenerator.class,
        JwtAuthEntryPoint.class
    })

) 
public class BusControllerTest {
    @Autowired
    private  MockMvc mockMvc;
    private  BusService busService;
    private BusMapper busMapper;

    
    @Autowired 
    private  ObjectMapper objectMapper;

    private  BusResponseDTO busResponseDTO;
    private  Bus bus;

    static  class BusControllerTestConfig {
        
        @Bean 
        @Primary 
        BusService busService(){
            return  mock(BusService.class);
        }


        @Bean 
        @Primary 
        BusMapper busMapper(){
            return  mock(BusMapper.class);
        }

        
    }

    @BeforeEach 
    void setUp(@Autowired  BusService busServiceMock, @Autowired BusMapper busMapperMock){
        this.busService = busServiceMock;
        this.busMapper = busMapperMock;

        reset(busService, busMapper);

        Category category = new Category(10L, "Interprovincial", "Buses de larga distancia");
        Driver driver1 = new Driver(20L, "Juan Pérez", "juan.perez@example.com", "Conductor con 5 años de experiencia.", new HashSet<>());
        Driver driver2 = new Driver(21L, "María García", "maria.garcia@example.com", "Conductora profesional certificada.", new HashSet<>());

        MarcaBus marcaBus = new MarcaBus();
        marcaBus.setId(1L);
        marcaBus.setName("Toyota");


        bus = new Bus();
        bus.setId(1L);
        bus.setNumberBus("ABC-123");
        bus.setPlate("XYZ-789");
        bus.setStatus(Status.ACTIVO);
        bus.setMarcaBus(marcaBus);
        bus.setCategory(category);
        bus.addDrivers(driver1);
        bus.addDrivers(driver2);

        busResponseDTO = new BusResponseDTO();
        busResponseDTO.setId(1L);
        busResponseDTO.setNumberBus("ABC-123");
        busResponseDTO.setPlate("XYZ-789");
        busResponseDTO.setStatus("ACTIVO");
        busResponseDTO.setMarcaBus("Toyota");
        busResponseDTO.setCategoryBusName("Interprovincial");
        busResponseDTO.setCategoryBusId(10L);

        // DTOs de Drivers para el BusResponseDTO
        DriverResponseDto driverResponse1 = new DriverResponseDto(20L, "Juan Pérez", "juan.perez@example.com", "Conductor con 5 años de experiencia.");
        DriverResponseDto driverResponse2 = new DriverResponseDto(21L, "María García", "maria.garcia@example.com", "Conductora profesional certificada.");

        Set<DriverResponseDto> driversDto = new HashSet<>();
        driversDto.add(driverResponse1);
        driversDto.add(driverResponse2);
        busResponseDTO.setDriverDto(driversDto);



    }



    @Test
    @DisplayName  ("Get /api/v1/buses/{id} - Debe retornar un bus por ID cuando existe")
    @WithMockUser(username = "testUser", roles = "USER")
    void shouldReturnBusByID()throws Exception {

    }




}
