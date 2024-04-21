package com.example.proyecto_gtics.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SuperadminController {


    @GetMapping(value ={"/superadmin","/superadmin/administradores-sede"})
    public String dashboard(){
        return "Superadmin/index";
    }

    @GetMapping(value ={"/superadmin/inventario"})
    public String inventario(){
        return "Superadmin/inventario";
    }

    @GetMapping(value ={"/superadmin/inventario/estado-reposicion"})
    public String estadoReposiciones(){
        return "Superadmin/estadoReposicion";
    }

    @GetMapping(value ={"/superadmin/inventario/restricciones"})
    public String restricciones(){
        return "Superadmin/restricciones";
    }

    @GetMapping(value ={"/superadmin/solicitudes-reposicion"})
    public String solicitudesReposicion(){
        return "Superadmin/soliReposicion";
    }

    @GetMapping(value ={"/superadmin/farmacistas"})
    public String farmacistas(){
        return "Superadmin/farmacistas";
    }

    @GetMapping(value ={"/superadmin/doctores"})
    public String doctores(){
        return "Superadmin/doctores";
    }

    @GetMapping(value ={"/superadmin/pacientes"})
    public String pacientes(){
        return "Superadmin/pacientes";
    }

    @GetMapping(value ={"/superadmin/perfil"})
    public String perfil(){
        return "Superadmin/editarPerfil";
    }

    @GetMapping(value ={"/superadmin/editar-perfil"})
    public String editarPerfil(){
        return "Superadmin/editar";
    }

    @GetMapping(value ={"/superadmin/cambiar-contra"})
    public String cambiarContra(){
        return "Superadmin/cambiarContra";
    }



}
