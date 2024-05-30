package com.example.proyecto_gtics.controller;


import com.example.proyecto_gtics.entity.Usuarios;
import com.example.proyecto_gtics.repository.*;
import com.example.proyecto_gtics.service.EmailService;
import com.example.proyecto_gtics.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private EmailService emailService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TokenService tokenService;


    @GetMapping(value ={"","/","/login"})
    public String login(){
        return "Login/inicioSesion";
    }

    @GetMapping(value ={"/logout"})
    public String logout(){
        return "Login/cierreSesion";
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
            //Generación de token
            String token = tokenService.generateToken(paciente.getCorreo());
            String link = "http://localhost:8080/cambiar-contrasena?token=" + token;

            paciente.setNombre(nombres + ' ' + apellidos);
            paciente.setEstadoUsuario(estadoUsuarioRepository.findById("Activo").get());
            //Contraseña
            String temporalPassword = Usuarios.generateTemporaryPassword(10);
            String passwordEncriptada = passwordEncoder.encode(temporalPassword);
            paciente.setContrasena(passwordEncriptada);
            paciente.setTipoUsuario(tipoUsuarioRepository.findById("Paciente").get());
            paciente.setUsandoContrasenaTemporal(true);
            paciente.setToken(token);
            usuariosRepository.save(paciente);
            attr.addFlashAttribute("msg","Registro exitoso, se le enviarán sus credenciales por correo.");




            //Envío de correo con contraseña temporal
            String to = paciente.getCorreo();
            String subject = "Cambie su contraseña";
            emailService.sendEmail(to, subject, link, temporalPassword);

        }
        return "redirect:/login";
    }

    @GetMapping(value ={"/cambiar-contrasena"})
    public String recuperarCuenta(@RequestParam(name = "token",required = false) String token, Model model){

        if(token != null ){
            String email = tokenService.getEmailFromToken(token);
            if (email == null) {
                return "redirect:/login?errorToken";
            }

            model.addAttribute("token", token);

            return "Login/nuevaContra";
        }

        return "redirect:/login";
    }

    @PostMapping("/cambiarContrasena")
    public String cambiarContrasena(@RequestParam(name = "token",required = false) String token, @RequestParam("pass1") String pass1,
                                    @RequestParam("pass2") String pass2, Model model,
                                    RedirectAttributes attr) {

        if(token != null ){
            String email = tokenService.getEmailFromToken(token);

            if (email == null) {
                model.addAttribute("error", "Token inválido o expirado.");
                return "Login/nuevaContra";
            }

            Optional<Usuarios> optionalUsuario = usuariosRepository.findByCorreo(email);

            if (!optionalUsuario.isPresent()) {
                attr.addFlashAttribute("err","No existe un usuario con el correo asociado al token.");
                return "redirect:/login";
            }

            if(pass1.equalsIgnoreCase(pass2)){
                Usuarios usuario = optionalUsuario.get();
                usuario.setContrasena(passwordEncoder.encode(pass1));
                usuario.setUsandoContrasenaTemporal(false);
                usuariosRepository.save(usuario);
                tokenService.removeToken(token);
                attr.addFlashAttribute("msg","Cambio de contraseña exitoso");
                return "redirect:/login?sucess";
            }
            else {
                attr.addFlashAttribute("err","Las contraseñas no coinciden.");
                return "redirect:/cambiar-contrasena";
            }


        }

        return "redirect:/login";
    }



    @PostMapping(value = "login/validarCampos")
    public String validarCampos(@RequestParam("email") String correo, @RequestParam("password") String password, RedirectAttributes attr){

        Usuarios user = usuariosRepository.findByCorreo(correo).get();
        if(user == null){
            attr.addFlashAttribute("err","credenciales inválidas.");
            return "redirect:/login";
        }

        Integer idUser = user.getIdUsuario();
        if(user.getContrasena().equalsIgnoreCase(password)){

            switch (idUser) {
                case 1 -> {
                    return "redirect:/superadmin";
                }
                case 12 -> {
                    return "redirect:/administradorsede";
                }
                case 1027 -> {
                    return "redirect:/farmacista";
                }
                case 1002 -> {
                    return "redirect:/clinicarenacer";
                }
                default -> {
                    return "redirect:/login";
                }
            }

        }
        else{
            attr.addFlashAttribute("err","credenciales inválidas.");
            return "redirect:/login";
        }


    }
}
