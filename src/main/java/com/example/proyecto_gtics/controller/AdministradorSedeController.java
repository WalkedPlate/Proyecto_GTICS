package com.example.proyecto_gtics.controller;


import com.example.proyecto_gtics.entity.Ordenes;
import com.example.proyecto_gtics.entity.TipoOrden;
import com.example.proyecto_gtics.entity.TipoUsuario;
import com.example.proyecto_gtics.entity.Usuarios;
import com.example.proyecto_gtics.repository.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

@Controller
public class AdministradorSedeController {

    final CategoriasRepository categoriasRepository;
    final DetallesOrdenRepository detallesOrdenRepository;
    final EstadoOrdenRepository estadoOrdenRepository;
    final EstadoUsuarioRepository estadoUsuarioRepository;
    final OrdenesRepository ordenesRepository;
    final PreferenciasUsuarioRepository preferenciasUsuarioRepository;
    final ProductosRepository productosRepository;
    final ProductosSedeRepository productosSedeRepository;
    final SedesRepository sedesRepository;
    final TipoCobroRepository tipoCobroRepository;
    final TipoOrdenRepository tipoOrdenRepository;
    final TipoUsuarioRepository tipoUsuarioRepository;
    final UsuariosRepository usuariosRepository;

    public AdministradorSedeController(CategoriasRepository categoriasRepository, DetallesOrdenRepository detallesOrdenRepository,
                                       EstadoOrdenRepository estadoOrdenRepository, EstadoUsuarioRepository estadoUsuarioRepository,
                                       OrdenesRepository ordenesRepository, PreferenciasUsuarioRepository preferenciasUsuarioRepository,
                                       ProductosRepository productosRepository, ProductosSedeRepository productosSedeRepository,
                                       SedesRepository sedesRepository, TipoCobroRepository tipoCobroRepository, TipoOrdenRepository tipoOrdenRepository,
                                       TipoUsuarioRepository tipoUsuarioRepository, UsuariosRepository usuariosRepository){

        this.categoriasRepository = categoriasRepository;
        this.detallesOrdenRepository = detallesOrdenRepository;
        this.estadoOrdenRepository = estadoOrdenRepository;
        this.estadoUsuarioRepository = estadoUsuarioRepository;
        this.ordenesRepository = ordenesRepository;
        this. preferenciasUsuarioRepository = preferenciasUsuarioRepository;
        this.productosRepository = productosRepository;
        this.productosSedeRepository = productosSedeRepository;
        this.sedesRepository = sedesRepository;
        this.tipoCobroRepository = tipoCobroRepository;
        this.tipoOrdenRepository = tipoOrdenRepository;
        this.tipoUsuarioRepository = tipoUsuarioRepository;
        this.usuariosRepository = usuariosRepository;
    }



    @GetMapping(value ={"/administradorsede"})
    public String paginaPrincipal(){
        return "AdministradorSede/index";
    }


    @GetMapping(value ={"/administradorsede/ordenes-reposicion"})
    public String ordenesReposicion(Model model){
        Usuarios adminsede = usuariosRepository.findById(12).get();
        TipoOrden orden = tipoOrdenRepository.findById(2).get();
        List<Ordenes> listOrdenesReposicion = ordenesRepository.findByTipoOrdenAndUsuarios(orden,adminsede);
        model.addAttribute("listaOrdenesReposicion",listOrdenesReposicion);
        return "AdministradorSede/ordenesReposicion";

    }


    @GetMapping(value ={"/administradorsede/doctores"})
    public String doctores(Model model){

        TipoUsuario doctor = tipoUsuarioRepository.findById("Doctor").get();
        List<Usuarios> listaDoctores = usuariosRepository.findByTipoUsuario(doctor);

        model.addAttribute("listaDoctores",listaDoctores);

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
