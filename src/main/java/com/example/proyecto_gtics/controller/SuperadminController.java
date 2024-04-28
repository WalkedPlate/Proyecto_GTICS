package com.example.proyecto_gtics.controller;


import com.example.proyecto_gtics.entity.EstadoUsuario;
import com.example.proyecto_gtics.entity.TipoUsuario;
import com.example.proyecto_gtics.entity.Usuarios;
import com.example.proyecto_gtics.repository.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class SuperadminController {

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

    public SuperadminController(CategoriasRepository categoriasRepository, DetallesOrdenRepository detallesOrdenRepository,
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


    @GetMapping(value ={"/superadmin","/superadmin/administradores-sede"})
    public String dashboard(Model model){
        TipoUsuario adminSede = tipoUsuarioRepository.findById("AdministradorDeSede").get();
        EstadoUsuario estado = estadoUsuarioRepository.findById("Activo").get();
        List<Usuarios> listaAdminSede = usuariosRepository.findByTipoUsuarioAndEstadoUsuario(adminSede, estado);
        model.addAttribute("listaAdminSede", listaAdminSede);
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
    public String farmacistas(Model model){
        TipoUsuario farmacista = tipoUsuarioRepository.findById("Farmacista").get();
        EstadoUsuario estado = estadoUsuarioRepository.findById("Activo").get();
        List<Usuarios> listaFarmacistas = usuariosRepository.findByTipoUsuarioAndEstadoUsuario(farmacista, estado);
        model.addAttribute("listaFarmacistas", listaFarmacistas);
        return "Superadmin/farmacistas";
    }

    @GetMapping(value ={"/superadmin/farmacistas/solicitudes"})
    public String soliFarmacistas(){
        return "Superadmin/soliFarmacistas";
    }

    @GetMapping(value ={"/superadmin/doctores"})
    public String doctores(Model model){

        TipoUsuario doctores = tipoUsuarioRepository.findById("Doctor").get();
        EstadoUsuario estado = estadoUsuarioRepository.findById("Activo").get();
        List<Usuarios> listaDoctores = usuariosRepository.findByTipoUsuarioAndEstadoUsuario(doctores,estado);
        model.addAttribute("listaDoctores",listaDoctores);
        return "Superadmin/doctores";
    }

    @GetMapping(value ={"/superadmin/pacientes"})
    public String pacientes(Model model){

        TipoUsuario paciente = tipoUsuarioRepository.findById("Paciente").get();
        EstadoUsuario estado = estadoUsuarioRepository.findById("Activo").get();
        List<Usuarios> listaPaciente = usuariosRepository.findByTipoUsuarioAndEstadoUsuario(paciente,estado);
        model.addAttribute("listaPacientes",listaPaciente);
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
