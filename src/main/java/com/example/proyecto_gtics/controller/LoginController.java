package com.example.proyecto_gtics.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class LoginController {

    @GetMapping(value ={"","/","/login"})
    public String login(){
        return "Login/inicioSesion";
    }


    @GetMapping(value ={"/registro"})
    public String registro(){
        return "Login/registro";
    }

    @GetMapping(value ={"/recuperar_cuenta"})
    public String recuperarCuenta(){
        return "Login/nuevaContra";
    }
}
