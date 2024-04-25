package com.example.proyecto_gtics.controller;

import com.example.proyecto_gtics.repository.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FarmaciaWebVentaController {

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

    public FarmaciaWebVentaController(CategoriasRepository categoriasRepository, DetallesOrdenRepository detallesOrdenRepository,
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

    @GetMapping(value ={"/clinicarenacer"})
    public String paginaPrincipal(){
        return "FarmaciaWebVenta/index";
    }

    @GetMapping(value ={"/clinicarenacer/paciente"})
    public String paginaPrincipalConLogin(){
        return "FarmaciaWebVenta/user";
    }

    @GetMapping(value ={"/clinicarenacer/categoria"})
    public String ordenarPorCategoria(){
        return "FarmaciaWebVenta/PorCategoria";
    }

    @GetMapping(value ={"/clinicarenacer/categoria/subcategoria"})
    public String ordenarPorSubcategoria(){
        return "FarmaciaWebVenta/porSubcategoria";
    }

    @GetMapping(value ={"/clinicarenacer/paciente/chatfarmacista"})
    public String chatFarmacista(){
        return "FarmaciaWebVenta/chat";
    }

    @GetMapping(value ={"/clinicarenacer/producto"})
    public String producto(){
        return "FarmaciaWebVenta/producto";
    }

    @GetMapping(value ={"/clinicarenacer/paciente/carrito"})
    public String carrito(){
        return "FarmaciaWebVenta/carrito";
    }

}
