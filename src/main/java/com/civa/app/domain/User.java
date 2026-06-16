package com.civa.app.domain;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "user")
public class User {
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id; 
    private String  name;
    private String username;
    private String email;
    private String password;
    
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Role> roles = new HashSet<>();
}
