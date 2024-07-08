package com.example.proyecto_gtics.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TemporarlController {

    @GetMapping(value = "javier")
    public String temporarl() {
        return "index";
    }

}
