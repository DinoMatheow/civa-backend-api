package com.civa.app.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;

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


    @TestConfiguration 
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

        when(busService.findById(anyLong())).thenReturn(bus);
        when(busMapper.toBusResponseDTO(any(Bus.class))).thenReturn(busResponseDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/bus/{id}", 1L)
        .accept(MediaType.APPLICATION_JSON)
    
    ) 

        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.numberBus").value("ABC-123"))
        .andExpect(jsonPath("$.plate").value("XYZ-789"))
        .andExpect(jsonPath("$.categoryBusName").value("Interprovincial"))

        .andExpect(jsonPath("$.driverDto.length()").value(2))
        .andExpect(jsonPath("$.driverDto[2]").doesNotExist())

        // --- Verificación completa de Juan Pérez (sin asumir si es [0] o [1]) ---
        .andExpect(jsonPath("$.driverDto[?(@.name == 'Juan Pérez')].name").value("Juan Pérez"))
        .andExpect(jsonPath("$.driverDto[?(@.name == 'Juan Pérez')].email").value("juan.perez@example.com"))
        .andExpect(jsonPath("$.driverDto[?(@.name == 'Juan Pérez')].bio").value("Conductor con 5 años de experiencia."))

        // --- Verificación completa de María García (sin asumir si es [0] o [1]) ---
        .andExpect(jsonPath("$.driverDto[?(@.name == 'María García')].name").value("María García"))
        .andExpect(jsonPath("$.driverDto[?(@.name == 'María García')].email").value("maria.garcia@example.com"))
        .andExpect(jsonPath("$.driverDto[?(@.name == 'María García')].bio").value("Conductora profesional certificada."));


        verify(busService, times(1)).findById(1L);
        verify(busMapper, times(1)).toBusResponseDTO(bus);

    }


   


}
