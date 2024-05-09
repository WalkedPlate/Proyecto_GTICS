package com.example.proyecto_gtics.controller;

import com.example.proyecto_gtics.entity.*;
import com.example.proyecto_gtics.repository.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class FarmacistaController {

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

    public FarmacistaController(CategoriasRepository categoriasRepository, DetallesOrdenRepository detallesOrdenRepository,
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

    @GetMapping(value ={"/farmacista"})
    public String paginaPrincipal(Model model){
        Sedes sede = sedesRepository.findByIdSedes(2);
        List<ProductosSedes> listaProductos = productosSedeRepository.findBySedes(sede);
        model.addAttribute("listaProductos",listaProductos);
        return "Farmacista/index";
    }

    @GetMapping(value ={"/farmacista/ordenes-linea"})
    public String ordenesLinea(Model model){

        Optional<TipoOrden> tipoOrden1 = tipoOrdenRepository.findById(3);
        Optional<TipoOrden> tipoOrden2 = tipoOrdenRepository.findById(4);
        Optional<EstadoOrden> estadoOrden = estadoOrdenRepository.findById(1);
        List<Ordenes> listaOrdenes = ordenesRepository.findByEstadoOrdenAndTipoOrdenOrTipoOrden(estadoOrden,tipoOrden1,tipoOrden2);
        model.addAttribute("listaOrdenes",listaOrdenes);
        return "Farmacista/OrdenesLinea";
    }

    @GetMapping(value ={"/farmacista/ordenes-venta"})
    public String ordenesVenta(Model model){
        List<Ordenes> listaOrdenes = ordenesRepository.encuentraOrdenesPorEstadosOrdenes(4,10,3,4,1);
        model.addAttribute("listaOrdenes",listaOrdenes);
        return "Farmacista/OrdenesVenta";
    }

    @PostMapping(value ={"/farmacista/ordenes-linea/ver-orden"})
    public String verOrden(Model model, @RequestParam("idOrden") Integer idOrden){
        Ordenes orden = ordenesRepository.findByIdordenes(idOrden);
        List<DetallesOrden> listaDetallesOrden = detallesOrdenRepository.findByOrdenes(orden);
        model.addAttribute("listaDetallesOrden",listaDetallesOrden);
        model.addAttribute("orden",orden);
        return "Farmacista/verOrdenLinea";
    }

    @PostMapping(value ={"/farmacista/ordenes-venta/ver-orden"})
    public String verOrdenVenta(Model model, @RequestParam("idOrden") Integer idOrden){
        Ordenes orden = ordenesRepository.findByIdordenes(idOrden);
        List<DetallesOrden> listaDetallesOrden = detallesOrdenRepository.findByOrdenes(orden);
        model.addAttribute("listaDetallesOrden",listaDetallesOrden);
        model.addAttribute("orden",orden);
        return "Farmacista/verOrdenVenta";
    }

    @GetMapping(value ={"/farmacista/chat"})
    public String chat(){
        return "Farmacista/Chat";
    }


    @GetMapping(value ={"/farmacista/perfil"})
    public String perfil(){
        return "Farmacista/perfil";
    }

    @GetMapping(value ={"/farmacista/editar-perfil"})
    public String editarPerfil(){
        return "farmacista/editarPerfil";
    }

    @GetMapping(value ={"/farmacista/cambiar-contra"})
    public String cambiarContra(){
        return "farmacista/cambiarContra";
    }




}
