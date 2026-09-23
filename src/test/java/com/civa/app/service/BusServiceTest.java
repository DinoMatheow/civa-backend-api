package com.civa.app.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import com.civa.app.domain.Bus;
import com.civa.app.domain.Category;
import com.civa.app.domain.Driver;
import com.civa.app.dto.BusRequestDto;
import com.civa.app.dto.BusResponseDTO;
import com.civa.app.exception.ResourceNotFoundException;
import com.civa.app.mapper.BusMapper;
import com.civa.app.repository.BusRepository;

@ExtendWith(MockitoExtension.class) 
public class BusServiceTest {


    @Mock 
    private BusRepository busRepository;

    @Mock 
    private  BusMapper busMapper;

    @Mock 
    private  CategoryService categoryService;


    @Mock 
    private  DriverService driverService;


    @InjectMocks
    private BusService busService;

    private Bus bus;
    private BusRequestDto busRequestDto;
    private BusResponseDTO busResponseDTO;
    private Category category;
    private  Driver driver;
    private  PageRequest pageable;


    @BeforeEach 
    void setUp(){
       category = new Category(1L, "conferencia", "descripcion");
       driver = new Driver( 10L, "lucas", "lucas@gmail.com","bio de lucas", new HashSet<>()); 

        bus = new Bus();
        bus.setId(1L);
        bus.setNumberBus("ABC-123");           
        bus.setCategory(category);
        bus.getDrivers().add(driver);      

        busRequestDto = new BusRequestDto();
        busRequestDto.setNumberBus("ABC-123");
        busRequestDto.setCategoryBusId(1L);
        busRequestDto.setDriversIds(new HashSet<>(Set.of(10L)));

        busResponseDTO = new BusResponseDTO();
        busResponseDTO.setId(1L);
        busResponseDTO.setNumberBus("ABC-123");
         busResponseDTO.setCategoryBusName("Interprovincial");

            pageable = PageRequest.of(0, 10);
}


    @Test
    @DisplayName("Debe retornar un Bus cuando el Id existe")
    void shouldReturnBusWhenIdExists(){
        when(busRepository.findById(anyLong())).thenReturn(Optional.of(bus));

        Bus fountBus = busService.findById(1L);

        assertNotNull(fountBus);
        assertEquals(bus.getId(), fountBus.getId());
        verify(busRepository, times(1)).findById(1L);

    }

    @Test 
    @DisplayName("Debe lanzar ResourceNotFoundException cuando el ID no existe")
    void shouldThrowResourceNotFountExceptionWhenIdDoesNotExist(){
        when(busRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException thrown = assertThrows( 
           ResourceNotFoundException.class, () -> {
            busService.findById(99L);
           });

           assertEquals("El ID: 99 no se encontro", thrown.getMessage());

           verify(busRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("Debe guarda un Bus exitosamenete con tageoria y conductores")
    void shouldSaveBusSuccesFulyWithCategory(){
        Bus  busWithoutId = new Bus();
        busWithoutId.setNumberBus(busRequestDto.getNumberBus());
        busWithoutId.setMarcaBus(busRequestDto.getMarcaBus());
        busWithoutId.setPlate(busRequestDto.getPlate());
        busWithoutId.setStatus(busRequestDto.getStatus());
        when(busMapper.toEntity(any(BusRequestDto.class))).thenReturn(busWithoutId);

        when(categoryService.findById(busRequestDto.getCategoryBusId())).thenReturn(category);

        when(driverService.findById(10L)).thenReturn(driver);


        when(busRepository.save(any(Bus.class))).thenAnswer(
            invocation -> {
                Bus savedBus = invocation.getArgument(0);
                savedBus.setId(1L);
                return savedBus;
            });


        Bus savedBus = busService.save(busRequestDto);
        assertNotNull(savedBus);
        assertEquals(1L, savedBus.getId());
        assertEquals(busRequestDto.getNumberBus(), savedBus.getNumberBus());
        assertEquals(category, savedBus.getCategory());
        assertEquals(1, savedBus.getDrivers().size());

        assertTrue(savedBus.getDrivers().contains(driver));

        verify(busMapper, times(1)).toEntity(busRequestDto);
        verify(categoryService, times(1)).findById(busRequestDto.getCategoryBusId());
        verify(driverService, times(1)).findById(10L);
        verify(busRepository, times(1)).save(any(Bus.class));
    }

    @Test
    @DisplayName("Debe guardar un bus exitoxamenete sin drivers")
    void shouldSaveBusSuccesFulyWithDrivers(){
        busRequestDto.setDriversIds(null);

        Bus busWithOutId = new Bus();

        busWithOutId.setNumberBus(busRequestDto.getNumberBus());
        busWithOutId.setAttributes(busRequestDto.getAttributes());
        busWithOutId.setPlate(busRequestDto.getPlate());

         when(busMapper.toEntity(any(BusRequestDto.class))).thenReturn(busWithOutId);

        when(categoryService.findById(busRequestDto.getCategoryBusId())).thenReturn(category);

        // when(driverService.findById(10L)).thenReturn(driver);


        when(busRepository.save(any(Bus.class))).thenAnswer(
            invocation -> {
                Bus savedBus = invocation.getArgument(0);
                savedBus.setId(1L);
                return savedBus;
            });


        Bus savedBus = busService.save(busRequestDto);
        assertNotNull(savedBus);
        assertEquals(1L, savedBus.getId());
        assertEquals(busRequestDto.getNumberBus(), savedBus.getNumberBus());
        assertEquals(category, savedBus.getCategory());
        assertTrue(savedBus.getDrivers().isEmpty());

        verify(busMapper, times(1)).toEntity(busRequestDto);
        verify(categoryService, times(1)).findById(busRequestDto.getCategoryBusId());
        verify(driverService, never()).findById(anyLong());
        verify(busRepository, times(1)).save(any(Bus.class));
        


    }



    @Test 
    @DisplayName("Debe lanzar ResourceNotFountException si la categoria no existe al guardar")
    void shouldThoewReosurceNotFoundExceptionWhenCategoryNotFoundOnSace(){
        Bus busWithOutId = new Bus();

        when(busMapper.toEntity(any(BusRequestDto.class))).thenReturn(busWithOutId);

        when(categoryService.findById((anyLong()))).thenThrow(
            new ResourceNotFoundException("Category not found with id:" + busRequestDto.getCategoryBusId()));


        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class,  ()-> {
                busService.save(busRequestDto);
        });

        assertEquals("Category not found with id:"  + busRequestDto.getCategoryBusId(), thrown.getMessage());

        verify(busRepository, never()).save(any(Bus.class));



    }
























    }
      



