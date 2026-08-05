package com.civa.app.security.controller;


import java.util.Collections;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.civa.app.domain.Role;
import com.civa.app.domain.User;
import com.civa.app.mapper.UserMapper;
import com.civa.app.repository.RoleRepository;
import com.civa.app.repository.UserRepository;
import com.civa.app.security.dto.JwtAuthResponseDto;
import com.civa.app.security.dto.LoginDto;
import com.civa.app.security.dto.RegisterDto;
import com.civa.app.security.jwt.JwtGenerator;

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtGenerator jwtGenerator;
    private final UserRepository userRepository;
    // private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;


    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponseDto> authenticateUser(@RequestBody LoginDto loginDto){
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
        );
     SecurityContextHolder.getContext().setAuthentication((authentication));
     String token = jwtGenerator.generateToken(authentication);

     return new ResponseEntity<>(new JwtAuthResponseDto(token), HttpStatus.OK);        
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterDto registerDto) {
        if(userRepository.existsByUsername(registerDto.getUsername())){
            return new ResponseEntity<>("Nombre de usuario, ya existe", HttpStatus.BAD_REQUEST);
        }      
        if(userRepository.existsByEmail(registerDto.getEmail())){
            return new ResponseEntity<>("El email, ya existe", HttpStatus.BAD_REQUEST);
        }     
                User user = userMapper.registerDtoToUser(registerDto);
                user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

                // Role roles = roleRepository.findByName("ROLE_USER").orElseThrow( ()-> new RuntimeException("Error, el rol no existes") ); 
                // user.setRoles(Collections.singleton(roles));

                userRepository.save(user);

                return new ResponseEntity<>("Usuario registrado", HttpStatus.CREATED);  
        }
    

}
