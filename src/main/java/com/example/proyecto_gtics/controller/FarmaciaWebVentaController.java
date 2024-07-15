package com.example.proyecto_gtics.controller;

import com.example.proyecto_gtics.dto.ProductoMejorVendido;
import com.example.proyecto_gtics.dto.ProductosAnadidosRecientemente;
import com.example.proyecto_gtics.dto.ProductosMejorValorados;
import com.example.proyecto_gtics.dto.ProductosTendencia;
import com.example.proyecto_gtics.entity.*;
import com.example.proyecto_gtics.repository.*;
import com.example.proyecto_gtics.service.BuscarProductosService;
import com.example.proyecto_gtics.service.CardService;
import com.example.proyecto_gtics.service.MessageService;
import com.example.proyecto_gtics.service.ProductoBuscarServicio;
import com.itextpdf.text.pdf.qrcode.Mode;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
                                TipoUsuarioRepository tipoUsuarioRepository, UsuariosRepository usuariosRepository
                                ){

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
    @Autowired
    private TipoChatRepository tipoChatRepository;
    @Autowired
    private MensajesRepository mensajesRepository;
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private MessageService messageService;
    @Autowired
    private CardService cardService;
    @Autowired
    private ProductoBuscarServicio productoBuscarServicio;
    @Autowired
    private BuscarProductosService buscarProductosService;
    @Autowired
    private DistritosRepository distritosRepository;

    //Formatear strings a dates
    DateTimeFormatter formatStringToDate = new DateTimeFormatterBuilder().append(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toFormatter();
    DateTimeFormatter formatDateToSring = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @GetMapping(value ={"/clinicarenacer"})
    public String paginaPrincipal(Model model,  HttpSession session){

        Usuarios paciente =(Usuarios)session.getAttribute("usuario");
        Usuarios superAdmin = (Usuarios) session.getAttribute("originalUser");//Superadmin logueado
        //Verificamos que el superadmin no pueda acceder a administrador de sede sin una sesion
        if(Objects.equals(paciente.getTipoUsuario().getIdTipoUsuario(), "SuperAdmin")){
            return "redirect:/superadmin";
        }
        //-------------------------------------------------------------------------------------
        if(superAdmin != null){
            model.addAttribute("superAdmin",superAdmin);
        }
        model.addAttribute("paciente", paciente);

        // Obtener todos los productos
        List<Productos> listaProductos = productosRepository.findAll();

        // Barajar la lista de productos de forma aleatoria
        Collections.shuffle(listaProductos, new Random());

        // Tomar los primeros 20 productos de la lista barajada
        List<Productos> productosAleatorios = listaProductos.stream()
                .limit(20)
                .collect(Collectors.toList());

        List<ProductosTendencia> listarProductosTendencia = productosRepository.obtenerProductosMasComprados();
        List<ProductosMejorValorados> listarProductosMejorValorados = productosRepository.obtenerProductosMejorValorados();
        List<ProductosAnadidosRecientemente> listarProductosRecientes = productosRepository.obtenerProductosRecientes();
        ProductoMejorVendido productoMejorVendido = productosRepository.obtenerProductoMejorVendido();

        List<Categorias> listaCategorias = categoriasRepository.findAll();
        List<Long> listaCantidades = new ArrayList<>();
        for (Categorias categorias: listaCategorias){
            listaCantidades.add(productosRepository.countByCategorias(categorias));
        }

        model.addAttribute("listaCantCategorias",listaCantidades);
        model.addAttribute("listaProductos",productosAleatorios);
        model.addAttribute("listaCategorias", listaCategorias);
        model.addAttribute("listaTendencia",listarProductosTendencia);
        model.addAttribute("listarProductosMejorValorados",listarProductosMejorValorados);
        model.addAttribute("listarProductosRecientes",listarProductosRecientes);
        model.addAttribute("productoMejorVendido",productoMejorVendido);

        return "FarmaciaWebVenta/index";
    }

    @GetMapping(value = "/clinicarenacer/historialPedidos")
    public String historialPedidos(Model model, String nombre, HttpSession session){
        Usuarios paciente =(Usuarios)session.getAttribute("usuario");
        Usuarios superAdmin = (Usuarios) session.getAttribute("originalUser");//Superadmin logueado
        //Verificamos que el superadmin no pueda acceder a administrador de sede sin una sesion
        if(Objects.equals(paciente.getTipoUsuario().getIdTipoUsuario(), "SuperAdmin")){
            return "redirect:/superadmin";
        }
        //-------------------------------------------------------------------------------------
        if(superAdmin != null){
            model.addAttribute("superAdmin",superAdmin);
        }
        model.addAttribute("paciente", paciente);

        List<Productos> buscarProductos = productosRepository.findByNombreContainingIgnoreCase(nombre);
        model.addAttribute("nombre", buscarProductos);
        List<Categorias> listaCategorias = categoriasRepository.findAll();
        model.addAttribute("listaCategorias", listaCategorias);


        TipoOrden web = tipoOrdenRepository.findById(3).get();

        List<Ordenes> listaOrdenes = ordenesRepository.findByUsuarios(paciente);

        model.addAttribute("listaOrdenes",listaOrdenes);

        return "FarmaciaWebVenta/historialPedidos";
    }

    @GetMapping(value = "/clinicarenacer/paciente/verPedido")
    public String verPedido(Model model, @RequestParam(value = "idOrden", required = false) Integer idOrden,  HttpSession session, String nombre){

        Usuarios paciente =(Usuarios)session.getAttribute("usuario");
        Usuarios superAdmin = (Usuarios) session.getAttribute("originalUser");//Superadmin logueado
        //Verificamos que el superadmin no pueda acceder a administrador de sede sin una sesion
        if(Objects.equals(paciente.getTipoUsuario().getIdTipoUsuario(), "SuperAdmin")){
            return "redirect:/superadmin";
        }
        //-------------------------------------------------------------------------------------
        if(superAdmin != null){
            model.addAttribute("superAdmin",superAdmin);
        }
        model.addAttribute("paciente", paciente);

        //Si se intenta ingresar sin un id
        if (idOrden == null){
            return "redirect:/clinicarenacer";
        }

        List<Productos> buscarProductos = productosRepository.findByNombreContainingIgnoreCase(nombre);
        model.addAttribute("nombre", buscarProductos);
        List<Categorias> listaCategorias = categoriasRepository.findAll();
        model.addAttribute("listaCategorias", listaCategorias);
        List<Ordenes> listaOrdenes = ordenesRepository.findByUsuarios(paciente);

        model.addAttribute("listaOrdenes",listaOrdenes);

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
    public String searchProducts(Model model, @RequestParam("nombre") String nombre,  HttpSession session ) {
        try {
            Usuarios paciente = (Usuarios) session.getAttribute("usuario");
            List<Productos> buscarProductos = productosRepository.findByNombreContainingIgnoreCase(nombre);
            List<Categorias> listaCategorias = categoriasRepository.findAll();
            model.addAttribute("listaCategorias", listaCategorias);
            model.addAttribute("productosEncontrados", buscarProductos);
            return "FarmaciaWebVenta/productosBusqueda";  // Vista para mostrar los resultados
        } catch (Exception e) {
            // Log error
            System.err.println("Error durante la búsqueda de productos: " + e.getMessage());
            model.addAttribute("errorMessage", "Hubo un error durante la búsqueda de productos.");
            return "FarmaciaWebVenta/Error";  // Ajusta esta vista según tus necesidades
        }
    }


    @GetMapping(value ={"/header"})
    public String cabecera(Model model, String nombre,  HttpSession session){
        Usuarios paciente =(Usuarios)session.getAttribute("usuario");
        Usuarios superAdmin = (Usuarios) session.getAttribute("originalUser");//Superadmin logueado
        //Verificamos que el superadmin no pueda acceder a administrador de sede sin una sesion
        if(Objects.equals(paciente.getTipoUsuario().getIdTipoUsuario(), "SuperAdmin")){
            return "redirect:/superadmin";
        }
        //-------------------------------------------------------------------------------------
        if(superAdmin != null){
            model.addAttribute("superAdmin",superAdmin);
        }
        model.addAttribute("paciente", paciente);

        List<Productos> buscarProductos = productoBuscarServicio.searchProductos(nombre);
        model.addAttribute("nombre", buscarProductos);
        List<Categorias> listaCategorias = categoriasRepository.findAll();
        model.addAttribute("listaCategorias", listaCategorias);
        return "FarmaciaWebVenta/fragments_clinicaweb/cabecera";
    }

    @GetMapping(value ={"/footer"})
    public String footer(Model model, String nombre,  HttpSession session){
        Usuarios paciente =(Usuarios)session.getAttribute("usuario");
        Usuarios superAdmin = (Usuarios) session.getAttribute("originalUser");//Superadmin logueado
        //Verificamos que el superadmin no pueda acceder a administrador de sede sin una sesion
        if(Objects.equals(paciente.getTipoUsuario().getIdTipoUsuario(), "SuperAdmin")){
            return "redirect:/superadmin";
        }
        //-------------------------------------------------------------------------------------
        if(superAdmin != null){
            model.addAttribute("superAdmin",superAdmin);
        }
        model.addAttribute("paciente", paciente);

        List<Productos> buscarProductos = productosRepository.findByNombreContainingIgnoreCase(nombre);
        model.addAttribute("nombre", buscarProductos);
        List<Categorias> listaCategorias = categoriasRepository.findAll();
        model.addAttribute("listaCategorias", listaCategorias);
        return "FarmaciaWebVenta/fragments_clinicaweb/footer";
    }

    @GetMapping(value ={"/clinicarenacer/paciente"})
    public String paginaPrincipalConLogin(HttpSession session,Model model){
        Usuarios paciente =(Usuarios)session.getAttribute("usuario");
        Usuarios superAdmin = (Usuarios) session.getAttribute("originalUser");//Superadmin logueado
        //Verificamos que el superadmin no pueda acceder a administrador de sede sin una sesion
        if(Objects.equals(paciente.getTipoUsuario().getIdTipoUsuario(), "SuperAdmin")){
            return "redirect:/superadmin";
        }
        //-------------------------------------------------------------------------------------
        if(superAdmin != null){
            model.addAttribute("superAdmin",superAdmin);
        }
        model.addAttribute("paciente", paciente);

        return "FarmaciaWebVenta/user";
    }

    @GetMapping(value ={"/clinicarenacer/categoria"})
    public String ordenarPorCategoria(Model model,  @RequestParam(name = "idCategoria") int idCategoria ,
                                      @RequestParam("page") Optional<Integer> page,
                                      @RequestParam("size") Optional<Integer> size,
                                      @RequestHeader(value = "X-Requested-With", required = false) String requestedWith,
                                      HttpSession session){
        Usuarios paciente =(Usuarios)session.getAttribute("usuario");
        Usuarios superAdmin = (Usuarios) session.getAttribute("originalUser");//Superadmin logueado
        //Verificamos que el superadmin no pueda acceder a administrador de sede sin una sesion
        if(Objects.equals(paciente.getTipoUsuario().getIdTipoUsuario(), "SuperAdmin")){
            return "redirect:/superadmin";
        }
        //-------------------------------------------------------------------------------------
        if(superAdmin != null){
            model.addAttribute("superAdmin",superAdmin);
        }
        model.addAttribute("paciente", paciente);

        Categorias categorias = categoriasRepository.findById(idCategoria).get();

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(8);

        Page<Productos> productosPage = productosRepository.findByCategorias(categorias, PageRequest.of(currentPage - 1, pageSize));

        List<Productos> listaProductosPorCategoria = productosRepository.findByCategorias(categorias);
        List<Productos> listarProducto = productosRepository.findAll();
        List<Categorias> listaCategorias = categoriasRepository.findAll();
        List<Long> listaCantidades = new ArrayList<>();
        for (Categorias categoria: listaCategorias){
            listaCantidades.add(productosRepository.countByCategorias(categoria));
        }
        model.addAttribute("categoriaActual",categorias);
        model.addAttribute("listaProductos",productosPage.getContent());
        model.addAttribute("listarProducto",listarProducto);
        model.addAttribute("listaCategorias", listaCategorias);
        model.addAttribute("listaCantCategorias",listaCantidades);
        model.addAttribute("productoPage", productosPage);

        int totalPages = productosPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "FarmaciaWebVenta/PorCategoria";

    }

    @GetMapping(value ={"/clinicarenacer/categoria/subcategoria"})
    public String ordenarPorSubcategoria(Model model,HttpSession session){
        Usuarios paciente =(Usuarios)session.getAttribute("usuario");
        Usuarios superAdmin = (Usuarios) session.getAttribute("originalUser");//Superadmin logueado
        //Verificamos que el superadmin no pueda acceder a administrador de sede sin una sesion
        if(Objects.equals(paciente.getTipoUsuario().getIdTipoUsuario(), "SuperAdmin")){
            return "redirect:/superadmin";
        }
        //-------------------------------------------------------------------------------------
        if(superAdmin != null){
            model.addAttribute("superAdmin",superAdmin);
        }
        model.addAttribute("paciente", paciente);
        return "FarmaciaWebVenta/porSubcategoria";
    }

    @GetMapping(value ={"/clinicarenacer/chatfarmacista"})
    public String chatFarmacista(HttpSession session, Model model, @RequestParam(name = "chatId", required = false) Integer chatId,
                                 RedirectAttributes attr){

        Usuarios paciente = (Usuarios) session.getAttribute("usuario"); // Paciente logueado
        Usuarios superAdmin = (Usuarios) session.getAttribute("originalUser");//Superadmin logueado
        //Verificamos que el superadmin no pueda acceder a administrador de sede sin una sesion
        if(Objects.equals(paciente.getTipoUsuario().getIdTipoUsuario(), "SuperAdmin")){
            return "redirect:/superadmin";
        }
        //-------------------------------------------------------------------------------------
        if(superAdmin != null){
            model.addAttribute("superAdmin",superAdmin);
        }
        model.addAttribute("paciente", paciente);


        if(chatId == null){
            Optional<Chat> opt = chatRepository.findFirstByUsuario2OrderByIdChatDesc(paciente);
            if(opt.isPresent()){
                Chat chat = opt.get();
                if(messageService.verificarAccesoChat(chat.getIdChat(),paciente)){
                    List<Chat> listaChatsPaciente = chatRepository.findByUsuario2OrderByIdChatDesc(paciente);
                    List<Mensajes> listaUltimosMensajes = new ArrayList<>();

                    for(Chat ch: listaChatsPaciente){
                        Mensajes ultimoMensaje = mensajesRepository.findFirstByChatAndSenderOrderByIdMensajesDesc(ch,1);
                        listaUltimosMensajes.add(ultimoMensaje);
                    }

                    model.addAttribute("listaUltimosMensajes",listaUltimosMensajes);
                    model.addAttribute("listaChatsPaciente",listaChatsPaciente);
                    model.addAttribute("chat",chat);
                    return "FarmaciaWebVenta/chat";
                }
                else {
                    attr.addFlashAttribute("err","No tienes acceso a ese chat.");
                    return "redirect:/clinicarenacer";
                }
            }
            else {
                attr.addFlashAttribute("err","El paciente aún no tiene chats.");
                return "redirect:/clinicarenacer";
            }

        }

        if(messageService.verificarAccesoChat(chatId,paciente)){
            Chat chat = chatRepository.findById(chatId).get();
            List<Chat> listaChatsPaciente = chatRepository.findByUsuario2OrderByIdChatDesc(paciente);
            List<Mensajes> listaUltimosMensajes = new ArrayList<>();

            for(Chat ch: listaChatsPaciente){
                Mensajes ultimoMensaje = mensajesRepository.findFirstByChatAndSenderOrderByIdMensajesDesc(ch,1);
                listaUltimosMensajes.add(ultimoMensaje);
            }

            model.addAttribute("listaUltimosMensajes",listaUltimosMensajes);
            model.addAttribute("listaChatsPaciente",listaChatsPaciente);
            model.addAttribute("chat",chat);
            return "FarmaciaWebVenta/chat";
        }
        else {
            attr.addFlashAttribute("err","No tienes acceso a ese chat.");
            return "redirect:/clinicarenacer";
        }


    }

    @GetMapping(value ={"/clinicarenacer/producto"})
    public String producto(Model model,
                           @RequestParam(name = "idProductos") int idProductos,
                           HttpSession session){

        Usuarios paciente =(Usuarios)session.getAttribute("usuario");
        Usuarios superAdmin = (Usuarios) session.getAttribute("originalUser");//Superadmin logueado
        //Verificamos que el superadmin no pueda acceder a administrador de sede sin una sesion
        if(Objects.equals(paciente.getTipoUsuario().getIdTipoUsuario(), "SuperAdmin")){
            return "redirect:/superadmin";
        }
        //-------------------------------------------------------------------------------------
        if(superAdmin != null){
            model.addAttribute("superAdmin",superAdmin);
        }
        model.addAttribute("paciente", paciente);
        Optional<Productos> optionalProducto = productosRepository.findById(idProductos);

        if (optionalProducto.isPresent()) {
            Productos productos = optionalProducto.get();

            Pageable pageable = PageRequest.of(0, 4); // Limitar a los 4 primeros productos
            List<Productos> mejoresProductos = productosRepository.findTop4ProductosByCategoria(productos.getCategorias().getIdCategorias(), pageable);

            List<Productos> listaProductos = productosRepository.findAll();
            List<Categorias> listaCategorias = categoriasRepository.findAll();
            List<Long> listaCantidades = new ArrayList<>();
            for (Categorias categoria : listaCategorias) {
                listaCantidades.add(productosRepository.countByCategorias(categoria));
            }

            model.addAttribute("productoActual", productos);
            model.addAttribute("mejoresProductos", mejoresProductos);
            model.addAttribute("listaCantCategorias", listaCantidades);
            model.addAttribute("listaProductos", listaProductos);
            model.addAttribute("listaCategorias", listaCategorias);
            return "FarmaciaWebVenta/producto";
        } else {
            // Manejar el caso donde el producto no fue encontrado
            return "redirect:/error";
        }

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
        public String guardarDatos(@RequestParam(name = "paymentMethod") String paymentMethod,
                                   @RequestParam("archivo") MultipartFile file,
                                   @RequestParam(name = "tipoEntrega") String tipoEntrega,
                                   @RequestParam(name = "direccion", required = false, defaultValue = "") String direccion,
                                   @RequestParam(name = "distrito", required = false, defaultValue = "") String distrito,
                                   @RequestParam(name = "iddoctor", required = false) Integer iddoctor,
                                   @RequestParam(name = "idSede", required = false) Integer idSede,
                                   RedirectAttributes attr, @SessionAttribute("carrito") ArrayList<DetallesOrden> carrito, HttpSession session){
        Usuarios pacienteLogueado =(Usuarios) session.getAttribute("usuario");

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
            // Determinar el tipo de cobro
            if ("cash".equals(paymentMethod)) {
                ordenPreSave.setTipoCobro(tipoCobroRepository.findById(1).get()); // Pago en efectivo
            } else if ("card".equals(paymentMethod)) {
                ordenPreSave.setTipoCobro(tipoCobroRepository.findById(2).get()); // Pago con tarjeta
            }

            Usuarios doctor = null;
            Sedes sede = null;
            if(iddoctor != null){
                doctor = usuariosRepository.findById(iddoctor).orElse(null);
            }

            if(idSede != null){
                sede = sedesRepository.findById(idSede).orElse(null);
            }



            // Guardar dirección de entrega según el tipo de entrega seleccionado
            if ("delivery".equals(tipoEntrega)) {
                ordenPreSave.setDireccion(direccion + ',' + distrito);
            } else {
                // Si es recojo en farmacia, no se necesita dirección
                ordenPreSave.setDireccion("Recojo en farmacia");
                ordenPreSave.setDoctor(doctor);
                ordenPreSave.setSedes(sede);
            }
            ordenesRepository.save(ordenPreSave);


            Ordenes ordenRecuperada =  ordenesRepository.save(ordenPreSave);;

            //Seteamos el codigo en base al ID de la orden
            ordenRecuperada.setCodigo("ORD-WEB-"+ordenRecuperada.getIdordenes());
            ordenesRepository.save(ordenRecuperada);

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
        public String EliminarDetalleCarrito(@RequestParam(name = "index",required = false) Integer index, @SessionAttribute("carrito") ArrayList<DetallesOrden> carrito, HttpSession session, RedirectAttributes attr,Model model){
        Usuarios paciente =(Usuarios)session.getAttribute("usuario");
        Usuarios superAdmin = (Usuarios) session.getAttribute("originalUser");//Superadmin logueado
        //Verificamos que el superadmin no pueda acceder a administrador de sede sin una sesion
        if(Objects.equals(paciente.getTipoUsuario().getIdTipoUsuario(), "SuperAdmin")){
            return "redirect:/superadmin";
        }
        //-------------------------------------------------------------------------------------
        if(superAdmin != null){
            model.addAttribute("superAdmin",superAdmin);
        }
        model.addAttribute("paciente", paciente);
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
                                           RedirectAttributes attr, @SessionAttribute("carrito") ArrayList<DetallesOrden> carrito, HttpSession session,Model model){
            Usuarios paciente =(Usuarios)session.getAttribute("usuario");
        Usuarios superAdmin = (Usuarios) session.getAttribute("originalUser");//Superadmin logueado
        //Verificamos que el superadmin no pueda acceder a administrador de sede sin una sesion
        if(Objects.equals(paciente.getTipoUsuario().getIdTipoUsuario(), "SuperAdmin")){
            return "redirect:/superadmin";
        }
        //-------------------------------------------------------------------------------------
        if(superAdmin != null){
            model.addAttribute("superAdmin",superAdmin);
        }
        model.addAttribute("paciente", paciente);
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
        Usuarios superAdmin = (Usuarios) session.getAttribute("originalUser");//Superadmin logueado
        //Verificamos que el superadmin no pueda acceder a administrador de sede sin una sesion
        if(Objects.equals(paciente.getTipoUsuario().getIdTipoUsuario(), "SuperAdmin")){
            return "redirect:/superadmin";
        }
        //-------------------------------------------------------------------------------------
        if(superAdmin != null){
            model.addAttribute("superAdmin",superAdmin);
        }
        model.addAttribute("paciente", paciente);
        model.addAttribute("listaDetallesOrden",carrito);
        TipoUsuario doctores = tipoUsuarioRepository.findById("Doctor").get();
        EstadoUsuario estado = estadoUsuarioRepository.findById("Activo").get();
        List<Usuarios> listaDoctores = usuariosRepository.findByTipoUsuarioAndEstadoUsuario(doctores,estado);
        model.addAttribute("listaDoctores",listaDoctores);


        float monto = 0;

        for(DetallesOrden detallesOrden: carrito){
            monto += detallesOrden.getMontoParcial();
        }
        model.addAttribute("carrito",monto);

        List<Sedes> listaSedes = sedesRepository.findAll();
        model.addAttribute("listaSedes",listaSedes);

        List<Distritos> listaDistritos = distritosRepository.findAll();
        model.addAttribute("listaDistritos",listaDistritos);
        return "FarmaciaWebVenta/pagarCarrito";
    }



    @GetMapping(value = {"/clinicarenacer/paciente/carrito"})
        public String carrito(Model model, @SessionAttribute("carrito") ArrayList<DetallesOrden> carrito, String nombre,  HttpSession session){

        Usuarios paciente =(Usuarios)session.getAttribute("usuario");
        Usuarios superAdmin = (Usuarios) session.getAttribute("originalUser");//Superadmin logueado
        //Verificamos que el superadmin no pueda acceder a administrador de sede sin una sesion
        if(Objects.equals(paciente.getTipoUsuario().getIdTipoUsuario(), "SuperAdmin")){
            return "redirect:/superadmin";
        }
        //-------------------------------------------------------------------------------------
        if(superAdmin != null){
            model.addAttribute("superAdmin",superAdmin);
        }
        model.addAttribute("paciente", paciente);

        List<Productos> buscarProductos = productosRepository.findByNombreContainingIgnoreCase(nombre);
        model.addAttribute("nombre", buscarProductos);
        List<Categorias> listaCategorias = categoriasRepository.findAll();
        model.addAttribute("listaCategorias", listaCategorias);
        //List<DetallesOrden> listaDetallesOrden= detallesOrdenRepository.findByOrdenes(optOrden.get());

        //model.addAttribute("ordenCarrito",optOrden.get());
        model.addAttribute("listaDetallesOrden",carrito);
        return "FarmaciaWebVenta/carrito";

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

    @GetMapping(value ={"/clinicarenacer/perfil"})
    public String perfil(Model model,HttpSession session){
        Usuarios paciente = (Usuarios) session.getAttribute("usuario"); // Paciente Logueado
        Usuarios superAdmin = (Usuarios) session.getAttribute("originalUser");//Superadmin logueado
        //Verificamos que el superadmin no pueda acceder a administrador de sede sin una sesion
        if(Objects.equals(paciente.getTipoUsuario().getIdTipoUsuario(), "SuperAdmin")){
            return "redirect:/superadmin";
        }
        //-------------------------------------------------------------------------------------
        if(superAdmin != null){
            model.addAttribute("superAdmin",superAdmin);
        }
        model.addAttribute("paciente", paciente);

        List<Categorias> listaCategorias = categoriasRepository.findAll();
        model.addAttribute("listaCategorias", listaCategorias);

        return "FarmaciaWebVenta/perfil";
    }

    @GetMapping(value ={"/clinicarenacer/editar-perfil"})
    public String editarPerfil(Model model,HttpSession session){
        Usuarios paciente = (Usuarios) session.getAttribute("usuario"); // Paciente Logueado
        Usuarios superAdmin = (Usuarios) session.getAttribute("originalUser");//Superadmin logueado
        //Verificamos que el superadmin no pueda acceder a administrador de sede sin una sesion
        if(Objects.equals(paciente.getTipoUsuario().getIdTipoUsuario(), "SuperAdmin")){
            return "redirect:/superadmin";
        }
        //-------------------------------------------------------------------------------------
        if(superAdmin != null){
            model.addAttribute("superAdmin",superAdmin);
        }
        model.addAttribute("paciente", paciente);

        List<Categorias> listaCategorias = categoriasRepository.findAll();
        model.addAttribute("listaCategorias", listaCategorias);
        List<Distritos> listaDistritos = distritosRepository.findAll();
        model.addAttribute("listaDistritos",listaDistritos);
        return "FarmaciaWebVenta/editarPerfil";
    }

    @GetMapping(value ={"/clinicarenacer/cambiar-contra"})
    public String cambiarContra(Model model,HttpSession session){
        Usuarios paciente = (Usuarios) session.getAttribute("usuario"); // Paciente Logueado
        Usuarios superAdmin = (Usuarios) session.getAttribute("originalUser");//Superadmin logueado
        //Verificamos que el superadmin no pueda acceder a administrador de sede sin una sesion
        if(Objects.equals(paciente.getTipoUsuario().getIdTipoUsuario(), "SuperAdmin")){
            return "redirect:/superadmin";
        }
        //-------------------------------------------------------------------------------------
        if(superAdmin != null){
            model.addAttribute("superAdmin",superAdmin);
        }
        model.addAttribute("paciente", paciente);

        List<Categorias> listaCategorias = categoriasRepository.findAll();
        model.addAttribute("listaCategorias", listaCategorias);

        return "FarmaciaWebVenta/cambiarContra";
    }


    @PostMapping(value = "/clinicarenacer/actualizar-contra")
    public String actualizarContra(@RequestParam(name = "pass1", required = false) String pass1,
                                   @RequestParam(name = "pass2", required = false) String pass2,
                                   RedirectAttributes attr, HttpSession session, Model model){

        Usuarios paciente = (Usuarios) session.getAttribute("usuario"); // Paciente Logueado
        Usuarios superAdmin = (Usuarios) session.getAttribute("originalUser");//Superadmin logueado
        //Verificamos que el superadmin no pueda acceder a administrador de sede sin una sesion
        if(Objects.equals(paciente.getTipoUsuario().getIdTipoUsuario(), "SuperAdmin")){
            return "redirect:/superadmin";
        }
        //-------------------------------------------------------------------------------------
        if(superAdmin != null){
            model.addAttribute("superAdmin",superAdmin);
        }
        model.addAttribute("paciente", paciente);


        if(pass1 == null || pass2 == null){
            attr.addFlashAttribute("err","Debe rellenar los campos.");
            return "redirect:/clinicarenacer/cambiar-contra";
        }
        if(pass1.isEmpty() || pass2.isEmpty()){
            attr.addFlashAttribute("err","Debe rellenar los campos.");
            return "redirect:/clinicarenacer/cambiar-contra";
        }


        if(pass1.equalsIgnoreCase(pass2)){
            paciente.setContrasena(passwordEncoder.encode(pass1));
            usuariosRepository.save(paciente);
            attr.addFlashAttribute("msg","Contraseña actualizada exitosamente.");
            return "redirect:/clinicarenacer/cambiar-contra";
        }
        else {
            attr.addFlashAttribute("wrn","Las contraseñas no coinciden.");
            return "redirect:/clinicarenacer/cambiar-contra";
        }

    }


    @PostMapping(value = "/clinicarenacer/guardar_perfil")
    public String actualizarPerfil(@RequestParam(name = "direccion", required = false) String direccion,
                                   @RequestParam(name = "distrito", required = false) String distrito,
                                   @RequestParam(name = "correo",required = false) String correo,
                                   @RequestParam("image") MultipartFile file,
                                   RedirectAttributes attr, HttpSession session){

        Usuarios paciente = (Usuarios) session.getAttribute("usuario"); // Paciente Logueado

        if(direccion == null || distrito == null || correo == null){
            attr.addFlashAttribute("msg","Debe rellenar los campos.");
            return "redirect:/clinicarenacer/editar-perfil";
        }
        if(direccion.isEmpty() || distrito.isEmpty() || correo.isEmpty()){
            attr.addFlashAttribute("msg","Debe rellenar los campos.");
            return "redirect:/clinicarenacer/editar-perfil";
        }


        //Sobre la foto de un producto --------------------------------------------------------------------------------
        if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            if (fileName.contains("..")) {
                attr.addFlashAttribute("err","No se permiten '..' en el archivo");
                return "redirect:/clinicarenacer/editar-perfil";
            }
            try {
                paciente.setFoto(file.getBytes());
                paciente.setFotonombre(fileName);
                paciente.setFotocontenttype(file.getContentType());

            } catch (IOException e) {
                e.printStackTrace();
                attr.addFlashAttribute("err","No se permiten '..' en el archivo");
                return "redirect:/clinicarenacer/editar-perfil";
            }
        }
        //-----------------------------------------------------------------------------------------------------------


        paciente.setCorreo(correo);
        paciente.setDireccion(direccion);
        paciente.setDistritoResidencia(distrito);
        usuariosRepository.save(paciente);
        attr.addFlashAttribute("msg","Perfil actualizado exitosamente.");
        session.setAttribute("usuario",usuariosRepository.findByIdUsuario(paciente.getIdUsuario()));
        return "redirect:/clinicarenacer/perfil";

    }




}

