package com.civa.app.domain;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "bus")
public class Bus { 

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long  id;

    @Column(name = "number_bus", unique = true, nullable = false)
    private String numberBus;

    @Column(name = "plate", unique = true, nullable = false)
    private String plate;
    
    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime  createdAt;
    
    @Column(name = "attributes", nullable = false)
    private String attributes;

    @Column(name = "status", nullable = false   )
    private Status status;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "marca_id", nullable = false)
    private MarcaBus marcaBus;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "bus_driver",
            joinColumns = @JoinColumn(name = "bus_id"),
            inverseJoinColumns = @JoinColumn(name = "driver_id")
    )
    private Set<Driver> drivers = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "category_id", nullable = true)
    private Category category;

    @ManyToMany(mappedBy = "attendeBus", fetch = FetchType.LAZY)
    private Set<User> attendedUsers = new HashSet<>();

    public void addDrivers(Driver driver) {
        this.drivers.add(driver);
        driver.getBuses().add(this);
    }
    public void removeDrivers(Driver driver) {
        this.drivers.remove(driver);
        driver.getBuses().remove(this);
    }



}