package com.civa.app.service;

import java.util.List;


import com.civa.app.domain.Bus;


public interface IBusService {
    
    List<Bus> findAll();
    Bus findById(Long id);
}
