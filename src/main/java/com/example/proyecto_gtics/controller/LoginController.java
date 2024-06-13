package com.example.proyecto_gtics.controller;


import com.example.proyecto_gtics.dto.ResultDni;
import com.example.proyecto_gtics.entity.Usuarios;
import com.example.proyecto_gtics.repository.*;
import com.example.proyecto_gtics.service.DniService;
import com.example.proyecto_gtics.service.EmailService;
import com.example.proyecto_gtics.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.security.web.savedrequest.SimpleSavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.Authenticator;
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
    @Autowired
    private DniService dniService;


    @GetMapping(value ={"","/","/login"})
    public String login(@RequestParam(name = "logout",required = false) String logout, RedirectAttributes attr,
                        Authentication authentication, HttpServletRequest request, Model model,
                        HttpSession session){

/*
        if (authentication != null && authentication.isAuthenticated()) {
            SavedRequest savedRequest = (SavedRequest) session.getAttribute("SPRING_SECURITY_SAVED_REQUEST");
            String targetUrl = (savedRequest != null) ? savedRequest.getRedirectUrl() : "/";
            return "redirect:" + targetUrl;
        }
        SavedRequest savedRequest = new SimpleSavedRequest(request.getRequestURI());
        session.setAttribute("SPRING_SECURITY_SAVED_REQUEST", savedRequest);
*/
        if(true){
            attr.addFlashAttribute("msg","Sesión cerrada exitosamente.");
        }

        return "Login/inicioSesion";
    }


    @GetMapping(value ={"/registro"})
    public String registro(){
        return "Login/registro";
    }

    @PostMapping(value = {"/guardarPaciente"})
    public  String guardarPaciente(Usuarios paciente, BindingResult bindingResult, @RequestParam(name = "nombres", required = false) String nombres ,
                                   @RequestParam(name = "apellidos", required = false) String apellidos,
                                   HttpServletRequest request, RedirectAttributes attr){

        //Comprobar si existe el paciente:
        Optional<Usuarios> consultaPaciente = usuariosRepository.findByDni(paciente.getDni());
        //Comprobar si existe el paciente por correo:
        Optional<Usuarios> consultaPacienteCorreo = usuariosRepository.findByCorreo(paciente.getCorreo());
/*
        if(nombres.isEmpty() == null){
            attr.addFlashAttribute("err","Debe ingresar un nombre");
            return "redirect:/registro";
        }
        if(apellidos == null){
            attr.addFlashAttribute("err","Debe ingresar apellidos");
            return "redirect:/registro";
        }


        if (bindingResult.hasErrors()) {
            String error = bindingResult.getFieldError().getDefaultMessage();
            attr.addFlashAttribute("err",error);
            return "redirect:/registro";
        }
*/

        if(consultaPaciente.isPresent() || consultaPacienteCorreo.isPresent()){
            attr.addFlashAttribute("err","Ya existe un usuario con los datos ingresados.");
            return "redirect:/registro";
        }else {
            //Generación de token
            String token = tokenService.generateToken(paciente.getCorreo());
            String link = request.getScheme() + "://"+ request.getServerName()
                    + ":"+ request.getServerPort() +request.getContextPath()+ "/cambiar-contrasena?token=" + token;

            ResultDni resultDni = dniService.obtenerDatosPorDni(paciente.getDni().toString());
            if (resultDni == null || resultDni.getStatus() != 200) {
                attr.addFlashAttribute("err","DNI inválido");
                return "redirect:/registro";
            }


            paciente.setNombre(resultDni.getData().getNombres() + " " + resultDni.getData().getApellido_paterno() + " " + resultDni.getData().getApellido_materno());
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
            String pathToImage = "static/img/Login/icono.png";
            String imageId = "image001";
            emailService.sendEmail(to, subject, link, temporalPassword,pathToImage,imageId);

        }
        return "redirect:/login";
    }

    @PostMapping(value ={"/generarToken"})
    public String generarNuevoToken(@RequestParam(name = "email",required = false) String email, Model model,
                                  RedirectAttributes attr, HttpServletRequest request){

        if(email != null ){

            Optional<Usuarios> opt = usuariosRepository.findByCorreo(email);
            if(opt.isPresent()){
                Usuarios usuario = opt.get();

                //Generación de token
                String token = tokenService.generateToken(email);
                String link = request.getScheme() + "://"+ request.getServerName()
                        + ":"+ request.getServerPort() +request.getContextPath()+ "/cambiar-contrasena?token=" + token;

                if(usuario.getUsandoContrasenaTemporal()){

                    //Contraseña
                    String temporalPassword = Usuarios.generateTemporaryPassword(10);
                    String passwordEncriptada = passwordEncoder.encode(temporalPassword);
                    usuario.setContrasena(passwordEncriptada);
                    usuario.setToken(token);
                    usuariosRepository.save(usuario);

                    //Envío de correo con contraseña temporal
                    String to = email;
                    String subject = "Cambie su contraseña";
                    String pathToImage = "static/img/Login/icono.png";
                    String imageId = "image001";
                    emailService.sendEmail(to, subject, link, temporalPassword,pathToImage,imageId);
                    attr.addFlashAttribute("msg","Solictud válida, se le enviará un enlace por correo.");
                    return "redirect:/login";
                }
                else {
                    usuario.setToken(token);
                    usuariosRepository.save(usuario);

                    //Envío de correo con el link para cambiar su contraseña
                    String to = usuario.getCorreo();
                    String subject = "Reestablecer Contraseña";
                    String pathToImage = "static/img/Login/icono.png";
                    String imageId = "image001";
                    emailService.sendEmailPasswordChange(to, subject, link, "j",pathToImage,imageId);

                    attr.addFlashAttribute("msg","Solictud válida, se le enviará un enlace por correo.");
                    return "redirect:/login";

                }
            }
            else {
                attr.addFlashAttribute("err","Correo asociado al token inválido");
                return "redirect:/login";
            }
        }
        else {
            attr.addFlashAttribute("err","Correo asociado al token inválido");
            return "redirect:/login";
        }


    }

    @GetMapping(value ={"/cambiar-contrasena"})
    public String recuperarCuenta(@RequestParam(name = "token",required = false) String token, Model model,
                                  RedirectAttributes attr){

        if(token != null ){
            String email = tokenService.getEmailFromToken(token);
            if (email == null) {
                attr.addFlashAttribute("err","Token inválido.");
                return "redirect:/login";
            }
            model.addAttribute("email",email);

            if(tokenService.expiredToken(token)){
                model.addAttribute("generar",1);

                return "Login/nuevaContra";
            }
            model.addAttribute("generar",0);
            model.addAttribute("token", token);

            return "Login/nuevaContra";
        }
        else {
            attr.addFlashAttribute("err","Necesitas un token.");
            return "redirect:/login";
        }


    }

    @PostMapping("/cambiarContrasena")
    public String cambiarContrasena(@RequestParam(name = "token",required = false) String token, @RequestParam("pass1") String pass1,
                                    @RequestParam("pass2") String pass2, Model model,
                                    RedirectAttributes attr) {

        if(token != null ){
            String email = tokenService.getEmailFromToken(token);

            if (email == null) {
                attr.addFlashAttribute("err","Correo inválido.");
                return "redirect:/login";
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
        else{
            attr.addFlashAttribute("err","Error en el token");
            return "redirect:/login";
        }

    }

    @PostMapping("/recuperarCuenta")
    public String recuperarCuenta(@RequestParam(name = "email",required = false) String email, HttpServletRequest request,
                                    RedirectAttributes attr) {

        if(email != null ){

            Optional<Usuarios> opt = usuariosRepository.findByCorreo(email);

            if(opt.isPresent()){
                Usuarios usuario = opt.get();


                //Generación de token
                String token = tokenService.generateToken(usuario.getCorreo());
                String link = request.getScheme() + "://"+ request.getServerName()
                        + ":"+ request.getServerPort() +request.getContextPath()+ "/cambiar-contrasena?token=" + token;


                //Envío de correo con el link para cambiar su contraseña
                String to = usuario.getCorreo();
                String subject = "Reestablecer Contraseña";
                String pathToImage = "static/img/Login/icono.png";
                String imageId = "image001";
                emailService.sendEmailPasswordChange(to, subject, link, "j",pathToImage,imageId);

                attr.addFlashAttribute("msg","Solictud válida, se le enviará un enlace por correo.");
                return "redirect:/login";

            }
            else {
                attr.addFlashAttribute("err","Correo inválido");
                return "redirect:/login";
            }

        }
        else{
            //attr.addFlashAttribute("err","Tienes prohibido el acceso a esta vista.");
            return "redirect:/login";
        }

    }


    @GetMapping("/api/dni")
    public @ResponseBody ResultDni obtenerDatosDni(@RequestParam String dni) {
        String errorValidacion = dniService.validarDni(dni);
        if (errorValidacion != null) {
            ResultDni response = new ResultDni();
            response.setStatus(422);
            response.setMessage(errorValidacion);
            return response;
        }

        return dniService.obtenerDatosPorDni(dni);
    }

}
