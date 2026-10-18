package com.civa.app.data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.civa.app.domain.Bus;
import com.civa.app.domain.BusStatusEnum;
import com.civa.app.domain.Category;
import com.civa.app.domain.City;
import com.civa.app.domain.Driver;
import com.civa.app.domain.MarcaBus;
import com.civa.app.domain.Role;
import com.civa.app.domain.Trip;
import com.civa.app.domain.TripStatusEnum;
import com.civa.app.domain.User;
import com.civa.app.repository.BusRepository;
import com.civa.app.repository.CategoryRepository;
import com.civa.app.repository.CityRepository;
import com.civa.app.repository.DriverRepository;
import com.civa.app.repository.MarcaBusRepository;
import com.civa.app.repository.RoleRepository;
import com.civa.app.repository.TripRepository;
import com.civa.app.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
    private final CityRepository cityRepository;
    private final TripRepository tripRepository;

    @Override
    @Transactional
    public void run(String... args) {
        loadUsers();

        List<MarcaBus> marcas = loadMarcas();
        List<Category> categories = loadCategories();
        List<Driver> drivers = loadDrivers();
        loadBuses(marcas, categories, drivers);

        Map<String, City> cities = loadCities();
        loadTrips(cities);
    }

    // ---------- Usuarios y roles ----------
    private void loadUsers() {
        Role adminRole = findOrCreateRole("ROLE_ADMIN");
        Role userRole = findOrCreateRole("ROLE_USER");

        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setName("Administrador");
            admin.setUsername("admin");
            admin.setEmail("admin@gmail.com");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRoles(new HashSet<>(Set.of(adminRole, userRole)));
            userRepository.save(admin);
            log.info("Usuario 'admin' creado.");
        }

        if (userRepository.findByUsername("user").isEmpty()) {
            User user = new User();
            user.setName("Usuario");
            user.setUsername("user");
            user.setEmail("user@gmail.com");
            user.setPassword(passwordEncoder.encode("user12345"));
            user.setRoles(new HashSet<>(Set.of(userRole)));
            userRepository.save(user);
            log.info("Usuario 'user' creado.");
        }
    }

    private Role findOrCreateRole(String name) {
        return roleRepository.findByName(name).orElseGet(() -> {
            Role role = new Role();
            role.setName(name);
            return roleRepository.save(role);
        });
    }

    // ---------- Marcas ----------
    private List<MarcaBus> loadMarcas() {
        if (marcaBusRepository.count() == 0) {
            for (String name : List.of("Volvo", "Scania", "Mercedes-Benz")) {
                MarcaBus marca = new MarcaBus();
                marca.setName(name);
                marcaBusRepository.save(marca);
            }
        }
        return marcaBusRepository.findAll();
    }

    // ---------- Categorías de bus ----------
    private List<Category> loadCategories() {
        if (categoryRepository.count() == 0) {
            categoryRepository.save(new Category(null, "Económico", "Asientos estándar."));
            categoryRepository.save(new Category(null, "Semi cama", "Asientos reclinables a 140°."));
            categoryRepository.save(new Category(null, "Cama", "Asientos reclinables a 180°."));
        }
        return categoryRepository.findAll();
    }

    // ---------- Conductores ----------
    private List<Driver> loadDrivers() {
        if (driverRepository.count() == 0) {
            driverRepository.save(new Driver(null, "Carlos Ramírez", "carlos.ramirez@civa.com",
                    "15 años de experiencia en rutas de sierra.", new HashSet<>()));
            driverRepository.save(new Driver(null, "Luis Torres", "luis.torres@civa.com",
                    "Especialista en rutas de costa.", new HashSet<>()));
            driverRepository.save(new Driver(null, "Miguel Quispe", "miguel.quispe@civa.com",
                    "Conductor de largo recorrido.", new HashSet<>()));
            driverRepository.save(new Driver(null, "José Flores", "jose.flores@civa.com",
                    "Instructor de manejo defensivo.", new HashSet<>()));
        }
        return driverRepository.findAll();
    }

    // ---------- Buses ----------
    private void loadBuses(List<MarcaBus> marcas, List<Category> categories, List<Driver> drivers) {
        if (busRepository.count() > 0) return;

        List<Bus> buses = new ArrayList<>();
        for (int i = 1; i <= 60; i++) {
            Bus bus = new Bus();
            bus.setNumberBus("Bus #" + String.format("%03d", i));
            bus.setPlate("ABC-" + (100 + i));
            bus.setAttributes("Aire acondicionado, baño, WiFi");
            bus.setStatus(BusStatusEnum.ACTIVO);
            bus.setMarcaBus(marcas.get(i % marcas.size()));
            bus.setCategory(categories.get(i % categories.size()));
            bus.setDrivers(new HashSet<>(Set.of(drivers.get(i % drivers.size()))));
            buses.add(bus);
        }
        busRepository.saveAll(buses);
        log.info("Insertados {} buses.", buses.size());
    }

    // ---------- Ciudades ----------
    private Map<String, City> loadCities() {
        if (cityRepository.count() == 0) {
            for (String name : List.of("Lima", "Arequipa", "Trujillo", "Cusco")) {
                City city = new City();
                city.setName(name);
                cityRepository.save(city);
            }
        }
        return cityRepository.findAll().stream()
                .collect(Collectors.toMap(City::getName, Function.identity()));
    }

    // ---------- Viajes ----------
    private void loadTrips(Map<String, City> cities) {
        if (tripRepository.count() > 0) return;

        List<Bus> buses = busRepository.findAll();

        // origen, destino, horas de viaje, precio
        String[][] routes = {
                {"Lima", "Arequipa", "16", "85.00"},
                {"Lima", "Trujillo", "8", "50.00"},
                {"Lima", "Cusco", "21", "120.00"}
        };
        int[] departureHours = {8, 14, 21};

        List<Trip> trips = new ArrayList<>();
        int busIndex = 0;

        for (int day = 1; day <= 7; day++) {
            LocalDate date = LocalDate.now().plusDays(day);

            for (String[] route : routes) {
                City a = cities.get(route[0]);
                City b = cities.get(route[1]);
                int hours = Integer.parseInt(route[2]);
                BigDecimal price = new BigDecimal(route[3]);

                for (int hour : departureHours) {
                    LocalDateTime departure = date.atTime(hour, 0);

                    trips.add(buildTrip(a, b, departure, hours, price, buses.get(busIndex++ % buses.size())));
                    trips.add(buildTrip(b, a, departure, hours, price, buses.get(busIndex++ % buses.size())));
                }
            }
        }
        tripRepository.saveAll(trips);
        log.info("Insertados {} viajes.", trips.size());
    }

    private Trip buildTrip(City origin, City destination, LocalDateTime departure,
                           int hours, BigDecimal price, Bus bus) {
        Trip trip = new Trip();
        trip.setOrigin(origin);
        trip.setDestination(destination);
        trip.setBus(bus);
        trip.setDepartureTime(departure);
        trip.setArrivalTime(departure.plusHours(hours));
        trip.setPrice(price);
        trip.setAvailableSeats(40);
        trip.setStatus(TripStatusEnum.PROGRAMADO);
        trip.setTripCode(code(origin) + "-" + code(destination) + "-"
                + departure.toLocalDate().toString().replace("-", "")
                + "-" + String.format("%02d", departure.getHour()) + "00");
        return trip;
    }

    private String code(City city) {
        return city.getName().substring(0, 3).toUpperCase();
    }
}