package com.example.proyecto_gtics.controller;

import com.example.proyecto_gtics.entity.*;
import com.example.proyecto_gtics.repository.*;
import jakarta.validation.Valid;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;

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

    //Formatear strings a dates
    DateTimeFormatter formatStringToDate = new DateTimeFormatterBuilder().append(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toFormatter();
    DateTimeFormatter formatDateToSring = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @GetMapping(value ={"/clinicarenacer"})
    public String paginaPrincipal(Model model, String nombre, @RequestParam(name = "noprimeraVez", required = false) Integer noprimeraVez,
                                  @RequestParam(name = "idCarrito", required = false) Integer idCarrito){

        //Optional<Productos> optProductos= productosRepository.findById(idCarrito);
        /*Integer id;
        if(noprimeraVez != null){
            //Creamos una orden tipo carrito
            crearOrdenCarrito(usuariosRepository.findByIdUsuario(1019));
            Ordenes ordenRecuperada = ordenesRepository.findFirstByOrderByIdordenesDesc();
            id = ordenRecuperada.getIdordenes();
        }else{
            id = 0;
        }*/

        List<Productos> buscarProductos = productosRepository.findByNombreContainingIgnoreCase(nombre);
        List<Productos> listaProductos = productosRepository.findAll();
        List<Productos> listarProducto = productosRepository.findAll();
        List<Categorias> listaCategorias = categoriasRepository.findAll();
        List<Long> listaCantidades = new ArrayList<>();
        for (Categorias categorias: listaCategorias){
            listaCantidades.add(productosRepository.countByCategorias(categorias));
        }

        model.addAttribute("nombre", buscarProductos);
        model.addAttribute("listaCantCategorias",listaCantidades);
        model.addAttribute("listaProductos",listaProductos);
        model.addAttribute("listarProducto",listarProducto);
        model.addAttribute("listaCategorias", listaCategorias);
        /*if (id==0){
            model.addAttribute("idCarrito",idCarrito);
        }else{
            model.addAttribute("idCarrito",id);
        }*/
        return "FarmaciaWebVenta/index";
    }

    @GetMapping(value = "/historialPedidos")
    public String historialPedidos(Model model){
        Usuarios paciente = usuariosRepository.findByIdUsuario(1027);

        TipoOrden web = tipoOrdenRepository.findById(3).get();

        List<Ordenes> listaOrdenes = ordenesRepository.findByTipoOrdenAndUsuarios(web,paciente);

        model.addAttribute("listaOrdenes",listaOrdenes);

        return "FarmaciaWebVenta/historialPedidos";
    }

    @GetMapping(value = "paciente/verPedido")
    public String verPedido(Model model, @RequestParam("idOrden") Integer idOrden){

        Optional<Ordenes> opt = ordenesRepository.findById(idOrden);

        if(opt.isEmpty()){
            return "redirect:/clinicarenacer";
        }

        List<DetallesOrden> listaDetalles = detallesOrdenRepository.findByOrdenes(opt.get());

        model.addAttribute("ordenActual",opt.get());
        model.addAttribute("listaDetalles",listaDetalles);

        return "FarmaciaWebVenta/verPedido";
    }




    @GetMapping(value ={"/header"})
    public String cabecera(Model model, String nombre){
        List<Productos> buscarProductos = productosRepository.findByNombreContainingIgnoreCase(nombre);
        model.addAttribute("nombre", buscarProductos);
        List<Categorias> listaCategorias = categoriasRepository.findAll();
        model.addAttribute("listaCategorias", listaCategorias);
        model.addAttribute("idCarrito",1000);
        return "FarmaciaWebVenta/fragments_clinicaweb/cabecera";
    }

    @GetMapping(value ={"/footer"})
    public String footer(Model model, String nombre){
        List<Productos> buscarProductos = productosRepository.findByNombreContainingIgnoreCase(nombre);
        model.addAttribute("nombre", buscarProductos);
        List<Categorias> listaCategorias = categoriasRepository.findAll();
        model.addAttribute("listaCategorias", listaCategorias);
        return "FarmaciaWebVenta/fragments_clinicaweb/footer";
    }

    @GetMapping(value ={"/clinicarenacer/paciente"})
    public String paginaPrincipalConLogin(){

        return "FarmaciaWebVenta/user";
    }

    @GetMapping(value ={"/clinicarenacer/categoria"})
    public String ordenarPorCategoria(Model model,  @RequestParam(name = "idCategoria") int idCategoria){
        Categorias categorias = categoriasRepository.findById(idCategoria).get();

        List<Productos> listaProductosPorCategoria = productosRepository.findByCategorias(categorias);
        List<Productos> listarProducto = productosRepository.findAll();
        List<Categorias> listaCategorias = categoriasRepository.findAll();
        List<Long> listaCantidades = new ArrayList<>();
        for (Categorias categoria: listaCategorias){
            listaCantidades.add(productosRepository.countByCategorias(categoria));
        }
        model.addAttribute("listaCantCategorias",listaCantidades);


        model.addAttribute("categoriaActual",categorias);
        model.addAttribute("listaProductos",listaProductosPorCategoria);
        model.addAttribute("listarProducto",listarProducto);
        model.addAttribute("listaCategorias", listaCategorias);
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
    public String producto(Model model, @RequestParam(name = "idProductos") int idProductos){

        Productos productos = productosRepository.findById(idProductos).get();
        List<Productos> listaProductos = productosRepository.findAll();
        List<Categorias> listaCategorias = categoriasRepository.findAll();
        List<Long> listaCantidades = new ArrayList<>();
        for (Categorias categoria: listaCategorias){
            listaCantidades.add(productosRepository.countByCategorias(categoria));
        }

        model.addAttribute("productoActual",productos);

        model.addAttribute("listaCantCategorias",listaCantidades);
        model.addAttribute("listaProductos",listaProductos);
        model.addAttribute("listaCategorias", listaCategorias);
        return "FarmaciaWebVenta/producto";
    }





    @PostMapping(value = {"/clinicarenacer/paciente/guardardetalles"})
        public String guardarenCarrito(@RequestParam(name = "idProductos") Integer idProductos, @RequestParam(name = "idCarrito") Integer idCarrito ,RedirectAttributes attr){

        DetallesOrden detallesOrden = new DetallesOrden();
        detallesOrden.setOrdenes(ordenesRepository.findByIdordenes(idCarrito));
        detallesOrden.setProductos(productosRepository.findById(idProductos).get());
        detallesOrden.setCantidad(1);

        if(validarDuplicadoDeProductoEnUnaOrden(detallesOrden, ordenesRepository.findByIdordenes(idCarrito))){
            attr.addFlashAttribute("err","Ya se agregó al carrito.");

            return "redirect:/clinicarenacer";
        }
        detallesOrdenRepository.save(detallesOrden);

        return "redirect:/clinicarenacer";
    }

    @PostMapping(value = {"/clinicarenacer/paciente/guardarDatos"})
        public String guardarDatos(@RequestParam(name = "nombre") String nombre, @RequestParam(name = "correo") String correo,
                                   @RequestParam(name = "dni") String dni,  @RequestParam("archivo") MultipartFile file,
                                   RedirectAttributes attr, @RequestParam(name = "idCarrito") Integer idCarrito){
        if (file.isEmpty()) {
            attr.addFlashAttribute("err","Debe subir una foto de la receta.");
            return "redirect:/clinicarenacer/paciente/pagar";
        }
        String fileName = file.getOriginalFilename();
        if (fileName.contains("..")) {
            attr.addFlashAttribute("err","No se permiten '..' en el archivo");
            return "redirect:/clinicarenacer/paciente/pagar";
        }
        try {
            Ordenes ordenPreSave = ordenesRepository.findByIdordenes(idCarrito);
            ordenPreSave.setFotoReceta(file.getBytes());
            ordenPreSave.setFotonombre(fileName);
            ordenPreSave.setFotocontenttype(file.getContentType());

        } catch (IOException e) {
            e.printStackTrace();
            attr.addFlashAttribute("err","Error al subir la foto");
            return "redirect:/clinicarenacer/paciente/pagar";
        }

        Usuarios paciente = new Usuarios();

        if (!usuarioYaRegistrado(Integer.parseInt(dni))){ //Caso crear un paciente / el paciente no está registrado aún en el sistema
            paciente.setSedes(sedesRepository.findById(2).get()); // asignamos la sede actual
            paciente.setTipoUsuario(tipoUsuarioRepository.findById("Paciente").get());
            LocalDate fechaActual = LocalDateTime.now(ZoneId.of("America/New_York")).toLocalDate(); //sacamos la fecha actual
            paciente.setFechaRegistro(Date.from(fechaActual.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            paciente.setContrasena("Temporal_password");
            paciente.setCorreo(correo);
            paciente.setEstadoUsuario(estadoUsuarioRepository.findById("Activo").get());
            paciente.setNombre(nombre);
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
                return "redirect:/clinicarenacer/paciente/pagar";
            }

        }

        try {
            Ordenes ordenPreSave = ordenesRepository.findByIdordenes(idCarrito);
            ordenPreSave.setFotoReceta(file.getBytes());
            ordenPreSave.setFotonombre(fileName);
            ordenPreSave.setFotocontenttype(file.getContentType());

            ordenPreSave.setUsuarios(paciente);
            ordenPreSave.setTipoOrden(tipoOrdenRepository.findById(3).get());
            ordenesRepository.save(ordenPreSave);

            attr.addFlashAttribute("msg","Orden generada correctamente");

            return "redirect:/clinicarenacer";

        } catch (IOException e) {
            e.printStackTrace();
            attr.addFlashAttribute("err","Error al subir la foto");
            return "redirect:/clinicarenacer/paciente/pagar";
        }
    }

    @PostMapping(value = {"/clinicarenacer/paciente/eliminardetalles"})
        public String EliminarDetalleCarrito(@RequestParam(name = "idDetalle") Integer idDetalle, @RequestParam(name = "idCarrito", required = false) Integer idCarrito, RedirectAttributes attr){
        Optional<DetallesOrden> optionalDetallesOrden = detallesOrdenRepository.findById(idDetalle);
        if(optionalDetallesOrden.isPresent()){
            detallesOrdenRepository.delete(optionalDetallesOrden.get());
            attr.addFlashAttribute("msg","Se eliminó el producto correctamente.");
            return "redirect:/clinicarenacer/paciente/carrito?idCarrito=" + idCarrito;
        }
        else {
            attr.addFlashAttribute("err","No se pudo eliminar.");
            return "redirect:/clinicarenacer/paciente/carrito?idCarrito=" + idCarrito;
        }
    }

    @GetMapping(value = {"/clinicarenacer/paciente/carrito"})
        public String carrito(Model model, @RequestParam(name = "idCarrito", required = false) Integer idCarrito){
        Optional<Ordenes> optOrden = ordenesRepository.findById(idCarrito);

        if(optOrden.isPresent()){

            if(!validarTipoOrden(6,optOrden.get())){
                //if(!validarTipoOrden(3,optOrden.get()) || optOrden.get().getSedes().getIdSedes() != 2){
                    return "redirect:/clinicarenacer";
                //}
            }

            List<DetallesOrden> listaDetallesOrden= detallesOrdenRepository.findByOrdenes(optOrden.get());

            model.addAttribute("ordenCarrito",optOrden.get());
            model.addAttribute("listaDetallesOrden",listaDetallesOrden);

            return "FarmaciaWebVenta/carrito";
        }
        else {
            return "redirect:/clinicarenacer";
            }

        }

    @GetMapping(value ={"/clinicarenacer/paciente/pagar"})
    public String pagar(){
        return "FarmaciaWebVenta/pagarCarrito";
    }



    public boolean validarTipoOrden(Integer tipo_orden_idtipo_orden, Ordenes orden) {
        // Valida si una orden es de un tipo dado
        boolean valido = orden.getTipoOrden().getIdTipoOrden() == tipo_orden_idtipo_orden;
        return valido;
    }

    public boolean validarDuplicadoDeProductoEnUnaOrden(DetallesOrden detallesOrdenPorComprobar, Ordenes orden) {

        boolean valido = false;
        List<DetallesOrden> listaDetalles = detallesOrdenRepository.findByOrdenes(orden);
        for(DetallesOrden detalle : listaDetalles){
            if (detalle.getProductos().getIdProductos() == detallesOrdenPorComprobar.getProductos().getIdProductos()){
                valido = true;
            }
        }
        return valido;
    }


    public void crearOrdenCarrito(Usuarios usuario){
        Ordenes ordenCarrito = new Ordenes();
        ordenCarrito.setEstadoOrden(estadoOrdenRepository.findById(4).get()); //Estado se configura en aceptado (no importa mucho en una orden carrito)
        ordenCarrito.setTipoOrden(tipoOrdenRepository.findById(6).get()); // Tipo de orden 6 (carrito)
        ordenCarrito.setUsuarios(usuario);
        ordenCarrito.setTipoCobro(tipoCobroRepository.findById(1).get()); // Asignamos un tipo de cobro
        ordenCarrito.setSedes(sedesRepository.findById(2).get());

        ordenCarrito.setCodigo(UUID.randomUUID().toString());
        LocalDate fechaActual = LocalDateTime.now(ZoneId.of("America/New_York")).toLocalDate(); //sacamos la fecha actual
        ordenCarrito.setFechaRegistro(fechaActual.format(formatDateToSring));
        LocalDate fechaEntrega = fechaActual.plusDays(20);
        ordenCarrito.setFechaEntrega(fechaEntrega.format(formatDateToSring));

        ordenesRepository.save(ordenCarrito); // creamos la orden de reposición
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

