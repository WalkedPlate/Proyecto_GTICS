package com.example.proyecto_gtics.controller;

import com.example.proyecto_gtics.entity.*;
import com.example.proyecto_gtics.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

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

    @Autowired
    private PasswordEncoder passwordEncoder;


    //Formatear strings a dates
    DateTimeFormatter formatStringToDate = new DateTimeFormatterBuilder().append(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toFormatter();
    DateTimeFormatter formatDateToSring = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @GetMapping(value ={"/clinicarenacer"})
    public String paginaPrincipal(Model model, String nombre, @RequestParam(name = "noprimeraVez", required = false) Integer noprimeraVez,  HttpSession session){

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

        Usuarios paciente =(Usuarios)session.getAttribute("usuario");

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

    @GetMapping(value = "/clinicarenacer/historialPedidos")
    public String historialPedidos(Model model, String nombre, HttpSession session){
        Usuarios paciente =(Usuarios)session.getAttribute("usuario");

        List<Productos> buscarProductos = productosRepository.findByNombreContainingIgnoreCase(nombre);
        model.addAttribute("nombre", buscarProductos);
        List<Categorias> listaCategorias = categoriasRepository.findAll();
        model.addAttribute("listaCategorias", listaCategorias);


        TipoOrden web = tipoOrdenRepository.findById(3).get();

        List<Ordenes> listaOrdenes = ordenesRepository.findByTipoOrdenAndUsuarios(web,paciente);

        model.addAttribute("listaOrdenes",listaOrdenes);

        return "FarmaciaWebVenta/historialPedidos";
    }

    @GetMapping(value = "/clinicarenacer/paciente/verPedido")
    public String verPedido(Model model, @RequestParam("idOrden") Integer idOrden,  HttpSession session){

        Usuarios paciente =(Usuarios)session.getAttribute("usuario");

        Optional<Ordenes> opt = ordenesRepository.findById(idOrden);

        if(opt.isEmpty()){
            return "redirect:/clinicarenacer";
        }



        List<DetallesOrden> listaDetalles = detallesOrdenRepository.findByOrdenesIdordenes(idOrden);

        model.addAttribute("ordenActual",opt.get());
        model.addAttribute("listaDetalles",listaDetalles);

        return "FarmaciaWebVenta/verPedido";
    }


    @PostMapping(value = "/clinicarenacer/search")
    public String searchProducts(Model model,  String nombre,  HttpSession session ) {
        Usuarios paciente =(Usuarios)session.getAttribute("usuario");
        List<Productos> buscarProductos = productosRepository.findByNombreContainingIgnoreCase(nombre);
        model.addAttribute("nombre", buscarProductos);
        return "redirect:/clinicarenacer";
    }


    @GetMapping(value ={"/header"})
    public String cabecera(Model model, String nombre,  HttpSession session){
        Usuarios paciente =(Usuarios)session.getAttribute("usuario");
        List<Productos> buscarProductos = productosRepository.findByNombreContainingIgnoreCase(nombre);
        model.addAttribute("nombre", buscarProductos);
        List<Categorias> listaCategorias = categoriasRepository.findAll();
        model.addAttribute("listaCategorias", listaCategorias);
        return "FarmaciaWebVenta/fragments_clinicaweb/cabecera";
    }

    @GetMapping(value ={"/footer"})
    public String footer(Model model, String nombre,  HttpSession session){
        Usuarios paciente =(Usuarios)session.getAttribute("usuario");
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
    public String ordenarPorCategoria(Model model,  @RequestParam(name = "idCategoria") int idCategoria ,  HttpSession session){
        Usuarios paciente =(Usuarios)session.getAttribute("usuario");

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
    public String producto(Model model, @RequestParam(name = "idProductos") int idProductos,  HttpSession session){

        Usuarios paciente =(Usuarios)session.getAttribute("usuario");

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
        public String guardarenCarrito(@SessionAttribute("carrito") ArrayList<DetallesOrden> carrito, HttpSession session , @RequestParam(name = "idProductos") Integer idProductos, RedirectAttributes attr){

        Usuarios paciente =(Usuarios)session.getAttribute("usuario");

        DetallesOrden detallesOrden = new DetallesOrden();
        //detallesOrden.setOrdenes(ordenesRepository.findByIdordenes(idCarrito));
        Productos p =productosRepository.findById(idProductos).get();
        detallesOrden.setProductos(p);
        detallesOrden.setCantidad(1);
        detallesOrden.setMontoParcial(p.getPrecio()*1);


        if(validarDuplicadoDeProductoEnUnaOrden(detallesOrden, carrito)){
            attr.addFlashAttribute("err","Ya se agregó al carrito.");
            return "redirect:/clinicarenacer";
        }
        carrito.add(detallesOrden);
        session.setAttribute("carrito",carrito);

        //detallesOrdenRepository.save(detallesOrden);

        return "redirect:/clinicarenacer";
    }

    @PostMapping(value = {"/clinicarenacer/paciente/guardarDatos"})
        public String guardarDatos(@RequestParam(name = "nombre") String nombre, @RequestParam(name = "correo") String correo,
                                   @RequestParam(name = "dni") String dni,  @RequestParam("archivo") MultipartFile file,
                                   RedirectAttributes attr, @SessionAttribute("carrito") ArrayList<DetallesOrden> carrito, HttpSession session){
        Usuarios pacienteLogueado =(Usuarios)session.getAttribute("usuario");

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
            Ordenes ordenPreSave = new Ordenes();
            ordenPreSave.setFotoReceta(file.getBytes());
            ordenPreSave.setFotonombre(fileName);
            ordenPreSave.setFotocontenttype(file.getContentType());

        } catch (IOException e) {
            e.printStackTrace();
            attr.addFlashAttribute("err","Error al subir la foto");
            return "redirect:/clinicarenacer/paciente/pagar";
        }

        Usuarios paciente = new Usuarios();

        paciente = usuariosRepository.findByDni(paciente.getDni()).get();

        if(paciente.getTipoUsuario().getIdTipoUsuario().equalsIgnoreCase("Paciente")){
                attr.addFlashAttribute("wrn","Aviso: El DNI del paciente ya se encuentra registrado en el sistema. Se usarán los datos del paciente registrado en el sistema");
        }
        else {
            attr.addFlashAttribute("err","Aviso: El DNI  ya se encuentra registrado en el sistema y no pertenece a un usuario con el rol Paciente.");
            return "redirect:/clinicarenacer/paciente/pagar";
        }


        try {
            Ordenes ordenPreSave = new Ordenes();
            ordenPreSave.setFotoReceta(file.getBytes());
            ordenPreSave.setFotonombre(fileName);
            ordenPreSave.setFotocontenttype(file.getContentType());
            LocalDate fechaActual = LocalDateTime.now(ZoneId.of("America/New_York")).toLocalDate(); //sacamos la fecha actual
            ordenPreSave.setFechaRegistro(fechaActual.format(formatDateToSring));
            LocalDate fechaEntrega = fechaActual.plusDays(20);
            ordenPreSave.setFechaEntrega(fechaEntrega.format(formatDateToSring));

            ordenPreSave.setUsuarios(pacienteLogueado);
            ordenPreSave.setTipoOrden(tipoOrdenRepository.findById(3).get());
            ordenPreSave.setCodigo(UUID.randomUUID().toString());
            ordenPreSave.setEstadoOrden(estadoOrdenRepository.findByIdEstadoOrden(1));
            ordenPreSave.setTipoCobro(tipoCobroRepository.findById(1).get());
            ordenesRepository.save(ordenPreSave);

            Ordenes ordenRecuperada = ordenesRepository.findFirstByOrderByIdordenesDesc();

            float monto = 0;

            for(DetallesOrden detallesOrden: carrito){
                monto += detallesOrden.getMontoParcial();
                detallesOrden.setOrdenes(ordenRecuperada);
                detallesOrdenRepository.save(detallesOrden);
            }

            ordenRecuperada.setMonto(monto);
            ordenesRepository.save(ordenRecuperada);

            carrito.clear();
            session.setAttribute("carrito",carrito);

            attr.addFlashAttribute("msg","Orden generada correctamente");


            return "redirect:/clinicarenacer";

        } catch (IOException e) {
            e.printStackTrace();
            attr.addFlashAttribute("err","Error al subir la foto");
            return "redirect:/clinicarenacer/paciente/pagar";
        }
    }

    @GetMapping(value = {"/clinicarenacer/paciente/eliminardetalles"})
        public String EliminarDetalleCarrito(@RequestParam(name = "index",required = false) Integer index, @SessionAttribute("carrito") ArrayList<DetallesOrden> carrito, HttpSession session, RedirectAttributes attr){
        Usuarios paciente =(Usuarios)session.getAttribute("usuario");
        DetallesOrden detalle = carrito.get(index);
        if(detalle != null){
            carrito.remove(detalle);
            session.setAttribute("carrito",carrito);
            attr.addFlashAttribute("msg","Se eliminó el producto correctamente.");
            return "redirect:/clinicarenacer/paciente/carrito";
        }
        else {
            attr.addFlashAttribute("err","No se pudo eliminar.");
            return "redirect:/clinicarenacer/paciente/carrito";
        }
    }

    @GetMapping(value = {"/clinicarenacer/paciente/actualizardetalles"})
    public String actualizarDetalleCarrito(@RequestParam(name = "index",required = false) Integer index, @RequestParam(name = "cantidad", required = false) Integer cantidad,
                                           RedirectAttributes attr, @SessionAttribute("carrito") ArrayList<DetallesOrden> carrito, HttpSession session){
            Usuarios paciente =(Usuarios)session.getAttribute("usuario");
            DetallesOrden detalle = carrito.get(index);

            if(cantidad>0){
                detalle.setCantidad(cantidad);
                detalle.setMontoParcial(cantidad*detalle.getProductos().getPrecio());
                session.setAttribute("carrito",carrito);
                attr.addFlashAttribute("msg","La cantidad se actualizó correctamente.");
                return "redirect:/clinicarenacer/paciente/carrito";
            }
            else {
                attr.addFlashAttribute("err","La cantidad debe ser positiva.");
                return "redirect:/clinicarenacer/paciente/carrito";
            }
    }

    @GetMapping(value = {"/clinicarenacer/paciente/pagarCarrito"})
    public String pagarCarrito(Model model, @SessionAttribute("carrito") ArrayList<DetallesOrden> carrito,  HttpSession session){
        Usuarios paciente =(Usuarios)session.getAttribute("usuario");
        model.addAttribute("listaDetallesOrden",carrito);
        TipoUsuario doctores = tipoUsuarioRepository.findById("Doctor").get();
        EstadoUsuario estado = estadoUsuarioRepository.findById("Activo").get();
        List<Usuarios> listaDoctores = usuariosRepository.findByTipoUsuarioAndEstadoUsuario(doctores,estado);
        model.addAttribute("listaDoctores",listaDoctores);

        Ordenes ordenRecuperada = ordenesRepository.findFirstByOrderByIdordenesDesc();

        float monto = 0;

        for(DetallesOrden detallesOrden: carrito){
            monto += detallesOrden.getMontoParcial();
            detallesOrden.setOrdenes(ordenRecuperada);
            detallesOrdenRepository.save(detallesOrden);
        }

        ordenRecuperada.setMonto(monto);


        model.addAttribute("carrito",ordenRecuperada);


        List<Sedes> listaSedes = sedesRepository.findAll();
        model.addAttribute("listaSedes",listaSedes);
        return "FarmaciaWebVenta/pagarCarrito";
    }



    @GetMapping(value = {"/clinicarenacer/paciente/carrito"})
        public String carrito(Model model, @SessionAttribute("carrito") ArrayList<DetallesOrden> carrito, String nombre,  HttpSession session){

        Usuarios paciente =(Usuarios)session.getAttribute("usuario");

        List<Productos> buscarProductos = productosRepository.findByNombreContainingIgnoreCase(nombre);
        model.addAttribute("nombre", buscarProductos);
        List<Categorias> listaCategorias = categoriasRepository.findAll();
        model.addAttribute("listaCategorias", listaCategorias);
        //List<DetallesOrden> listaDetallesOrden= detallesOrdenRepository.findByOrdenes(optOrden.get());

        //model.addAttribute("ordenCarrito",optOrden.get());
        model.addAttribute("listaDetallesOrden",carrito);
        return "FarmaciaWebVenta/carrito";

        //Optional<Ordenes> optOrden = ordenesRepository.findById(idCarrito);

        //if(idCarrito == null){
            //return "FarmaciaWebVenta/carrito";
        //}



        //if(optOrden.isPresent()){

            //if(!validarTipoOrden(6,optOrden.get())){
                //if(!validarTipoOrden(3,optOrden.get()) || optOrden.get().getSedes().getIdSedes() != 2){
                    //return "redirect:/clinicarenacer";
                //}
            //}

        //}
        //else {
           // return "redirect:/clinicarenacer";
           // }

    }






    public boolean validarTipoOrden(Integer tipo_orden_idtipo_orden, Ordenes orden) {
        // Valida si una orden es de un tipo dado
        boolean valido = orden.getTipoOrden().getIdTipoOrden() == tipo_orden_idtipo_orden;
        return valido;
    }

    public boolean validarDuplicadoDeProductoEnUnaOrden(DetallesOrden detallesOrdenPorComprobar, List<DetallesOrden> listaDetalles) {

        boolean valido = false;
        //List<DetallesOrden> listaDetalles = detallesOrdenRepository.findByOrdenes(orden);
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

