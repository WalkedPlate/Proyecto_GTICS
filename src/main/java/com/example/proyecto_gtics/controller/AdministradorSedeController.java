package com.example.proyecto_gtics.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdministradorSedeController {

    @GetMapping(value ={"/administradorsede"})
    public String paginaPrincipal(){
        return "AdministradorSede/index";
    }


    @GetMapping(value ={"/administradorsede/ordenes-reposicion"})
    public String ordenesReposicion(){
        return "AdministradorSede/ordenesReposicion";
    }


    @GetMapping(value ={"/administradorsede/doctores"})
    public String doctores(){
        return "AdministradorSede/doctores";
    }


    @GetMapping(value ={"/administradorsede/farmacistas"})
    public String farmacistas(){
        return "AdministradorSede/farmacistas";
    }


    @GetMapping(value ={"/administradorsede/medicinas"})
    public String medicinas(){
        return "AdministradorSede/medicinas";
    }


    @GetMapping(value ={"/administradorsede/perfil"})
    public String perfil(){
        return "AdministradorSede/perfil";
    }




}
