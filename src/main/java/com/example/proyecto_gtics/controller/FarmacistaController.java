package com.example.proyecto_gtics.controller;

import com.example.proyecto_gtics.entity.*;
import com.example.proyecto_gtics.repository.*;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;

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

    //Formatear strings a dates
    DateTimeFormatter formatStringToDate = new DateTimeFormatterBuilder().append(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toFormatter();
    DateTimeFormatter formatDateToSring = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    @GetMapping(value ={"/farmacista"})
    public String paginaPrincipal(Model model){
        Sedes sede = sedesRepository.findByIdSedes(2);
        TipoUsuario doctor = tipoUsuarioRepository.findById("Doctor").get();
        EstadoUsuario activo = estadoUsuarioRepository.findById("Activo").get();

        List<ProductosSedes> listaProductos = productosSedeRepository.findBySedes(sede);
        model.addAttribute("listaProductos",listaProductos);
        model.addAttribute("listaDoctores",usuariosRepository.findByTipoUsuarioAndSedesAndEstadoUsuario(doctor,sede,activo));
        return "Farmacista/index";
    }

    @PostMapping(value = "/farmacista/guardarOrden")
    public String guardarOrden(@RequestParam("listaIdsProductos") List<Integer> listaIdsProductos, @RequestParam("listaCantidades") List<String> listaCantidades,
                               /*@RequestParam("checkbox") List<String> listCheckbox,*/ @Valid Usuarios paciente, BindingResult bindingResult,
                               @RequestParam("fechaEntregaStr") String fechaEntregaStr, @RequestParam("idDoctor") Integer idDoctor,
                               RedirectAttributes attr){

        if (bindingResult.hasErrors()) {
            String error = bindingResult.getFieldError().getDefaultMessage();

            attr.addFlashAttribute("err",error);

            return "redirect:/farmacista";
        }

        for(String s : listaCantidades){
            try {
                Integer.parseInt(s);
            }
            catch (NumberFormatException n){
                attr.addFlashAttribute("err","Las cantidades deben ser números.");
                return "redirect:/farmacista";
            }
        }



        if (!usuarioYaRegistrado(paciente.getDni())){ //Caso crear un paciente / el paciente no está registrado aún en el sistema
            paciente.setSedes(sedesRepository.findById(2).get()); // asignamos la sede actual
            paciente.setTipoUsuario(tipoUsuarioRepository.findById("Paciente").get());
            LocalDate fechaActual = LocalDateTime.now(ZoneId.of("America/New_York")).toLocalDate(); //sacamos la fecha actual
            paciente.setFechaRegistro(Date.from(fechaActual.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            paciente.setContrasena("Temporal_password");
            paciente.setCorreo(UUID.randomUUID().toString());
            paciente.setEstadoUsuario(estadoUsuarioRepository.findById("Activo").get());
            usuariosRepository.save(paciente);
            paciente = usuariosRepository.findFirstByOrderByIdUsuarioDesc();
            attr.addFlashAttribute("wrn","Aviso: Se creó un nuevo registro del paciente con el DNI ingresado.");
        }
        else {

            paciente = usuariosRepository.findByDni(paciente.getDni()).get();

            if(paciente.getTipoUsuario().getIdTipoUsuario().equalsIgnoreCase("Paciente")){
                attr.addFlashAttribute("wrn","Aviso: El DNI del paciente ya se encuentra registrado en el sistema. Se usarán los datos del paciente registrado en el sistema");
            }
            else {
                attr.addFlashAttribute("err","Aviso: El DNI  ya se encuentra registrado en el sistema y no pertenece a un usuario con el rol Paciente.");
                return "redirect:/farmacista";
            }

        }

        crearOrden(paciente,6,8,idDoctor); // creamos la orden (tipo carrito)
        Ordenes ordenCreada = ordenesRepository.findFirstByOrderByIdordenesDesc(); //Recuperamos la orden que acabamos de crear

        int index = 0;
        for(Integer id: listaIdsProductos){
            Productos p = productosRepository.findById(id).get();
            String cantidadStr = listaCantidades.get(index);

            try{
                Integer cantidad = Integer.parseInt(cantidadStr);
                if(cantidad > 0){
                    DetallesOrden detallesOrden = new DetallesOrden();
                    detallesOrden.setOrdenes(ordenCreada);
                    detallesOrden.setProductos(p);
                    detallesOrden.setCantidad(cantidad);
                    detallesOrden.setMontoParcial(cantidad*p.getPrecio());
                    detallesOrdenRepository.save(detallesOrden); // Guardamos los productos y detalles de orden
                }

            }
            catch (NumberFormatException n){
                attr.addFlashAttribute("err","Las cantidades deben ser números.");
                return "redirect:/farmacista";
            }


            index++;
        }
        ordenCreada.setTipoOrden(tipoOrdenRepository.findById(1).get()); // Finalmente cambiamos el tipo de orden a orden presencial
        ordenesRepository.save(ordenCreada);

        attr.addFlashAttribute("msg","Orden Registrada exitosamente");
        attr.addFlashAttribute("wrn","Solo se añadieron las medicinas en las cuales se ingresó un número entero positivo en la cantidad solicitada.");
        return "redirect:/farmacista";

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

    @PostMapping(value ={"/farmacista/ordenes-linea/aprobar"})
    public String aprobarOrdenDeLinea(@RequestParam("idOrden") Integer idOrden, RedirectAttributes attr){

        Optional<Ordenes> opt = ordenesRepository.findById(idOrden);
        if(opt.isPresent()){
            Ordenes ordenes = opt.get();
            ordenes.setEstadoOrden(estadoOrdenRepository.findById(4).get());
            ordenesRepository.save(ordenes);
            return "redirect:/farmacista/ordenes-linea";
        }
        else {
            return "redirect:/farmacista/ordenes-linea";
        }


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


    public void crearOrden(Usuarios usuario, Integer tipoOrden, Integer estadoOrden, Integer idDoctor) {
        Ordenes orden = new Ordenes();
        orden.setEstadoOrden(estadoOrdenRepository.findById(estadoOrden).get()); //Estado se configura en aceptado (no importa mucho en una orden carrito)
        orden.setTipoOrden(tipoOrdenRepository.findById(tipoOrden).get()); // Tipo de orden
        orden.setUsuarios(usuario);
        orden.setTipoCobro(tipoCobroRepository.findById(1).get()); // Asignamos un tipo de cobro
        orden.setSedes(sedesRepository.findById(2).get());
        orden.setDoctor(usuariosRepository.findByIdUsuario(idDoctor));

        orden.setCodigo(UUID.randomUUID().toString());
        LocalDate fechaActual = LocalDateTime.now(ZoneId.of("America/New_York")).toLocalDate(); //sacamos la fecha actual
        orden.setFechaRegistro(fechaActual.format(formatDateToSring));
        LocalDate fechaEntrega = fechaActual.plusDays(20);
        orden.setFechaEntrega(fechaEntrega.format(formatDateToSring));

        ordenesRepository.save(orden); // creamos la

    }

    public Boolean usuarioYaRegistrado(Integer dni){
        boolean yaRegistrado = false;
        Optional<Usuarios> opt = usuariosRepository.findByDni(dni);

        if (opt.isPresent()){
            yaRegistrado = Objects.equals(opt.get().getDni(), dni);
        }

        return yaRegistrado;
    }


}
