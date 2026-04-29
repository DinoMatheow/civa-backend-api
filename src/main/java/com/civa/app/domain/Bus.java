package com.civa.app.domain;

import java.time.LocalDateTime;

import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marca_id", nullable = false)
    private MarcaBus marcaBus;


}