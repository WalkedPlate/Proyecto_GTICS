package com.example.proyecto_gtics.controller;


import com.example.proyecto_gtics.entity.*;
import com.example.proyecto_gtics.repository.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        Usuarios adminSede = usuariosRepository.findById(12).get(); //Admin de sede logueado
        model.addAttribute("adminSede",adminSede);

        TipoOrden orden = tipoOrdenRepository.findById(2).get(); //Tipo de orden: Orden de reposición
        List<Ordenes> listOrdenesReposicion = ordenesRepository.findByTipoOrdenAndUsuarios(orden,adminSede);
        model.addAttribute("listaOrdenesReposicion",listOrdenesReposicion);

        //Pasando medicinas de la sede:
        List<ProductosSedes> listMedicinas = productosSedeRepository.findBySedes(adminSede.getSedes());
        model.addAttribute("listaMedicinas",listMedicinas);

        return "AdministradorSede/ordenesReposicion";

    }

    @PostMapping(value ={ "/administradorsede/guardarorden-reposicion"})
    public String guardarOrdenReposicion(@RequestParam("idProducto") int id,@RequestParam("fechaEntrega") String fechaEntrega,DetallesOrden detallesOrden){

        Usuarios adminSede = usuariosRepository.findById(12).get();//Admin de sede logueado

        Productos productos = productosRepository.findById(id).get();
        Integer hola = 5;
        Optional<Ordenes> ordenes = ordenesRepository.findById(hola);
        if(ordenes.isPresent()){
            detallesOrden.setOrdenes(ordenes.get());
        }
        else{
            System.out.println("hola");
        }
        Ordenes orden1 = new Ordenes();
        orden1.setIdordenes(6);
        detallesOrden.setOrdenes(orden1);
        detallesOrden.setProductos(productos);


        /*detallesOrden.getOrdenes().setUsuarios(adminSede);
        detallesOrden.getOrdenes().getTipoOrden().setIdTipoOrden(2);
        detallesOrden.getOrdenes().setCodigo("0494870");
        detallesOrden.getOrdenes().setFechaEntrega(fechaEntrega);*/

        detallesOrdenRepository.save(detallesOrden);
        return "redirect:/administradorsede/ordenes-reposicion";
    }


    @GetMapping(value ={"/administradorsede/doctores"})
    public String doctores(Model model){

        Usuarios adminSede = usuariosRepository.findById(12).get();//Admin de sede logueado

        TipoUsuario doctor = tipoUsuarioRepository.findById("Doctor").get(); //Tipo de usuario: Doctor
        List<Usuarios> listaDoctores = usuariosRepository.findByTipoUsuarioAndSedes(doctor,adminSede.getSedes());

        model.addAttribute("listaDoctores",listaDoctores);
        model.addAttribute("adminSede",adminSede);

        return "AdministradorSede/doctores";
    }


    @GetMapping(value ={"/administradorsede/farmacistas"})
    public String farmacistas(Model model){

        Usuarios adminSede = usuariosRepository.findById(12).get();//Admin de sede logueado

        TipoUsuario farmacista = tipoUsuarioRepository.findById("Farmacista").get(); //Tipo de usuario: Farmacista
        List<Usuarios> listaFarmacistas = usuariosRepository.findByTipoUsuarioAndSedes(farmacista,adminSede.getSedes());

        model.addAttribute("listaFarmacistas",listaFarmacistas);
        model.addAttribute("adminSede",adminSede);


        return "AdministradorSede/farmacistas";
    }

    @PostMapping(value = "/administradorsede/guardarfarmacista")
    public String guardarFarmacista(Usuarios usuarios,@RequestParam("idSedes") int id){

        Sedes sedes = sedesRepository.findById(id).get();
        usuarios.setSedes(sedes);
        usuarios.setContrasena("Temporal_password");
        usuarios.setEstadoUsuario(estadoUsuarioRepository.findById("En revisión").get());
        usuarios.setTipoUsuario(tipoUsuarioRepository.findById("Farmacista").get());
        usuariosRepository.save(usuarios);
        return "redirect:/administradorsede/farmacistas";
    }

    @GetMapping("/administradorsede/borrarfarmacista")
    public String borrarFarmacista(@RequestParam("idFarmacista") Integer id){
        Optional<Usuarios> optSede =usuariosRepository.findById(id);
        if(optSede.isPresent()){
            usuariosRepository.deleteById(id);
        }
        return "redirect:/administradorsede/farmacistas";

    }


    @GetMapping(value ={"/administradorsede/medicinas"})
    public String medicinas(Model model){

        Usuarios adminSede = usuariosRepository.findById(12).get();//Admin de sede logueado
        List<ProductosSedes> listMedicinas = productosSedeRepository.findBySedes(adminSede.getSedes());
        model.addAttribute("listaMedicinas",listMedicinas);

        return "AdministradorSede/medicinas";
    }


    @GetMapping(value ={"/administradorsede/perfil"})
    public String perfil(){
        return "AdministradorSede/perfil";
    }




}
