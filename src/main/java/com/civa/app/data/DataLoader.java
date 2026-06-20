 package com.civa.app.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.civa.app.domain.Bus;
import com.civa.app.domain.MarcaBus;
import com.civa.app.domain.Role;
import com.civa.app.domain.Status;
import com.civa.app.domain.User;
import com.civa.app.repository.BusRepository;
import com.civa.app.repository.MarcaBusRepository;
import com.civa.app.repository.RoleRepository;
import com.civa.app.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final BusRepository busRepository;
    private final MarcaBusRepository marcaBusRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    @Transactional
    public void run(String... args) throws Exception {
     
     Role adminRole =  roleRepository.findByName("ROLE_ADMIN")
            .orElseGet(()-> {
                Role newRole = new Role();
                newRole.setName("ROLE_ADMIN");
                return roleRepository.save(newRole);
            });

    Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(()-> {
                    Role newRole = new Role();
                    newRole.setName("ROLE_USER");
                    return roleRepository.save(newRole);
                });
    if(userRepository.findByUsername("admin").isEmpty()){
        User admin = new User();
        admin.setName("Administrador");
        admin.setUsername("admin");
        admin.setEmail("admin@gmail.com");
        admin.setPassword(passwordEncoder.encode("admin"));

        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(adminRole);
        adminRoles.add(userRole);

        admin.setRoles(adminRoles);
        userRepository.save(admin);
        System.out.println("Usuario 'admin' creado.");
    }
     
     
     
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
