package com.hlw.controller;


import com.hlw.service.AdministratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/administrator")
public class AdministratorController {
    @Autowired
    private AdministratorService administratorService;
}
