package com.example.proyecto_gtics.controller;


import com.example.proyecto_gtics.entity.Usuarios;
import com.example.proyecto_gtics.repository.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class LoginController {

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

    public LoginController(CategoriasRepository categoriasRepository, DetallesOrdenRepository detallesOrdenRepository,
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

    @GetMapping(value ={"","/","/login"})
    public String login(){
        return "Login/inicioSesion";
    }

    /*Inicio para SuperAdmin*/
    @GetMapping(value ={"/loginSuperAdmin"})
    public String loginSuperAdmin(){
        return "Login/inicioSuperAdmin";
    }

    @GetMapping(value ={"/registro"})
    public String registro(){
        return "Login/registro";
    }

    @PostMapping(value = {"/guardarPaciente"})
    public  String guardarPaciente(Usuarios paciente, @RequestParam("nombres") String nombres , @RequestParam("apellidos") String apellidos, RedirectAttributes attr){

        //Comprobar si existe el paciente:
        Optional<Usuarios> consultaPaciente = usuariosRepository.findByDni(paciente.getDni());

        if(consultaPaciente.isPresent()){
            attr.addFlashAttribute("err","Ya existe un usuario con los datos ingresados.");
            return "redirect:/login";
        }else {
            paciente.setNombre(nombres + ' ' + apellidos);
            paciente.setEstadoUsuario(estadoUsuarioRepository.findById("Activo").get());
            paciente.setContrasena("Temporal_password");
            paciente.setTipoUsuario(tipoUsuarioRepository.findById("Paciente").get());
            usuariosRepository.save(paciente);
            attr.addFlashAttribute("msg","Registro exitoso, se le enviarán sus credenciales por correo.");
        }
        return "redirect:/login";
    }

    @GetMapping(value ={"/cambiar-contrasena"})
    public String recuperarCuenta(){
        return "Login/nuevaContra";
    }
}
