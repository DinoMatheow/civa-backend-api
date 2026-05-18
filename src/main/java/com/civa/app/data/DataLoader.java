package com.civa.app.data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.civa.app.domain.Bus;
import com.civa.app.domain.MarcaBus;
import com.civa.app.domain.Status;
import com.civa.app.repository.BusRepository;
import com.civa.app.repository.MarcaBusRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final BusRepository busRepository;
    private final MarcaBusRepository marcaBusRepository;
    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if(busRepository.count()== 0){

            MarcaBus marcaBus = new MarcaBus();
            marcaBus.setName("Toyota");
            marcaBusRepository.save(marcaBus);

            List<Bus> buses = new ArrayList<>();
            for ( int i = 1; i <= 60; i++){
                Bus bus = new Bus();
                bus.setNumberBus("Bus #" + String.format("%03d", i) );
                bus.setAttributes("Actulizando...");
                bus.setPlate("ABC-" + (100 + i));
                bus.setStatus(Status.ACTIVO);
                bus.setMarcaBus(marcaBus);
            
                buses.add(bus);
            } 
            busRepository.saveAll(buses);
            System.out.println("Insertando" + buses.size() + "buses" );
        }

    }



}
