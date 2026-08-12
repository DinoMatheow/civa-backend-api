 package com.civa.app.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.civa.app.domain.Bus;
import com.civa.app.domain.Category;
import com.civa.app.domain.Driver;
import com.civa.app.domain.MarcaBus;
import com.civa.app.domain.Role;
import com.civa.app.domain.Status;
import com.civa.app.domain.User;
import com.civa.app.repository.BusRepository;
import com.civa.app.repository.CategoryRepository;
import com.civa.app.repository.DriverRepository;
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
    private final CategoryRepository categoryRepository;
    private final DriverRepository driverRepository;

    
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

     if(userRepository.findByUsername("user").isEmpty()){
        User regularUser = new User();
        regularUser.setName("Usuario");
        regularUser.setUsername("user");
        regularUser.setEmail("user@gmail.com");
        regularUser.setPassword(passwordEncoder.encode("user12345"));

        Set<Role> userRoles = new HashSet<>();
        userRoles.add(userRole);
        regularUser.setRoles(userRoles);
        
        userRepository.save(regularUser);
        System.out.println("Usuario 'user' creado.");
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
        
        if (!categoryRepository.existsByName("Conferencia")) {
            Category conferencia = new Category(null, "Conferencia", "Eventos de gran escala con múltiples oradores.");
            categoryRepository.save(conferencia);
        }
        if (!categoryRepository.existsByName("Taller")) {
            Category taller = new Category(null, "Taller", "Eventos interactivos y prácticos.");
            categoryRepository.save(taller);
        }
        if (!categoryRepository.existsByName("Webinar")) {
            Category webinar = new Category(null, "Webinar", "Seminarios online en vivo.");
            categoryRepository.save(webinar);
        }

        // --- 5. Crear y Guardar Oradores si no existen ---
        if (!driverRepository.existsByEmail("john.doe@example.com")) {
            Driver john = new Driver(null, "John Doe", "john.doe@example.com", "Experto en desarrollo de software.", new HashSet<>());
            driverRepository.save(john);
        }
        if (!driverRepository.existsByEmail("jane.smith@example.com")) {
            Driver jane = new Driver(null, "Jane Smith", "jane.smith@example.com", "Especialista en marketing digital.", new HashSet<>());
            driverRepository.save(jane);
        }
         



    }





}
