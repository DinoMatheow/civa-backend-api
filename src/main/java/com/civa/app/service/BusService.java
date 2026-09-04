package com.civa.app.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.civa.app.domain.Bus;
import com.civa.app.domain.Category;
import com.civa.app.domain.Driver;
import com.civa.app.domain.User;
import com.civa.app.dto.BusRequestDto;
import com.civa.app.dto.BusResponseDTO;
import com.civa.app.mapper.BusMapper;
import com.civa.app.repository.BusRepository;

import lombok.RequiredArgsConstructor;
import  com.civa.app.exception.ResourceNotFoundException;

@Service
@RequiredArgsConstructor
public class BusService implements IBusService {
    
    private final BusRepository busRepository;
    private final BusMapper busMapper;
    private final CategoryService categoryService;
    private final DriverService driverService;

    

    @Override
    @Transactional(readOnly = true)
    public Page<BusResponseDTO> findAll(String numberBus, Pageable pageable) {
        Page<Bus> busPage;
        if(numberBus !=null && !numberBus.trim().isEmpty()){
            busPage = busRepository.findByNumberBusContainingIgnoreCase(numberBus, pageable);
        }else {
            busPage = busRepository.findAll(pageable);
        }

        List<BusResponseDTO> dtos = busPage.getContent().stream()
        .map(busMapper::toBusResponseDTO)
        .toList();

        return new PageImpl<>(dtos, pageable, busPage.getTotalElements());
        // return busRepository.findAll();
    }
    @Override
    @Transactional(readOnly = true)
    public Bus findById(Long id) {
        return busRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El ID: " + id  +" no se encontro " ));
    }
    @Override
    @Transactional
    public void deleteById(Long id) {
        Bus busToDelete = this.findById(id);
        busRepository.delete(busToDelete);
        // throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
    }
    @Override
    @Transactional
    public Bus save(BusRequestDto busRequestDto) {
        Bus bus = busMapper.toEntity(busRequestDto);
        Category category = categoryService.findById(busRequestDto.getCategoryBusId());
        bus.setCategory(category);

        if(busRequestDto.getDriversIds() != null && !busRequestDto.getDriversIds().isEmpty()){
            Set<Driver> drivers = busRequestDto.getDriversIds().stream()
                    .map(driverService::findById)
                    .collect(Collectors.toSet());
                    drivers.forEach(bus::addDrivers);
        }

        return busRepository.save(bus);
        // throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    @Override
    @Transactional
    public Bus update(Long id, BusRequestDto busRequestDto) {
        Bus existingBus =  busRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El ID: " + id  +" no se encontro " ));

        busMapper.updateBusFromDTO(busRequestDto, existingBus);


        if(!existingBus.getCategory().getId().equals(busRequestDto.getCategoryBusId())){
            Category category = categoryService.findById(busRequestDto.getCategoryBusId());
            existingBus.setCategory(category);
        }

        Set<Driver> updatedDrivers;
        if(busRequestDto.getDriversIds() != null && !busRequestDto.getDriversIds().isEmpty()){
            updatedDrivers = busRequestDto.getDriversIds().stream()
                    .map(driverService::findById)
                    .collect(Collectors.toSet());
        }else{
            updatedDrivers = new HashSet<>();
        }
        new HashSet<>(existingBus.getDrivers())
        .forEach(currentDriver ->{
            if(!updatedDrivers.contains(currentDriver)){
                existingBus.removeDrivers(currentDriver);
            }   
        });

        updatedDrivers.forEach(newDriver -> {
            if(!existingBus.getDrivers().contains(newDriver)){
                existingBus.addDrivers(newDriver);
            }
        });
        return busRepository.save(existingBus);
    }

    @Transactional
    public List<Bus> getAllBusAndTheirDetailsProblematic(){
        List<Bus> buses = busRepository.findAll();

        buses.forEach( bus -> {
            bus.getDrivers().size();
            bus.getDrivers().stream().map(Driver::getName).collect(Collectors.toSet());
            if (bus.getCategory() != null) {
            bus.getCategory().getName();
        }
            bus.getAttendedUsers().size();
        });

        return buses;
    }


    @Transactional(readOnly = true)
    public List<Bus> getAllBusAndTheirDetailsOptimizeWithJoinFetch(){
        List<Bus> buses = busRepository.findAllWithCategoryAndDrivers();

         buses.forEach( bus -> {
            bus.getDrivers().size();
            bus.getDrivers().stream().map(Driver::getName).collect(Collectors.toSet());
            if (bus.getCategory() != null) {
            bus.getCategory().getName();
        }
            bus.getAttendedUsers().size();
        }); 

        return buses;

    }


    @Transactional(readOnly = true)
    public List<Bus> getAllBusAndTheirDetailsOptimizeWithJoinFetchAllDetails(){
        List<Bus> buses = busRepository.findAllWithAllDetails();

         buses.forEach( bus -> {
            bus.getNumberBus();
            if(bus.getDrivers() != null && !bus.getDrivers().isEmpty()) {
                bus.getDrivers().stream().map(Driver::getName).collect(Collectors.joining(", "));
            }
            if (bus.getCategory() != null) {
            bus.getCategory().getName();
        }
        
            if(bus.getAttendedUsers() != null && !bus.getAttendedUsers().isEmpty()) {
                bus.getAttendedUsers().stream().map(User::getUsername).collect(Collectors.joining(", "));
            }
        }); 

        return buses;

    }



}