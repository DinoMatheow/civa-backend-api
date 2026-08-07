package com.civa.app.domain;

import java.time.LocalDate;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "driver")
public class Driver {
     
    @Id @GeneratedValue( strategy =  GenerationType.IDENTITY )
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    private String bio;

    @ManyToMany(mappedBy = "drivers")
    private Set<Bus> buses;

    
}
