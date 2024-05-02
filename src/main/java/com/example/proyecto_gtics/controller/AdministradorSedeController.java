package com.example.proyecto_gtics.controller;


import com.example.proyecto_gtics.entity.*;
import com.example.proyecto_gtics.repository.*;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    @GetMapping(value={"/administradorsede/nuevaOrden"})
    public String nuevaOrden(Model model){

        List<Productos> listaProductos = productosRepository.findAll();

        model.addAttribute("listaProductos",listaProductos);

        return "AdministradorSede/nuevaOrdenReposicion";
    }

    @GetMapping(value={"/administradorsede/verOrden"})
    public String verOrden(Model model, @RequestParam ("idOrdenRepo") Integer idOrdenRepo){

        Ordenes ordenReposicion = ordenesRepository.findById(idOrdenRepo).get();
        //Validamos que la orden sea de reposicion
        if(!validarTipoOrden(2,ordenReposicion)){
            return "redirect:/administradorsede/ordenes-reposicion";
        }

        List<DetallesOrden> listaDetallesOrden = detallesOrdenRepository.findByOrdenes(ordenReposicion);

        model.addAttribute("ordenReposicion",ordenReposicion);
        model.addAttribute("listaDetallesOrden",listaDetallesOrden);

        return "AdministradorSede/verOrdenReposicion";
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

    @GetMapping(value = {"/administradorsede/borrarorden-reposicion"})
    public String borrarOrdenReposicion(@RequestParam("idOrden") Integer id){
        Optional<Ordenes> optOrden =ordenesRepository.findById(id);
        if(optOrden.isPresent()){
            ordenesRepository.cambiarEstadoOrden(4,id); //Se cambia el estado de la orden a 4 (Eliminado)
        }

        return "redirect:/administradorsede/ordenes-reposicion";
    }

    @GetMapping(value ={"/administradorsede/doctores"})
    public String doctores(Model model){

        Usuarios adminSede = usuariosRepository.findById(12).get();//Admin de sede logueado
        EstadoUsuario estadoUsuario = estadoUsuarioRepository.findById("Activo").get();
        TipoUsuario doctor = tipoUsuarioRepository.findById("Doctor").get(); //Tipo de usuario: Doctor
        List<Usuarios> listaDoctores = usuariosRepository.findByTipoUsuarioAndSedesAndEstadoUsuario(doctor,adminSede.getSedes(),estadoUsuario);

        model.addAttribute("listaDoctores",listaDoctores);
        model.addAttribute("adminSede",adminSede);

        return "AdministradorSede/doctores";
    }


    @GetMapping(value ={"/administradorsede/farmacistas"})
    public String farmacistas(Model model){

        Usuarios adminSede = usuariosRepository.findById(12).get();//Admin de sede logueado
        EstadoUsuario estado = estadoUsuarioRepository.findById("activo").get();
        TipoUsuario farmacista = tipoUsuarioRepository.findById("Farmacista").get(); //Tipo de usuario: Farmacista
        List<Usuarios> listaFarmacistas = usuariosRepository.findByTipoUsuarioAndSedesAndEstadoUsuario(farmacista,adminSede.getSedes(),estado);

        model.addAttribute("listaFarmacistas",listaFarmacistas);
        model.addAttribute("adminSede",adminSede);


        return "AdministradorSede/farmacistas";
    }

    @PostMapping(value = "/administradorsede/guardarfarmacista")
    public String guardarFarmacista(Usuarios usuarios, @RequestParam("idSedes") int id, RedirectAttributes attr){

        Sedes sedes = sedesRepository.findById(id).get();
        if (sedes == null) {
            attr.addFlashAttribute("msg","error=sede_invalida");
            return "redirect:/administradorsede/farmacistas";
        }

        // Validación del nombre
        if (usuarios.getNombre() == null || usuarios.getNombre().isEmpty() || usuarios.getNombre().matches("^\\d.*$")) {
            attr.addFlashAttribute("msg","error=nombre_invalido");
            return "redirect:/administradorsede/farmacistas";
        } else if (!isValidEmail(usuarios.getCorreo())) {
            attr.addFlashAttribute("msg","error=correo_invalido");
            return "redirect:/administradorsede/farmacistas";
        } else if (!validarDNI(usuarios.getDni())) {
            attr.addFlashAttribute("msg","error=DNI_invalido");
            return "redirect:/administradorsede/farmacistas";
        } else if (!validarDNI(usuarios.getCodigoColegio())) {
            attr.addFlashAttribute("msg","error=codigo_invalido");
            return "redirect:/administradorsede/farmacistas";
        } else if (!validarCampo(usuarios.getDistritoResidencia())) {
            attr.addFlashAttribute("msg","error=direccion_invalido");
            return "redirect:/administradorsede/farmacistas";
        }

        usuarios.setSedes(sedes);
        usuarios.setContrasena("Temporal_password");
        usuarios.setEstadoUsuario(estadoUsuarioRepository.findById("En revisión").get());
        usuarios.setTipoUsuario(tipoUsuarioRepository.findById("Farmacista").get());
        usuariosRepository.save(usuarios);
        attr.addFlashAttribute("msg","Farmacista creado exitosamente");
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








/* VALIDACION DE CORREO
 */
private boolean isValidEmail(String email) {
    String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    Pattern pattern = Pattern.compile(emailRegex);
    Matcher matcher = pattern.matcher(email);
    return matcher.matches();
}

    public boolean validarDNI(String dni) {
        // Verifica si la cadena contiene solo dígitos y tiene un máximo de 7 caracteres
        return dni != null && dni.matches("\\d{1,8}");
    }

    public boolean validarCampo(String campo) {
        // Verifica si la cadena no es nula, no está vacía y su longitud es menor o igual a 255 caracteres
        return campo != null && !campo.trim().isEmpty() && campo.length() <= 255;
    }

    public boolean validarTipoOrden(Integer tipo_orden_idtipo_orden, Ordenes orden) {
        // Valida si una orden es de un tipo dado
        boolean valido = orden.getTipoOrden().getIdTipoOrden() == tipo_orden_idtipo_orden;
        return valido;
    }


}
