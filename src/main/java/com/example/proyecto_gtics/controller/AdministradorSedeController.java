package com.example.proyecto_gtics.controller;


import com.example.proyecto_gtics.dto.*;
import com.example.proyecto_gtics.entity.*;
import com.example.proyecto_gtics.repository.*;
import com.example.proyecto_gtics.service.DniService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.dao.DataIntegrityViolationException;
import org.thymeleaf.model.IModel;

import javax.naming.Binding;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
                                       TipoUsuarioRepository tipoUsuarioRepository, UsuariosRepository usuariosRepository) {

        this.categoriasRepository = categoriasRepository;
        this.detallesOrdenRepository = detallesOrdenRepository;
        this.estadoOrdenRepository = estadoOrdenRepository;
        this.estadoUsuarioRepository = estadoUsuarioRepository;
        this.ordenesRepository = ordenesRepository;
        this.preferenciasUsuarioRepository = preferenciasUsuarioRepository;
        this.productosRepository = productosRepository;
        this.productosSedeRepository = productosSedeRepository;
        this.sedesRepository = sedesRepository;
        this.tipoCobroRepository = tipoCobroRepository;
        this.tipoOrdenRepository = tipoOrdenRepository;
        this.tipoUsuarioRepository = tipoUsuarioRepository;
        this.usuariosRepository = usuariosRepository;
    }

    @Autowired
    CodigoColegioRespository codigoColegioRespository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private DniService dniService;

    //Formatear strings a dates
    DateTimeFormatter formatStringToDate = new DateTimeFormatterBuilder().append(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toFormatter();
    DateTimeFormatter formatDateToSring = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    @GetMapping(value = {"/administradorsede"})
    public String paginaPrincipal(Model model, HttpSession session) {
        Usuarios adminSede = (Usuarios) session.getAttribute("usuario"); //Admin de sede logueado
        Usuarios superAdmin = (Usuarios) session.getAttribute("originalUser");//Superadmin logueado
        //Verificamos que el superadmin no pueda acceder a administrador de sede sin una sesion
        if(Objects.equals(adminSede.getTipoUsuario().getIdTipoUsuario(), "SuperAdmin")){
            return "redirect:/superadmin";
        }
        //-------------------------------------------------------------------------------------
        if(superAdmin != null){
            model.addAttribute("superAdmin",superAdmin);
        }
        model.addAttribute("adminSede", adminSede);

        //Creacion de archivo JSON para añadir DATA en el grafico pastel
        List<CantProductoMenosPorSede> listCantProductoMenosPorSede = productosRepository.obtenerProductosPocoInventariado(adminSede.getSedes().getIdSedes());
        JSONObject jsonObject = new JSONObject();
        for (CantProductoMenosPorSede cantProductoMenosPorSede : listCantProductoMenosPorSede) {
            jsonObject.put(cantProductoMenosPorSede.getNombre(), cantProductoMenosPorSede.getCantidadTotal());
        }
        model.addAttribute("listaCantProductosSede", jsonObject.toString());

        //Creacion de archivo JSON para añadir DATA en el grafico de barras
        List<NroTransaccionesPorSede> listNroTransaccionesPorSede = ordenesRepository.encuentraNroTransaccinesPorSede();
        JSONObject jsonObject1 = new JSONObject();
        for (NroTransaccionesPorSede nroTransaccionesPorSede : listNroTransaccionesPorSede) {
            jsonObject1.put(nroTransaccionesPorSede.getNombre(), nroTransaccionesPorSede.getNroTransacciones());
        }
        model.addAttribute("listaNroTransaccionPorSede", jsonObject1.toString());

        //Creacion de archivo JSON para añadir DATA en el grafico de linea
        List<CantidadPorProductoSedeFecha> listCantProductoSedeFechaPorTresMeses = ordenesRepository.cantidadPorSedePorProductoPorFechaMes(adminSede.getSedes().getIdSedes());
        List<CantidadPorProductoSedeFecha> listCantProductoSedeFechaPor15Dias = ordenesRepository.cantidadPorSedePorProductoPorFechaDia(adminSede.getSedes().getIdSedes(),15);
        List<CantidadPorProductoSedeFecha> listCantProductoSedeFechaPor7Dias = ordenesRepository.cantidadPorSedePorProductoPorFechaDia(adminSede.getSedes().getIdSedes(),7);

        //Grafico 3 meses
        JSONArray jsonArray = new JSONArray();
        for (CantidadPorProductoSedeFecha cantidadPorProductoSedeFecha : listCantProductoSedeFechaPorTresMeses){
            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.put("nombre",cantidadPorProductoSedeFecha.getMedicamento());
            jsonObject2.put("cantidad",cantidadPorProductoSedeFecha.getCantidad());
            jsonObject2.put("sede",cantidadPorProductoSedeFecha.getNombreSede());
            jsonArray.put(jsonObject2);
        }

        //Grafico 15 dias
        JSONArray jsonArray2 = new JSONArray();
        for (CantidadPorProductoSedeFecha cantidadPorProductoSedeFecha : listCantProductoSedeFechaPor15Dias){
            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.put("nombre",cantidadPorProductoSedeFecha.getMedicamento());
            jsonObject2.put("cantidad",cantidadPorProductoSedeFecha.getCantidad());
            jsonObject2.put("sede",cantidadPorProductoSedeFecha.getNombreSede());
            jsonArray2.put(jsonObject2);
        }

        //Grafico 7 dias
        JSONArray jsonArray3 = new JSONArray();
        for (CantidadPorProductoSedeFecha cantidadPorProductoSedeFecha : listCantProductoSedeFechaPor7Dias){
            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.put("nombre",cantidadPorProductoSedeFecha.getMedicamento());
            jsonObject2.put("cantidad",cantidadPorProductoSedeFecha.getCantidad());
            jsonObject2.put("sede",cantidadPorProductoSedeFecha.getNombreSede());
            jsonArray3.put(jsonObject2);
        }
        System.out.println(jsonArray);

        model.addAttribute("listaCantProductoSedeFechaPor3Meses",jsonArray.toString());
        model.addAttribute("listaCantProductoSedeFechaPor15Dias",jsonArray2.toString());
        model.addAttribute("listaCantProductoSedeFechaPor7Dias",jsonArray3.toString());

        //Dos últimas ordenes de reposición

        TipoOrden ordenRepo = tipoOrdenRepository.findById(2).get();
        List<EstadoOrden> listEstadosNoListados = new ArrayList<>();
        for(int i = 1; i<= 3; i++){
            EstadoOrden e = estadoOrdenRepository.findById(i).get();
            listEstadosNoListados.add(e);
        }

        List<Ordenes> listaOrdenesRepo = ordenesRepository.findTop2ByTipoOrdenAndSedesAndEstadoOrdenNotInOrderByIdordenesDesc(ordenRepo, adminSede.getSedes(), listEstadosNoListados);
        int cantOrdenes = listaOrdenesRepo.size();
        if (cantOrdenes == 0){
            listaOrdenesRepo.add(ordenesRepository.findById(1).get());
            listaOrdenesRepo.add(ordenesRepository.findById(1).get());
        }
        if (cantOrdenes == 1){
            listaOrdenesRepo.add(ordenesRepository.findById(1).get());
        }

        model.addAttribute("listaOrdenesRepo",listaOrdenesRepo);

        return "AdministradorSede/index";
    }


    @GetMapping(value = {"/administradorsede/ordenes-reposicion"})
    public String ordenesReposicion(Model model, HttpSession session) {
        Usuarios adminSede = (Usuarios) session.getAttribute("usuario"); //Admin de sede logueado
        Usuarios superAdmin = (Usuarios) session.getAttribute("originalUser");//Superadmin logueado
        //Verificamos que el superadmin no pueda acceder a administrador de sede sin una sesion
        if(Objects.equals(adminSede.getTipoUsuario().getIdTipoUsuario(), "SuperAdmin")){
            return "redirect:/superadmin";
        }
        //-------------------------------------------------------------------------------------
        if(superAdmin != null){
            model.addAttribute("superAdmin",superAdmin);
        }
        model.addAttribute("adminSede", adminSede);

        TipoOrden tipoOrdenRepo = tipoOrdenRepository.findById(2).get(); //Tipo de orden: Orden de reposición
        EstadoOrden estadoEliminado = estadoOrdenRepository.findById(2).get(); //Estado de orden: Orden eliminada
        List<Ordenes> listOrdenesReposicion = ordenesRepository.findByTipoOrdenAndSedesAndEstadoOrdenNot(tipoOrdenRepo, adminSede.getSedes(), estadoEliminado);
        model.addAttribute("listaOrdenesReposicion", listOrdenesReposicion);

        //Pasando medicinas de la sede:
        List<ProductosSedes> listMedicinas = productosSedeRepository.findBySedes(adminSede.getSedes());
        model.addAttribute("listaMedicinas", listMedicinas);

        return "AdministradorSede/ordenesReposicion";

    }


    @GetMapping(value = {"/administradorsede/verOrden"})
    public String verOrden(Model model, @RequestParam("idOrdenRepo") Integer idOrdenRepo, HttpSession session) {

        Usuarios adminSede = (Usuarios) session.getAttribute("usuario"); //Admin de sede logueado
        Usuarios superAdmin = (Usuarios) session.getAttribute("originalUser");//Superadmin logueado
        //Verificamos que el superadmin no pueda acceder a administrador de sede sin una sesion
        if(Objects.equals(adminSede.getTipoUsuario().getIdTipoUsuario(), "SuperAdmin")){
            return "redirect:/superadmin";
        }
        //-------------------------------------------------------------------------------------
        if(superAdmin != null){
            model.addAttribute("superAdmin",superAdmin);
        }
        model.addAttribute("adminSede", adminSede);

        Optional<Ordenes> optOrden = ordenesRepository.findById(idOrdenRepo);

        if (optOrden.isPresent()) {
            Ordenes ordenReposicion = optOrden.get();
            //Validamos que la orden sea de reposicion y que se pertenezaca a la sede 2
            if (!validarTipoOrden(2, ordenReposicion) || !Objects.equals(ordenReposicion.getSedes().getIdSedes(), adminSede.getSedes().getIdSedes())) {
                return "redirect:/administradorsede/ordenes-reposicion";
            }
            List<DetallesOrden> listaDetallesOrden = detallesOrdenRepository.findByOrdenes(ordenReposicion);
            model.addAttribute("ordenReposicion", ordenReposicion);
            model.addAttribute("listaDetallesOrden", listaDetallesOrden);
            model.addAttribute("esNuevaOrden", 1);
            return "AdministradorSede/verOrdenReposicion";
        }

        return "redirect:/administradorsede/ordenes-reposicion";
    }

    @GetMapping(value = {"/administradorsede/nuevaOrden"})
    public String nuevaOrden(Model model, @RequestParam(name = "idOrdenRepo", required = false) Integer id,
                             @RequestParam(name = "primeraVez", required = false) Integer primeraVez,
                             HttpSession session) {

        Usuarios adminSede = (Usuarios) session.getAttribute("usuario"); //Admin de sede logueado
        Usuarios superAdmin = (Usuarios) session.getAttribute("originalUser");//Superadmin logueado
        //Verificamos que el superadmin no pueda acceder a administrador de sede sin una sesion
        if(Objects.equals(adminSede.getTipoUsuario().getIdTipoUsuario(), "SuperAdmin")){
            return "redirect:/superadmin";
        }
        //-------------------------------------------------------------------------------------
        if(superAdmin != null){
            model.addAttribute("superAdmin",superAdmin);
        }
        model.addAttribute("adminSede", adminSede);

        if (primeraVez != null) {
            if (primeraVez == 1) {
                //Creamos una orden tipo carrito
                crearOrdenCarrito(adminSede);
                Ordenes ordenRecuperada = ordenesRepository.findFirstByOrderByIdordenesDesc();
                id = ordenRecuperada.getIdordenes();

                //Creacion del codigo de orden de reposicion:
                ordenRecuperada.setCodigo(crearCodigo(id));
                ordenesRepository.save(ordenRecuperada);
            }
        }

        if (id == null) {
            return "redirect:/administradorsede/ordenes-reposicion";
        }

        Optional<Ordenes> optOrden = ordenesRepository.findById(id);

        if (optOrden.isPresent()) {

            if (!validarTipoOrden(6, optOrden.get())) {
                if (!validarTipoOrden(2, optOrden.get()) || optOrden.get().getSedes().getIdSedes() != 2) { //¿Porque distinto de 2?
                    return "redirect:/administradorsede/ordenes-reposicion";
                }
            }

            List<DetallesOrden> listaDetallesOrden = detallesOrdenRepository.findByOrdenes(optOrden.get());
            List<Productos> listaProductos = productosRepository.findAll();

            model.addAttribute("ordenReposicion", optOrden.get());
            model.addAttribute("listaDetallesOrden", listaDetallesOrden);
            model.addAttribute("listaProductos", listaProductos);
            model.addAttribute("esNuevaOrden", 1);
            //model.addAttribute("detalle", new DetallesOrden());


            return "AdministradorSede/editarOrden";

        } else {
            return "redirect:/administradorsede/ordenes-reposicion";
        }


    }

    @GetMapping(value = {"/administradorsede/editarOrden"})
    public String editarOrden(Model model, @RequestParam("idOrdenRepo") Integer id,
                              HttpSession session) {

        Usuarios adminSede = (Usuarios) session.getAttribute("usuario"); //Admin de sede logueado
        Usuarios superAdmin = (Usuarios) session.getAttribute("originalUser");//Superadmin logueado
        //Verificamos que el superadmin no pueda acceder a administrador de sede sin una sesion
        if(Objects.equals(adminSede.getTipoUsuario().getIdTipoUsuario(), "SuperAdmin")){
            return "redirect:/superadmin";
        }
        //-------------------------------------------------------------------------------------
        if(superAdmin != null){
            model.addAttribute("superAdmin",superAdmin);
        }
        model.addAttribute("adminSede", adminSede);

        Optional<Ordenes> optOrden = ordenesRepository.findById(id);
        if (optOrden.isPresent()) {

            if (!validarTipoOrden(2, optOrden.get()) || !Objects.equals(optOrden.get().getSedes().getIdSedes(), adminSede.getSedes().getIdSedes())) {
                return "redirect:/administradorsede/ordenes-reposicion";
            }

            List<DetallesOrden> listaDetallesOrden = detallesOrdenRepository.findByOrdenes(optOrden.get());
            List<Productos> listaProductos = productosRepository.findAll();

            model.addAttribute("ordenReposicion", optOrden.get());
            model.addAttribute("listaDetallesOrden", listaDetallesOrden);
            model.addAttribute("listaProductos", listaProductos);
            model.addAttribute("esNuevaOrden", 0);
            return "AdministradorSede/editarOrden";

        } else {
            return "redirect:/administradorsede/ordenes-reposicion";
        }

    }

    @PostMapping(value = {"/administradorsede/guardarDetalleOrden"})
    public String guardarDetalleOrden(@Valid DetallesOrden detalle, BindingResult bindingResult, RedirectAttributes attr, @RequestParam("ordenes") Integer idOrden, @RequestParam(name = "nuevaOrden", required = false) Integer nuevaOrden,
                                      @RequestParam("esNuevaOrden") Integer esNuevaOrden,
                                      HttpSession session) {

        Usuarios adminSede = (Usuarios) session.getAttribute("usuario"); //Admin de sede logueado
        //Verificamos que el superadmin no pueda acceder a administrador de sede sin una sesion
        if(Objects.equals(adminSede.getTipoUsuario().getIdTipoUsuario(), "SuperAdmin")){
            return "redirect:/superadmin";
        }
        //-------------------------------------------------------------------------------------

        if (bindingResult.hasErrors()) {
            attr.addFlashAttribute("err", "La cantidad debe ser un número entero positivo.");
            return (esNuevaOrden == 0 ? "redirect:/administradorsede/editarOrden?idOrdenRepo=" : "redirect:/administradorsede/nuevaOrden?idOrdenRepo=") + idOrden;
        }


        if (detalle.getIdDetallesOrden() == null) {//Agregar un nuevo producto y cantidad
            DetallesOrden detallesOrdenToSave = new DetallesOrden();
            detallesOrdenToSave.setOrdenes(detalle.getOrdenes());
            detallesOrdenToSave.setProductos(detalle.getProductos());
            detallesOrdenToSave.setCantidad(detalle.getCantidad());
            detallesOrdenToSave.setMontoParcial(detallesOrdenToSave.getProductos().getPrecio() * detalle.getCantidad());

            if (validarDuplicadoDeProductoEnUnaOrden(detallesOrdenToSave, detallesOrdenToSave.getOrdenes())) {
                attr.addFlashAttribute("err", "No puede seleecionar un producto que ya añadió.");
                return (esNuevaOrden == 0 ? "redirect:/administradorsede/editarOrden?idOrdenRepo=" : "redirect:/administradorsede/nuevaOrden?idOrdenRepo=") + detallesOrdenToSave.getOrdenes().getIdordenes();
            } else {

                Integer cantidadDetallesOrden = detallesOrdenRepository.calcularCantidadDeDetallesPorOrden(detallesOrdenToSave.getOrdenes().getIdordenes());
                if (esNuevaOrden == 0 && cantidadDetallesOrden >= 1 && cantidadDetallesOrden <= 10) {
                    detallesOrdenRepository.save(detallesOrdenToSave);

                    Ordenes ordenReposicionRecuperada = ordenesRepository.findById(detallesOrdenToSave.getOrdenes().getIdordenes()).get(); // Recuperamos la orden
                    ordenReposicionRecuperada.setMonto(detallesOrdenRepository.calcularMontoTotal(ordenReposicionRecuperada.getIdordenes())); //Calculamos el monto total de la orden
                    ordenesRepository.save(ordenReposicionRecuperada); // Por último guardamos la orden con el monto total actualizado

                    attr.addFlashAttribute("msg", "Orden actualizada exitosamente");
                    return (esNuevaOrden == 0 ? "redirect:/administradorsede/editarOrden?idOrdenRepo=" : "redirect:/administradorsede/nuevaOrden?idOrdenRepo=") + detallesOrdenToSave.getOrdenes().getIdordenes();
                } else if (esNuevaOrden == 1 && cantidadDetallesOrden <= 10) {
                    detallesOrdenRepository.save(detallesOrdenToSave);

                    Ordenes ordenReposicionRecuperada = ordenesRepository.findById(detallesOrdenToSave.getOrdenes().getIdordenes()).get(); // Recuperamos la orden
                    ordenReposicionRecuperada.setMonto(detallesOrdenRepository.calcularMontoTotal(ordenReposicionRecuperada.getIdordenes())); //Calculamos el monto total de la orden
                    ordenesRepository.save(ordenReposicionRecuperada); // Por último guardamos la orden con el monto total actualizado

                    attr.addFlashAttribute("msg", "Orden actualizada exitosamente");
                    return (esNuevaOrden == 0 ? "redirect:/administradorsede/editarOrden?idOrdenRepo=" : "redirect:/administradorsede/nuevaOrden?idOrdenRepo=") + detallesOrdenToSave.getOrdenes().getIdordenes();

                } else {
                    attr.addFlashAttribute("err", "Solo puede haber de 1 a 10 medicamentos por orden de reposición.");
                    return (esNuevaOrden == 0 ? "redirect:/administradorsede/editarOrden?idOrdenRepo=" : "redirect:/administradorsede/nuevaOrden?idOrdenRepo=") + detallesOrdenToSave.getOrdenes().getIdordenes();
                }

            }


        } else {//Modificar la cantidad de un producto ya existente

            DetallesOrden detallesOrdenToSave = detallesOrdenRepository.findById(detalle.getIdDetallesOrden()).get();
            detallesOrdenToSave.setCantidad(detalle.getCantidad());
            detallesOrdenToSave.setMontoParcial(detallesOrdenToSave.getProductos().getPrecio() * detalle.getCantidad()); //Calculamos el nuevo monto parcial

            detallesOrdenRepository.save(detallesOrdenToSave);

            Ordenes ordenReposicionRecuperada = ordenesRepository.findById(detallesOrdenToSave.getOrdenes().getIdordenes()).get(); // Recuperamos la orden
            ordenReposicionRecuperada.setMonto(detallesOrdenRepository.calcularMontoTotal(ordenReposicionRecuperada.getIdordenes())); //Calculamos el monto total de la orden
            ordenesRepository.save(ordenReposicionRecuperada); // Por último guardamos la orden con el monto total actualizado

            attr.addFlashAttribute("msg", "Orden actualizada exitosamente");
            return (esNuevaOrden == 0 ? "redirect:/administradorsede/editarOrden?idOrdenRepo=" : "redirect:/administradorsede/nuevaOrden?idOrdenRepo=") + detallesOrdenToSave.getOrdenes().getIdordenes();
        }

    }


    @GetMapping(value = {"/administradorsede/borrarDetalleOrden"})
    public String borrarDetalleOrden(@RequestParam("idDetalleOrden") Integer id, RedirectAttributes attr,
                                     HttpSession session, Model model) {

        Usuarios adminSede = (Usuarios) session.getAttribute("usuario"); //Admin de sede logueado
        Usuarios superAdmin = (Usuarios) session.getAttribute("originalUser");//Superadmin logueado
        //Verificamos que el superadmin no pueda acceder a administrador de sede sin una sesion
        if(Objects.equals(adminSede.getTipoUsuario().getIdTipoUsuario(), "SuperAdmin")){
            return "redirect:/superadmin";
        }
        //-------------------------------------------------------------------------------------
        if(superAdmin != null){
            model.addAttribute("superAdmin",superAdmin);
        }
        model.addAttribute("adminSede", adminSede);

        Optional<DetallesOrden> optionalDetallesOrden = detallesOrdenRepository.findById(id);
        Integer cantidadDetallesOrden = detallesOrdenRepository.calcularCantidadDeDetallesPorOrden(optionalDetallesOrden.get().getOrdenes().getIdordenes());
        if (cantidadDetallesOrden <= 1) {
            attr.addFlashAttribute("err", "Solo puede haber de 1 a 10 medicamentos por orden de reposición.");
            return "redirect:/administradorsede/editarOrden?idOrdenRepo=" + optionalDetallesOrden.get().getOrdenes().getIdordenes();
        } else {
            optionalDetallesOrden.ifPresent(detallesOrdenRepository::delete);
            return "redirect:/administradorsede/editarOrden?idOrdenRepo=" + optionalDetallesOrden.get().getOrdenes().getIdordenes();
        }


    }

    @PostMapping(value = {"/administradorsede/guardarorden-reposicion"})
    public String guardarOrdenReposicion(@RequestParam("idOrdenCarrito") Integer idOrdenCarrito, RedirectAttributes attr,
                                         HttpSession session) {

        Usuarios adminSede = (Usuarios) session.getAttribute("usuario"); //Admin de sede logueado
        //Verificamos que el superadmin no pueda acceder a administrador de sede sin una sesion
        if(Objects.equals(adminSede.getTipoUsuario().getIdTipoUsuario(), "SuperAdmin")){
            return "redirect:/superadmin";
        }
        //-------------------------------------------------------------------------------------


        Ordenes ordenCarrito = ordenesRepository.findById(idOrdenCarrito).get();
        ordenCarrito.setTipoOrden(tipoOrdenRepository.findById(2).get());
        LocalDate fechaActual = LocalDateTime.now(ZoneId.of("America/New_York")).toLocalDate(); //sacamos la fecha actual
        ordenCarrito.setFechaRegistro(fechaActual.format(formatDateToSring));
        ordenCarrito.setEstadoOrden(estadoOrdenRepository.findById(1).get()); // Seteamos el estado de orden como Pendiente
        ordenesRepository.save(ordenCarrito);
        attr.addFlashAttribute("msg", "Se ha generado una nueva orden de reposición.");
        return "redirect:/administradorsede/ordenes-reposicion";
    }


    @GetMapping(value = {"/administradorsede/borrarorden-reposicion"})
    public String borrarOrdenReposicion(@RequestParam("idOrden") Integer id,
                                        HttpSession session, Model model) {
        Usuarios adminSede = (Usuarios) session.getAttribute("usuario"); //Admin de sede logueado
        Usuarios superAdmin = (Usuarios) session.getAttribute("originalUser");//Superadmin logueado
        //Verificamos que el superadmin no pueda acceder a administrador de sede sin una sesion
        if(Objects.equals(adminSede.getTipoUsuario().getIdTipoUsuario(), "SuperAdmin")){
            return "redirect:/superadmin";
        }
        //-------------------------------------------------------------------------------------
        if(superAdmin != null){
            model.addAttribute("superAdmin",superAdmin);
        }
        model.addAttribute("adminSede", adminSede);

        Optional<Ordenes> optOrden = ordenesRepository.findById(id);
        if (optOrden.isPresent()) {
            Ordenes ordenToSave = optOrden.get();
            ordenToSave.setEstadoOrden(estadoOrdenRepository.findById(2).get()); //Se cambia el estado de la orden a 4 (Eliminado)
            ordenesRepository.save(ordenToSave);
        }

        return "redirect:/administradorsede/ordenes-reposicion";
    }

    @GetMapping(value = {"/administradorsede/doctores"})
    public String doctores(Model model, HttpSession session) {

        Usuarios adminSede = (Usuarios) session.getAttribute("usuario"); //Admin de sede logueado
        Usuarios superAdmin = (Usuarios) session.getAttribute("originalUser");//Superadmin logueado
        //Verificamos que el superadmin no pueda acceder a administrador de sede sin una sesion
        if(Objects.equals(adminSede.getTipoUsuario().getIdTipoUsuario(), "SuperAdmin")){
            return "redirect:/superadmin";
        }
        //-------------------------------------------------------------------------------------
        if(superAdmin != null){
            model.addAttribute("superAdmin",superAdmin);
        }
        model.addAttribute("adminSede", adminSede);

        EstadoUsuario estadoUsuario = estadoUsuarioRepository.findById("Activo").get();
        TipoUsuario doctor = tipoUsuarioRepository.findById("Doctor").get(); //Tipo de usuario: Doctor
        List<Usuarios> listaDoctores = usuariosRepository.findByTipoUsuarioAndSedesAndEstadoUsuario(doctor, adminSede.getSedes(), estadoUsuario);

        model.addAttribute("listaDoctores", listaDoctores);
        model.addAttribute("adminSede", adminSede);

        return "AdministradorSede/doctores";
    }


    @GetMapping(value = {"/administradorsede/farmacistas"})
    public String farmacistas(Model model, HttpSession session) {

        Usuarios adminSede = (Usuarios) session.getAttribute("usuario"); //Admin de sede logueado
        Usuarios superAdmin = (Usuarios) session.getAttribute("originalUser");//Superadmin logueado
        //Verificamos que el superadmin no pueda acceder a administrador de sede sin una sesion
        if(Objects.equals(adminSede.getTipoUsuario().getIdTipoUsuario(), "SuperAdmin")){
            return "redirect:/superadmin";
        }
        //-------------------------------------------------------------------------------------
        if(superAdmin != null){
            model.addAttribute("superAdmin",superAdmin);
        }
        model.addAttribute("adminSede", adminSede);


        EstadoUsuario estado = estadoUsuarioRepository.findById("activo").get();
        TipoUsuario farmacista = tipoUsuarioRepository.findById("Farmacista").get(); //Tipo de usuario: Farmacista
        List<Usuarios> listaFarmacistas = usuariosRepository.findByTipoUsuarioAndSedesAndEstadoUsuario(farmacista, adminSede.getSedes(), estado);

        model.addAttribute("listaFarmacistas", listaFarmacistas);
        model.addAttribute("adminSede", adminSede);


        return "AdministradorSede/farmacistas";
    }

    @PostMapping(value = "/administradorsede/guardarfarmacista")
    public String guardarFarmacista(@Valid Usuarios usuarios, BindingResult bindingResult,
                                    @RequestParam("idSedes") int id, RedirectAttributes attr,
                                    HttpSession session) {

        Usuarios adminSede = (Usuarios) session.getAttribute("usuario"); //Admin de sede logueado
        //Verificamos que el superadmin no pueda acceder a administrador de sede sin una sesion
        if(Objects.equals(adminSede.getTipoUsuario().getIdTipoUsuario(), "SuperAdmin")){
            return "redirect:/superadmin";
        }
        //-------------------------------------------------------------------------------------

        if (bindingResult.hasErrors()) {
            String errors = bindingResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            attr.addFlashAttribute("err", errors);
            return "redirect:/administradorsede/farmacistas";
        } else {


            Optional<Sedes> sedesOpt = sedesRepository.findById(id);
            if (sedesOpt.isEmpty()) {
                attr.addFlashAttribute("err", "error=sede_invalida");
                return "redirect:/administradorsede/farmacistas";
            }
            if (usuarios.getIdUsuario() == null) { // Caso crear farmacista

                if (!validarCodigoColegio(usuarios.getCodigoColegio())) {
                    attr.addFlashAttribute("err", "El código de colegio no es válido.");
                    return "redirect:/administradorsede/farmacistas";
                }

                if (usuarioYaRegistrado(usuarios.getDni(), usuarios.getIdUsuario(),true)) {
                    attr.addFlashAttribute("err", "El DNI ya está registrado.");
                    return "redirect:/administradorsede/farmacistas";
                }

                if (correoYaRegistrado(usuarios.getCorreo(), usuarios.getIdUsuario(),true)) {
                    attr.addFlashAttribute("err", "El correo ya está registrado.");
                    return "redirect:/administradorsede/farmacistas";
                }

                ResultDni resultDni = dniService.obtenerDatosPorDni(usuarios.getDni().toString());
                if (resultDni == null || resultDni.getStatus() != 200 || resultDni.getData() == null) {
                    attr.addFlashAttribute("err","DNI inválido");
                    return "redirect:/administradorsede/farmacistas";
                }

                usuarios.setNombre(resultDni.getData().getNombres() + " " + resultDni.getData().getApellido_paterno() + " " + resultDni.getData().getApellido_materno());

                usuarios.setSedes(sedesOpt.get());
                usuarios.setContrasena("Temporal_password");
                usuarios.setEstadoUsuario(estadoUsuarioRepository.findById("En revisión").get());
                usuarios.setTipoUsuario(tipoUsuarioRepository.findById("Farmacista").get());
                usuariosRepository.save(usuarios);
                attr.addFlashAttribute("msg", "Farmacista creado exitosamente");
            } else {   // Caso Actualizar farmacista
                if (!validarCodigoColegio(usuarios.getCodigoColegio())) {
                    attr.addFlashAttribute("err", "El código de colegio no es válido.");
                    return "redirect:/administradorsede/farmacistas";
                }

                usuarios.setSedes(sedesOpt.get());
                usuarios.setEstadoUsuario(estadoUsuarioRepository.findById("activo").get());
                usuarios.setTipoUsuario(tipoUsuarioRepository.findById("Farmacista").get());
                usuariosRepository.save(usuarios);
                attr.addFlashAttribute("msg", "Farmacista actualizado exitosamente");
            }


            return "redirect:/administradorsede/farmacistas";
        }

    }

    @GetMapping("/administradorsede/borrarfarmacista")
    public String borrarFarmacista(@RequestParam("idFarmacista") Integer id,
                                   HttpSession session, Model model) {

        Usuarios adminSede = (Usuarios) session.getAttribute("usuario"); //Admin de sede logueado
        Usuarios superAdmin = (Usuarios) session.getAttribute("originalUser");//Superadmin logueado
        //Verificamos que el superadmin no pueda acceder a administrador de sede sin una sesion
        if(Objects.equals(adminSede.getTipoUsuario().getIdTipoUsuario(), "SuperAdmin")){
            return "redirect:/superadmin";
        }
        //-------------------------------------------------------------------------------------
        if(superAdmin != null){
            model.addAttribute("superAdmin",superAdmin);
        }
        model.addAttribute("adminSede", adminSede);

        Optional<Usuarios> optSede = usuariosRepository.findById(id);
        if (optSede.isPresent()) {
            usuariosRepository.deleteById(id);
        }
        return "redirect:/administradorsede/farmacistas";

    }


    @GetMapping(value = {"/administradorsede/medicinas"})
    public String medicinas(Model model, HttpSession session) {

        Usuarios adminSede = (Usuarios) session.getAttribute("usuario"); //Admin de sede logueado
        Usuarios superAdmin = (Usuarios) session.getAttribute("originalUser");//Superadmin logueado
        //Verificamos que el superadmin no pueda acceder a administrador de sede sin una sesion
        if(Objects.equals(adminSede.getTipoUsuario().getIdTipoUsuario(), "SuperAdmin")){
            return "redirect:/superadmin";
        }
        //-------------------------------------------------------------------------------------
        if(superAdmin != null){
            model.addAttribute("superAdmin",superAdmin);
        }
        model.addAttribute("adminSede", adminSede);

        //List<ProductosSedes> listMedicinas = productosSedeRepository.findBySedes(adminSede.getSedes());
        List<ProductosSedes> listMedicinas = productosSedeRepository.findBySedesAndVisibilidad(adminSede.getSedes(), 1);
        model.addAttribute("listaMedicinas", listMedicinas);

        return "AdministradorSede/medicinas";
    }


    @GetMapping(value = {"/administradorsede/perfil"})
    public String perfil(HttpSession session, Model model) {
        Usuarios adminSede = (Usuarios) session.getAttribute("usuario"); //Admin de sede logueado
        Usuarios superAdmin = (Usuarios) session.getAttribute("originalUser");//Superadmin logueado
        //Verificamos que el superadmin no pueda acceder a administrador de sede sin una sesion
        if(Objects.equals(adminSede.getTipoUsuario().getIdTipoUsuario(), "SuperAdmin")){
            return "redirect:/superadmin";
        }
        //-------------------------------------------------------------------------------------
        if(superAdmin != null){
            model.addAttribute("superAdmin",superAdmin);
        }
        model.addAttribute("adminSede", adminSede);

        return "AdministradorSede/perfil";
    }

    @GetMapping(value = {"/administradorsede/editar-perfil"})
    public String editarPerfil(HttpSession session, Model model) {
        Usuarios adminSede = (Usuarios) session.getAttribute("usuario"); //Admin de sede logueado
        Usuarios superAdmin = (Usuarios) session.getAttribute("originalUser");//Superadmin logueado
        //Verificamos que el superadmin no pueda acceder a administrador de sede sin una sesion
        if(Objects.equals(adminSede.getTipoUsuario().getIdTipoUsuario(), "SuperAdmin")){
            return "redirect:/superadmin";
        }
        //-------------------------------------------------------------------------------------
        if(superAdmin != null){
            model.addAttribute("superAdmin",superAdmin);
        }
        model.addAttribute("adminSede", adminSede);

        return "AdministradorSede/editarPerfil";
    }

    @PostMapping(value = "/administradorsede/guardar_perfil")
    public String actualizarPerfil(@RequestParam(name = "direccion", required = false) String direccion,
                                   @RequestParam(name = "distrito", required = false) String distrito,
                                   @RequestParam(name = "correo",required = false) String correo,
                                   @RequestParam("image") MultipartFile file,
                                   RedirectAttributes attr, HttpSession session){

        Usuarios adminSede = (Usuarios) session.getAttribute("usuario"); //Admin de sede logueado

        if(direccion == null || distrito == null || correo == null){
            attr.addFlashAttribute("msg","Debe rellenar los campos.");
            return "redirect:/administradorsede/editar-perfil";
        }
        if(direccion.isEmpty() || distrito.isEmpty() || correo.isEmpty()){
            attr.addFlashAttribute("msg","Debe rellenar los campos.");
            return "redirect:/administradorsede/editar-perfil";
        }


        //Sobre la foto de un producto --------------------------------------------------------------------------------
        if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            if (fileName.contains("..")) {
                attr.addFlashAttribute("err","No se permiten '..' en el archivo");
                return "redirect:/administradorsede/editar-perfil";
            }
            try {
                adminSede.setFoto(file.getBytes());
                adminSede.setFotonombre(fileName);
                adminSede.setFotocontenttype(file.getContentType());

            } catch (IOException e) {
                e.printStackTrace();
                attr.addFlashAttribute("err","No se permiten '..' en el archivo");
                return "redirect:/administradorsede/editar-perfil";
            }
        }
        //-----------------------------------------------------------------------------------------------------------


        adminSede.setCorreo(correo);
        adminSede.setDireccion(direccion);
        adminSede.setDistritoResidencia(distrito);
        usuariosRepository.save(adminSede);
        attr.addFlashAttribute("msg","Perfil actualizado exitosamente.");
        session.setAttribute("usuario",usuariosRepository.findByIdUsuario(adminSede.getIdUsuario()));
        return "redirect:/administradorsede/perfil";

    }

    @GetMapping(value = {"/administradorsede/cambiar-contra"})
    public String cambiarContra(HttpSession session, Model model) {
        Usuarios adminSede = (Usuarios) session.getAttribute("usuario"); //Admin de sede logueado
        Usuarios superAdmin = (Usuarios) session.getAttribute("originalUser");//Superadmin logueado
        //Verificamos que el superadmin no pueda acceder a administrador de sede sin una sesion
        if(Objects.equals(adminSede.getTipoUsuario().getIdTipoUsuario(), "SuperAdmin")){
            return "redirect:/superadmin";
        }
        //-------------------------------------------------------------------------------------
        if(superAdmin != null){
            model.addAttribute("superAdmin",superAdmin);
        }
        model.addAttribute("adminSede", adminSede);

        return "AdministradorSede/cambiarContra";
    }

    @PostMapping(value = "/administradorsede/actualizar-contra")
    public String actualizarContra(@RequestParam(name = "pass1", required = false) String pass1,
                                   @RequestParam(name = "pass2", required = false) String pass2,
                                   RedirectAttributes attr, HttpSession session){

        Usuarios adminSede = (Usuarios) session.getAttribute("usuario"); //Admin de sede logueado


        if(pass1 == null || pass2 == null){
            attr.addFlashAttribute("err","Debe rellenar los campos.");
            return "redirect:/administradorsede/cambiar-contra";
        }
        if(pass1.isEmpty() || pass2.isEmpty()){
            attr.addFlashAttribute("err","Debe rellenar los campos.");
            return "redirect:/administradorsede/cambiar-contra";
        }


        if(pass1.equalsIgnoreCase(pass2)){
            adminSede.setContrasena(passwordEncoder.encode(pass1));
            usuariosRepository.save(adminSede);
            attr.addFlashAttribute("msg","Contraseña actualizada exitosamente.");
            return "redirect:/administradorsede/cambiar-contra";
        }
        else {
            attr.addFlashAttribute("wrn","Las contraseñas no coinciden.");
            return "redirect:/administradorsede/cambiar-contra";
        }

    }

    @GetMapping("/administradorSede/notificaciones")
    @ResponseBody
    public List<String> obtenerNotificaciones(HttpSession session){
        Usuarios adminSede = (Usuarios) session.getAttribute("usuario"); //Admin de sede logueado
        List<String> notificacionesPendientes = new ArrayList<>();
        List<CantProductoMenosPorSede> listCantProductoMenosPorSede = productosRepository.obtenerProductosPocoInventariado(adminSede.getSedes().getIdSedes());
        //notificacionesPendientes.clear();
        if(listCantProductoMenosPorSede == null){
            notificacionesPendientes.add("No hay notificaciones");
        }else {
            for(CantProductoMenosPorSede item : listCantProductoMenosPorSede){
                notificacionesPendientes.add("Quedan "+item.getCantidadTotal()+ " unidades de "+item.getNombre());
            }
        }
        return notificacionesPendientes;
    }


    /* VALIDACION DE CORREO
     */
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]{1,255}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    public boolean validarDNI(String dni) {
        // Verifica si la cadena contiene solo dígitos y tiene un máximo de 8 caracteres
        return dni != null && dni.matches("\\d{8}");
    }

    public boolean validarCampo(String campo) {
        // Verifica si la cadena no es nula, no está vacía y su longitud es menor o igual a 255 caracteres
        return campo != null && !campo.trim().isEmpty() && campo.length() <= 255;
    }

    public boolean validarTipoOrden(Integer tipo_orden_idtipo_orden, Ordenes orden) {
        // Valida si una orden es de un tipo dado
        boolean valido = Objects.equals(orden.getTipoOrden().getIdTipoOrden(), tipo_orden_idtipo_orden);
        if(orden.getTipoOrden().getIdTipoOrden()==6){
            valido = true;
        }
        return valido;
    }

    public boolean validarDuplicadoDeProductoEnUnaOrden(DetallesOrden detallesOrdenPorComprobar, Ordenes orden) {

        boolean valido = false;
        List<DetallesOrden> listaDetalles = detallesOrdenRepository.findByOrdenes(orden);
        for (DetallesOrden detalle : listaDetalles) {
            if (detalle.getProductos().getIdProductos() == detallesOrdenPorComprobar.getProductos().getIdProductos()) {
                valido = true;
            }
        }
        return valido;
    }


    public void crearOrdenCarrito(Usuarios usuario) {
        Ordenes ordenCarrito = new Ordenes();
        ordenCarrito.setEstadoOrden(estadoOrdenRepository.findById(4).get()); //Estado se configura en aceptado (no importa mucho en una orden carrito)
        ordenCarrito.setTipoOrden(tipoOrdenRepository.findById(6).get()); // Tipo de orden 6 (carrito)
        ordenCarrito.setUsuarios(usuario);
        ordenCarrito.setTipoCobro(tipoCobroRepository.findById(1).get()); // Asignamos un tipo de cobro
        ordenCarrito.setSedes(sedesRepository.findById(usuario.getSedes().getIdSedes()).get());

        ordenCarrito.setCodigo(UUID.randomUUID().toString());
        LocalDate fechaActual = LocalDateTime.now(ZoneId.of("America/Lima")).toLocalDate(); //sacamos la fecha actual
        ordenCarrito.setFechaRegistro(fechaActual.format(formatDateToSring));
        LocalDate fechaEntrega = fechaActual.plusDays(20);
        ordenCarrito.setFechaEntrega(fechaEntrega.format(formatDateToSring));

        ordenesRepository.save(ordenCarrito); // creamos la orden de reposición
    }

    public String crearCodigo(Integer idOrden){
        String codigo = "ORD-REPO-"+idOrden.toString();
        return codigo;
    }

    public boolean validarCodigoColegio(String codigo) {

        boolean valido = false;
        Optional<CodigoColegio> opt = codigoColegioRespository.findByCodigo(codigo);
        if (opt.isPresent()) {
            valido = true;

        }
        return valido;
    }

    public Boolean usuarioYaRegistrado(Integer dni, Integer idUsuario, boolean registro){ // registro: true => registro, false => actualizar
        boolean yaRegistrado = false;

        Optional<Usuarios> opt = usuariosRepository.findByDni(dni);
        if(registro){
            if (opt.isPresent()){
                yaRegistrado = true;
            }
        }
        else {
            if (opt.isPresent()){
                if(!Objects.equals(opt.get().getIdUsuario(), idUsuario)){
                    yaRegistrado = true;
                }
            }
        }

        return yaRegistrado;
    }

    public Boolean correoYaRegistrado(String correo, Integer idUsuario, boolean registro){// registro: true => registro, false => actualizar
        boolean yaRegistrado = false;
        Optional<Usuarios> opt = usuariosRepository.findByCorreo(correo);

        if(registro){
            if (opt.isPresent()){
                yaRegistrado = true;
            }
        }
        else {
            if (opt.isPresent()){
                if(!Objects.equals(opt.get().getIdUsuario(), idUsuario)){
                    yaRegistrado = true;
                }
            }
        }

        return yaRegistrado;
    }


}
