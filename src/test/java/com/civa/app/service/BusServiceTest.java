package com.civa.app.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.PageRequest;

import com.civa.app.domain.Bus;
import com.civa.app.domain.Category;
import com.civa.app.domain.Driver;
import com.civa.app.dto.BusRequestDto;
import com.civa.app.dto.BusResponseDTO;
import com.civa.app.exception.ResourceNotFoundException;
import com.civa.app.mapper.BusMapper;
import com.civa.app.repository.BusRepository;

import net.bytebuddy.asm.Advice.AssignReturned.AsScalar;

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

           assertEquals("Evento no encontrado con id:99", thrown.getMessage());

           verify(busRepository, times(1)).findById(99L);
    }



























    }
      



