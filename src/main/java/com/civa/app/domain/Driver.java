package com.civa.app.domain;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
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
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Bus> buses;

    
}
