package com.civa.app.domain;

import java.sql.Date;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
    @Column(name = "number_bus", unique = true)
    private String numberBus;

    private String plate;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime  createdAt;

    private String attributes;

    private Status status;


    @ManyToOne
    @JoinColumn(name = "marca_id", nullable = false)
    private MarcaBus marcaBus;


}