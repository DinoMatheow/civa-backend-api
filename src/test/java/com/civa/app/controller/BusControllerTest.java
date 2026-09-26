package com.civa.app.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
import com.civa.app.dto.BusRequestDto;
import com.civa.app.dto.BusResponseDTO;
import com.civa.app.dto.DriverResponseDto;
import com.civa.app.exception.ResourceNotFoundException;
import com.civa.app.mapper.BusMapper;
import com.civa.app.security.jwt.JwtAuthEntryPoint;
import com.civa.app.security.jwt.JwtAuthenticationFilter;
import com.civa.app.security.jwt.JwtGenerator;
import com.civa.app.service.BusService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Pageable;

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
    @Test 
    @DisplayName("GET /api/v1/bus/{id} - Debe retornar 404 Not Found cuando el bus no existe" )
    @WithMockUser(username = "testUser", roles = "USER")
    void shouldReturnNotFOuntWhenBusDoesNotExist()throws Exception {
        when(busService.findById(anyLong())).thenThrow(
            new ResourceNotFoundException("El ID: 99 no se encontro")
        );

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/bus/{id}", 99L)
        .accept(MediaType.APPLICATION_JSON))


            .andExpectAll(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.message").value("El ID: 99 no se encontro"));



        verify(busService, times(1)).findById(99L);
        verify(busMapper, never()).toBusResponseDTO(any(Bus.class));



    }

    @Test
    @DisplayName("GET /api/v1/bus - Debe retornar todos los buses paginados y filtrados")
    @WithMockUser(username = "testUser", roles = "USER")
    void shouldReturnAllBusPageAndFiltered() throws Exception {
        
        DriverResponseDto driverResponseA = new DriverResponseDto(20L, "Juan Pérez", "juan.perez@example.com", "Experto en Spring Boot.");
        Set<DriverResponseDto> driversA = new HashSet<>(Set.of(driverResponseA));

            BusResponseDTO busResponse2 = new BusResponseDTO();
            busResponse2.setId(2L);
            busResponse2.setNumberBus("DEF-456");
            busResponse2.setPlate("QWE-321");
            busResponse2.setStatus("ACTIVO");
            busResponse2.setMarcaBus("Toyota");
            busResponse2.setCategoryBusName("Interprovincial");
            busResponse2.setCategoryBusId(10L);
            busResponse2.setDriverDto(driversA); // Asigna los drivers

            BusResponseDTO busResponse3 = new BusResponseDTO();
            busResponse3.setId(3L);
            busResponse3.setNumberBus("GHI-789");
            busResponse3.setPlate("RTY-654");
            busResponse3.setStatus("ACTIVO");
            busResponse3.setMarcaBus("Toyota");
            busResponse3.setCategoryBusName("Interprovincial");
            busResponse3.setCategoryBusId(10L);
            busResponse3.setDriverDto(driversA); // Asigna los mismos drivers

            List<BusResponseDTO> busResponseList = List.of(busResponse2, busResponse3);

            Pageable pageableMock = PageRequest.of(0, 10);

            Page<BusResponseDTO> busResponseDtoPage = new PageImpl<>(busResponseList,
                    pageableMock, busResponseList.size());


            when(busService.findAll(eq("Spring"), any(Pageable.class))).thenReturn(busResponseDtoPage);

            mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/bus")
                .param("page", "0")
                .param("size", "10")
                .param("numberBus", "Spring")
                .accept(MediaType.APPLICATION_JSON)
                
        )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.content[0]").exists())
            .andExpect(jsonPath("$.content[1]").exists())
            .andExpect(jsonPath("$.content[2]").doesNotExist())


        // Verificar los datos del primer bus en la página
        .andExpect(jsonPath("$.content[0].id").value(2))
        .andExpect(jsonPath("$.content[0].numberBus").value("DEF-456"))
        .andExpect(jsonPath("$.content[0].plate").value("QWE-321"))
        .andExpect(jsonPath("$.content[0].status").value("ACTIVO"))
        .andExpect(jsonPath("$.content[0].marcaBus").value("Toyota"))
        .andExpect(jsonPath("$.content[0].categoryBusName").value("Interprovincial"))
        .andExpect(jsonPath("$.content[0].driverDto.length()").value(1))
        .andExpect(jsonPath("$.content[0].driverDto[?(@.name == 'Juan Pérez')].id").value(20))
        .andExpect(jsonPath("$.content[0].driverDto[?(@.name == 'Juan Pérez')].name").value("Juan Pérez"))
        .andExpect(jsonPath("$.content[0].driverDto[?(@.name == 'Juan Pérez')].email").value("juan.perez@example.com"))
        .andExpect(jsonPath("$.content[0].driverDto[?(@.name == 'Juan Pérez')].bio").value("Experto en Spring Boot."))

        // Verificar los datos del segundo bus en la página
        .andExpect(jsonPath("$.content[1].id").value(3))
        .andExpect(jsonPath("$.content[1].numberBus").value("GHI-789"))
        .andExpect(jsonPath("$.content[1].plate").value("RTY-654"))
        .andExpect(jsonPath("$.content[1].status").value("ACTIVO"))
        .andExpect(jsonPath("$.content[1].marcaBus").value("Toyota"))
        .andExpect(jsonPath("$.content[1].categoryBusName").value("Interprovincial"))
        .andExpect(jsonPath("$.content[1].driverDto.length()").value(1))
        .andExpect(jsonPath("$.content[1].driverDto[?(@.name == 'Juan Pérez')].id").value(20))
        .andExpect(jsonPath("$.content[1].driverDto[?(@.name == 'Juan Pérez')].name").value("Juan Pérez"))
        .andExpect(jsonPath("$.content[1].driverDto[?(@.name == 'Juan Pérez')].email").value("juan.perez@example.com"))
        .andExpect(jsonPath("$.content[1].driverDto[?(@.name == 'Juan Pérez')].bio").value("Experto en Spring Boot."))



        .andExpect(jsonPath("$.pageable.pageNumber").value(0))
        .andExpect(jsonPath("$.pageable.pageSize").value(10))
        .andExpect(jsonPath("$.totalElements").value(2))
        .andExpect(jsonPath("$.totalPages").value(1))
        .andExpect(jsonPath("$.last").value(true));

        verify(busService, times(1)).findAll(eq("Spring"),  any(Pageable.class));
        verify(busService, never()).findById(anyLong());





    }

   

    @Test
    @DisplayName("POST /api/v1/bus - Debe crear un bus y retornar 201 Created")
    @WithMockUser(username = "adminUser", roles = "ADMIN")
    void shouldCreateBusSuccedfully() throws Exception {

    BusRequestDto busRequestDto = new BusRequestDto();
    busRequestDto.setNumberBus("JKL-321");
    busRequestDto.setPlate("MNO-654");
    busRequestDto.setCategoryBusId(10L);
    busRequestDto.setAttributes("Aire acondicionado, WiFi");   // ← faltaba
    busRequestDto.setStatus(Status.ACTIVO);        
    busRequestDto.setDriversIds(Set.of(20L, 21L));

    MarcaBus marcaBusRequest = new MarcaBus();                  // ← faltaba
    marcaBusRequest.setId(1L);
    marcaBusRequest.setName("Toyota");
    busRequestDto.setMarcaBus(marcaBusRequest);

    Bus savedBusEntity = new Bus();
    savedBusEntity.setId(5L); // El nuevo ID asignado
    savedBusEntity.setNumberBus("JKL-321");
    savedBusEntity.setPlate("MNO-654");
    savedBusEntity.setStatus(Status.ACTIVO);

    MarcaBus marcaBusForSavedBus = new MarcaBus();
    marcaBusForSavedBus.setId(1L);
    marcaBusForSavedBus.setName("Toyota");

    Category categoryForSavedBus = new Category(10L, "Interprovincial", "Buses de larga distancia");
    Driver driver1ForSavedBus = new Driver(20L, "Juan Pérez", "juan.perez@example.com", "Conductor con 5 años de experiencia.", new HashSet<>());
    Driver driver2ForSavedBus = new Driver(21L, "María García", "maria.garcia@example.com", "Conductora profesional certificada.", new HashSet<>());

    savedBusEntity.setMarcaBus(marcaBusForSavedBus);
    savedBusEntity.setCategory(categoryForSavedBus);
    savedBusEntity.addDrivers(driver1ForSavedBus);
    savedBusEntity.addDrivers(driver2ForSavedBus);

    BusResponseDTO createdBusResponseDTO = new BusResponseDTO();
    createdBusResponseDTO.setId(5L);
    createdBusResponseDTO.setNumberBus("JKL-321");
    createdBusResponseDTO.setPlate("MNO-654");
    createdBusResponseDTO.setStatus("ACTIVO");
    createdBusResponseDTO.setMarcaBus("Toyota");
    createdBusResponseDTO.setCategoryBusName("Interprovincial");
    createdBusResponseDTO.setCategoryBusId(10L);

    DriverResponseDto driverResponse1 = new DriverResponseDto(20L, "Juan Pérez", "juan.perez@example.com", "Conductor con 5 años de experiencia.");
    DriverResponseDto driverResponse2 = new DriverResponseDto(21L, "María García", "maria.garcia@example.com", "Conductora profesional certificada.");
    Set<DriverResponseDto> driversDto = new HashSet<>();
    driversDto.add(driverResponse1);
    driversDto.add(driverResponse2);
    createdBusResponseDTO.setDriverDto(driversDto);

    when(busService.save(any(BusRequestDto.class))).thenReturn(savedBusEntity);
    when(busMapper.toBusResponseDTO(savedBusEntity)).thenReturn(createdBusResponseDTO);

    mockMvc.perform(post("/api/v1/bus") 
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(busRequestDto)))

            .andExpect(status().isCreated()) 
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(5))
            .andExpect(jsonPath("$.numberBus").value("JKL-321"))
            .andExpect(jsonPath("$.plate").value("MNO-654"))
            .andExpect(jsonPath("$.status").value("ACTIVO"))
            .andExpect(jsonPath("$.marcaBus").value("Toyota"))
            .andExpect(jsonPath("$.categoryBusName").value("Interprovincial"))
            .andExpect(jsonPath("$.driverDto.length()").value(2))
            .andExpect(jsonPath("$.driverDto[?(@.name == 'Juan Pérez')].id").value(20))
            .andExpect(jsonPath("$.driverDto[?(@.name == 'María García')].id").value(21));

    verify(busService, times(1)).save(any(BusRequestDto.class));
    verify(busMapper, times(1)).toBusResponseDTO(savedBusEntity); 

    verify(busService, never()).findAll(anyString(), any(Pageable.class));
    verify(busService, never()).findById(anyLong());

    }

    @Test
    @DisplayName("DELETE /api/v1/bus{id}")
    @WithMockUser(username = "adminUser", roles = "ADMIN")
    void shouldDeleteBusSuccesfulyy() throws Exception{
        final Long busIdToDelete = 1L;
        
        doNothing().when(busService).deleteById(busIdToDelete);

        mockMvc.perform(delete("/api/v1/bus/{id}", busIdToDelete))
            .andExpect(status().isNoContent());

        verify(busService, times(1)).deleteById(busIdToDelete);
        verify(busMapper, never()).toBusResponseDTO(any(Bus.class));

    }

    @Test
    @DisplayName("PUT /api/v1/bus/{id} - Debe actualizar un bus existente y retornar 200 OK")
    @WithMockUser(username = "adminUser", roles = "ADMIN")
    void shouldUpdateBusSuccessfully() throws Exception {
         final Long busIdToUpdate = 1L; 

    BusRequestDto updateBusRequestDto = new BusRequestDto();
    updateBusRequestDto.setNumberBus("ABC-999");
    updateBusRequestDto.setPlate("ZZZ-111");
    updateBusRequestDto.setAttributes("Aire acondicionado, WiFi, Baño");
    updateBusRequestDto.setStatus(Status.ACTIVO);
    updateBusRequestDto.setCategoryBusId(11L); 
    updateBusRequestDto.setDriversIds(Set.of(22L)); 

    MarcaBus marcaBusRequest = new MarcaBus();
    marcaBusRequest.setId(2L);
    marcaBusRequest.setName("Volvo");
    updateBusRequestDto.setMarcaBus(marcaBusRequest);

    Bus updatedBusEntity = new Bus();
    updatedBusEntity.setId(busIdToUpdate);
    updatedBusEntity.setNumberBus("ABC-999");
    updatedBusEntity.setPlate("ZZZ-111");
    updatedBusEntity.setStatus(Status.ACTIVO);

    Category newCategory = new Category(11L, "Turismo", "Eventos de turismo y excursiones");
    MarcaBus newMarcaBus = new MarcaBus();
    newMarcaBus.setId(2L);
    newMarcaBus.setName("Volvo");
    Driver newDriver = new Driver(22L, "Carlos López", "carlos.lopez@example.com", "Conductor especializado en rutas largas.", new HashSet<>());

    updatedBusEntity.setCategory(newCategory);
    updatedBusEntity.setMarcaBus(newMarcaBus);
    updatedBusEntity.addDrivers(newDriver);

    BusResponseDTO updatedBusResponseDTO = new BusResponseDTO();
    updatedBusResponseDTO.setId(busIdToUpdate);
    updatedBusResponseDTO.setNumberBus("ABC-999");
    updatedBusResponseDTO.setPlate("ZZZ-111");
    updatedBusResponseDTO.setStatus("ACTIVO");
    updatedBusResponseDTO.setMarcaBus("Volvo");
    updatedBusResponseDTO.setCategoryBusName("Turismo");
    updatedBusResponseDTO.setCategoryBusId(11L);
    updatedBusResponseDTO.setDriverDto(Set.of(new DriverResponseDto(22L, "Carlos López", "carlos.lopez@example.com", "Conductor especializado en rutas largas.")));

    when(busService.update(eq(busIdToUpdate), any(BusRequestDto.class))).thenReturn(updatedBusEntity);
    when(busMapper.toBusResponseDTO(updatedBusEntity)).thenReturn(updatedBusResponseDTO);

    mockMvc.perform(put("/api/v1/bus/{id}", busIdToUpdate) 
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateBusRequestDto))) 

            .andExpect(status().isOk()) 
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(busIdToUpdate))
            .andExpect(jsonPath("$.numberBus").value("ABC-999"))
            .andExpect(jsonPath("$.plate").value("ZZZ-111"))
            .andExpect(jsonPath("$.status").value("ACTIVO"))
            .andExpect(jsonPath("$.marcaBus").value("Volvo"))
            .andExpect(jsonPath("$.categoryBusName").value("Turismo"))
            .andExpect(jsonPath("$.categoryBusId").value(11))
            .andExpect(jsonPath("$.driverDto.length()").value(1))
            .andExpect(jsonPath("$.driverDto[0].id").value(22))
            .andExpect(jsonPath("$.driverDto[0].name").value("Carlos López"));

    verify(busService, times(1)).update(eq(busIdToUpdate), any(BusRequestDto.class));
    verify(busMapper, times(1)).toBusResponseDTO(updatedBusEntity);

    verify(busService, never()).save(any(BusRequestDto.class)); 
    verify(busService, never()).findAll(anyString(), any(Pageable.class));
    verify(busService, never()).findById(anyLong());
    }

    @Test
    @DisplayName("DELETE /api/v1/bus/{id} - Debe eliminar un bus y retornar 204 No Content")
    @WithMockUser(username = "adminUser", roles = "ADMIN")
    void shouldDeleteBusSuccesfully()throws Exception {

        final Long busIdToDelete = 1L;

        doNothing().when(busService).deleteById(busIdToDelete);

        mockMvc.perform(delete("/api/v1/bus/{id}", busIdToDelete))
            .andExpect(status().isNoContent());


        verify(busService, times(1)).deleteById(busIdToDelete);
        verify(busMapper, never()).toBusResponseDTO(any(Bus.class));

    }


   
    @Test
    @DisplayName("DELETE /api/v1/bus/{id} - Debe retornar 404 Not Fount si el bus a eliminar no existe")
    @WithMockUser(username = "adminUser", roles = "ADMIN")
    void shouldReturnNotFountWhenDeletingNonExistentBus()throws Exception {

        final Long nonExistentBusId = 999L;

        doThrow(new ResourceNotFoundException("El ID: " + nonExistentBusId  +" no se encontro"))
            .when(busService).deleteById(nonExistentBusId);

            mockMvc.perform(delete("/api/v1/bus/{id}", nonExistentBusId ))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.error").value("Not Found"))
            .andExpect(jsonPath("$.message").value("El ID: " + nonExistentBusId  +" no se encontro"));
            
            verify(busService, times(1)).deleteById(nonExistentBusId);
            verify(busMapper, never()).toBusResponseDTO(any(Bus.class));


    }









   


}
