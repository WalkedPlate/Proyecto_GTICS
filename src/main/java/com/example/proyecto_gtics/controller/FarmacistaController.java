package com.example.proyecto_gtics.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FarmacistaController {

    @GetMapping(value ={"/farmacista"})
    public String paginaPrincipal(){
        return "Farmacista/index";
    }

    @GetMapping(value ={"/farmacista/ordenes-linea"})
    public String ordenesLinea(){
        return "Farmacista/OrdenesLinea";
    }

    @GetMapping(value ={"/farmacista/ordenes-venta"})
    public String ordenesVenta(){
        return "Farmacista/OrdenesVenta";
    }

    @GetMapping(value ={"/farmacista/ordenes-linea/ver-orden"})
    public String verOrden(){
        return "Farmacista/verOrden";
    }

    @GetMapping(value ={"/farmacista/ordenes-venta/ver-orden"})
    public String verOrdenVenta(){
        return "Farmacista/verOrdenVenta";
    }

    @GetMapping(value ={"/farmacista/chat"})
    public String chat(){
        return "Farmacista/Chat";
    }

    //No hay vista de <<perfil>>
    @GetMapping(value ={"/farmacista/perfil"})
    public String perfil(){
        return "Farmacista/index";
    }





}
