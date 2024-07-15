package com.example.proyecto_gtics.controller;


import com.example.proyecto_gtics.dto.CantProductPorOrdenClase;
import com.example.proyecto_gtics.dto.CantidadProductosPorOrden;
import com.example.proyecto_gtics.dto.CantidadTotalPorProducto;
import com.example.proyecto_gtics.dto.ResultDni;
import com.example.proyecto_gtics.entity.*;
import com.example.proyecto_gtics.repository.*;
import com.example.proyecto_gtics.service.DniService;
import com.example.proyecto_gtics.service.EmailService;
import com.example.proyecto_gtics.service.TokenService;
import com.example.proyecto_gtics.util.DniUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.w3c.dom.Attr;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private CodigoColegioRespository codigoColegioRespository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private DniService dniService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    EmailService emailService;
    @Autowired
    private DistritosRepository distritosRepository;


    @GetMapping(value ={"/superadmin","/superadmin/administradores-sede"})
    public String dashboard(Model model,HttpSession session){
        Usuarios superadmin = (Usuarios) session.getAttribute("usuario"); // Superadmin Logueado
        model.addAttribute("superadmin",superadmin);


        TipoUsuario adminSede = tipoUsuarioRepository.findById("AdministradorDeSede").get();
        EstadoUsuario estado = estadoUsuarioRepository.findById("Activo").get();
        EstadoUsuario estado2 = estadoUsuarioRepository.findById("Baneado").get();
        List<Usuarios> listaAdminSede = usuariosRepository.findByTipoUsuarioAndEstadoUsuarioOrEstadoUsuario(adminSede,estado,estado2);
        //List<Usuarios> listaAdminSede = usuariosRepository.findByTipoUsuarioAndEstadoUsuario(adminSede, estado);
        model.addAttribute("listaAdminSede", listaAdminSede);

        List<Sedes> listaSedes = sedesRepository.findAll();
        model.addAttribute("listaSedes",listaSedes);

        List<Distritos> listaDistritos = distritosRepository.findAll();
        model.addAttribute("listaDistritos",listaDistritos);
        return "Superadmin/index";
    }

    @PostMapping(value = {"/superadmin/guardarAdminSede"})
    public String guardarAdminSede(@Valid Usuarios adminSede, BindingResult bindingResult, @RequestParam("idSedes") int id,
                                   @RequestParam("idUsuario") int idAdminSede, RedirectAttributes attr,
                                   HttpSession session, HttpServletRequest request){
        Usuarios superadmin = (Usuarios) session.getAttribute("usuario"); // Superadmin Logueado
        // Convertir DNI a String para procesamiento
        String dniStr = DniUtils.formatDni(adminSede.getDni());

        Optional<Usuarios> adminsede = usuariosRepository.findById(idAdminSede);
        if(bindingResult.hasErrors()){

            String errors = bindingResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            attr.addFlashAttribute("err", errors);
            return "redirect:/superadmin";
        }else {


        if(adminsede.isPresent()){
            if(usuarioYaRegistrado(adminSede.getDni(),idAdminSede,false)){
                attr.addFlashAttribute("err","El DNI ya está registrado.");
                return "redirect:/superadmin/administradores-sede";
            }
            if(correoYaRegistrado(adminSede.getCorreo(),idAdminSede,false)){
                attr.addFlashAttribute("err","El correo ya está registrado.");
                return "redirect:/superadmin/administradores-sede";
            }

            adminSede.setEstadoUsuario(estadoUsuarioRepository.findById("Activo").get());// implementar:agarrar el estado de la base de datos
            adminSede.setContrasena(usuariosRepository.findByIdUsuario(idAdminSede).getContrasena());
            Sedes sedes = sedesRepository.findById(id).get();
            adminSede.setSedes(sedes);
            adminSede.setTipoUsuario(tipoUsuarioRepository.findById("AdministradorDeSede").get());
            attr.addFlashAttribute("msg","Datos del administrador de sede actualizados exitosamente");
            usuariosRepository.save(adminSede);
            return "redirect:/superadmin/administradores-sede";
        }
        else {
            if(usuarioYaRegistrado(adminSede.getDni(),adminSede.getIdUsuario(),true)){
                attr.addFlashAttribute("err","El DNI ya está registrado.");
                return "redirect:/superadmin/administradores-sede";
            }
            if(correoYaRegistrado(adminSede.getCorreo(),adminSede.getIdUsuario(),true)){
                attr.addFlashAttribute("err","El correo ya está registrado.");
                return "redirect:/superadmin/administradores-sede";
            }

            //Generación de token
            String token = tokenService.generateToken(adminSede.getCorreo());
            String link = request.getScheme() + "://"+ request.getServerName()
                    + ":"+ request.getServerPort() +request.getContextPath()+ "/cambiar-contrasena?token=" + token;


            ResultDni resultDni = dniService.obtenerDatosPorDni(dniStr);
            if (resultDni == null || resultDni.getStatus() != 200 || resultDni.getData() == null) {
                attr.addFlashAttribute("err","DNI inválido");
                return "redirect:/superadmin/administradores-sede";
            }




            adminSede.setNombre(resultDni.getData().getNombres() + " " + resultDni.getData().getApellido_paterno() + " " + resultDni.getData().getApellido_materno());
            adminSede.setEstadoUsuario(estadoUsuarioRepository.findById("Activo").get());
            //Contraseña
            String temporalPassword = Usuarios.generateTemporaryPassword(10);
            String passwordEncriptada = passwordEncoder.encode(temporalPassword);
            adminSede.setContrasena(passwordEncriptada);

            adminSede.setTipoUsuario(tipoUsuarioRepository.findById("AdministradorDeSede").get());
            adminSede.setUsandoContrasenaTemporal(true);
            adminSede.setToken(token);
            Sedes sedes = sedesRepository.findById(id).get();
            adminSede.setSedes(sedes);
            Path path = Paths.get("src/main/resources/static/img/Superadmin/administrador_icon.png");
            try {
                byte[] defaultPhoto = Files.readAllBytes(path);
                adminSede.setFoto(defaultPhoto);
                adminSede.setFotonombre("administrador_icon.png");
                adminSede.setFotocontenttype(MediaType.IMAGE_PNG_VALUE);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            usuariosRepository.save(adminSede);
            attr.addFlashAttribute("msg","Administrador de sede agregado exitosamente. Las credenciales temporales se enviarán" +
                    " al correo ingresado.");

            //Envío de correo con contraseña temporal
            String to = adminSede.getCorreo();
            String subject = "Cambie su contraseña";
            String pathToImage = "static/img/Login/icono.png";
            String imageId = "image001";
            emailService.sendEmail(to, subject, link, temporalPassword,pathToImage,imageId);
            return "redirect:/superadmin/administradores-sede";

        }

    }
    }

    @PostMapping("/superadmin/eliminarAdminSede")
    public String eliminarAdminSede(@RequestParam("idAdminSede") Integer id, RedirectAttributes redirectAttributes,
                                    HttpSession session) {
        Usuarios superadmin = (Usuarios) session.getAttribute("usuario"); // Superadmin Logueado

        if(id == null){
            redirectAttributes.addFlashAttribute("del", "Le recomendamos no modificar los parámetros a enviar.");
            return "redirect:/superadmin/administradores-sede";
        }

        Optional<Usuarios> optSede = usuariosRepository.findById(id);
        Usuarios adminSede = usuariosRepository.findByIdUsuario(id);
        if (optSede.isPresent()) {
            adminSede.setEstadoUsuario(estadoUsuarioRepository.findById("Eliminado").get());
            usuariosRepository.save(adminSede);
            //usuariosRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("del", "Administrador de sede eliminado exitosamente");
        }
        return "redirect:/superadmin/administradores-sede";
    }

    @PostMapping(value = {"/superadmin/banearAdminSede"})
    public String banearAdminSede( @RequestParam("diasBan") int diasBan,@RequestParam("idAdminSede") int idAdminSede,RedirectAttributes attr,
                                   HttpSession session){
        Usuarios superadmin = (Usuarios) session.getAttribute("usuario"); // Superadmin Logueado


        Date fechaActual = new Date();
        Usuarios adminSede = usuariosRepository.findByIdUsuario(idAdminSede);
        Optional<Usuarios> optSede =usuariosRepository.findById(idAdminSede);


        if(optSede.isPresent()){
            adminSede.setEstadoUsuario(estadoUsuarioRepository.findById("Baneado").get());
            adminSede.setDiasBan(diasBan);
            adminSede.setFechaBan(fechaActual);
            usuariosRepository.calculAyActualizarFechaDesbaneo(adminSede.getIdUsuario());
            attr.addFlashAttribute("ban","Administrador de sede baneado exitosamente");
            usuariosRepository.save(adminSede);
        }
        return "redirect:/superadmin/administradores-sede";
    }


    @GetMapping(value ={"/superadmin/inventario"})
    public String inventario(Model model,HttpSession session){
        Usuarios superadmin = (Usuarios) session.getAttribute("usuario"); // Superadmin Logueado
        model.addAttribute("superadmin",superadmin);


        List<Productos> listaMedicamentos = productosRepository.findByEstadoProducto("Activo");
        model.addAttribute("listaMedicamentos",listaMedicamentos);

        List<Categorias> listaCategorias = categoriasRepository.findAll();
        model.addAttribute("listaCategorias",listaCategorias);

        List<Sedes> listaSedes = sedesRepository.findAll();
        model.addAttribute("listaSedes",listaSedes);

        model.addAttribute("listaCantidadPorProducto",productosSedeRepository.obtenerCantidadTotalPorProducto());

        model.addAttribute("listaProductoSede",productosSedeRepository.findAll());
        return "Superadmin/inventario";
    }

    @PostMapping(value = {"/superadmin/guardarProducto"})
        public String guardarProducto(@Valid Productos productos, BindingResult bindingResult, @RequestParam("idCategoria") Integer idCategoria, @RequestParam("IDProducto") Integer idProducto,
                                  @RequestParam("archivo") MultipartFile file, RedirectAttributes attr, @RequestParam("idSedes") List<Integer> idSedes,
                                  HttpSession session){
        Usuarios superadmin = (Usuarios) session.getAttribute("usuario"); // Superadmin Logueado


        Optional<Productos> optProducto =productosRepository.findById(idProducto);

        if(bindingResult.hasErrors()){
            String errors = bindingResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            attr.addFlashAttribute("err", errors);
            return "redirect:/superadmin/inventario";
        }else {

        if(optProducto.isPresent()){

            //Sobre la foto de un producto --------------------------------------------------------------------------------
            if (!file.isEmpty()) {
                System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                String fileName = file.getOriginalFilename();
                if (fileName.contains("..")) {
                    attr.addFlashAttribute("err","No se permiten '..' en el archivo");
                    return "redirect:/superadmin/inventario";
                }
                try {
                    productos.setFoto(file.getBytes());
                    productos.setFotonombre(fileName);
                    productos.setFotocontenttype(file.getContentType());

                } catch (IOException e) {
                    e.printStackTrace();
                    attr.addFlashAttribute("err","No se permiten '..' en el archivo");
                    return "redirect:/superadmin/inventario";
                }
            }
            else {
                System.out.println("---------------------------------------------------------------------------------------------------");
                productos.setFoto(optProducto.get().getFoto());
                productos.setFotonombre(optProducto.get().getFotonombre());
                productos.setFotocontenttype(optProducto.get().getFotocontenttype());
            }

            //-----------------------------------------------------------------------------------------------------------


            Categorias categoria = categoriasRepository.findById(idCategoria).get();
            productos.setCategorias(categoria);
            productos.setEstadoProducto("Activo");

            //--------------------Cambios en el for por probar-----------------------------------------
            List<Sedes> sedesTotales = sedesRepository.findAll();
            List<Sedes> sedesVista =new ArrayList<>();//Sedes mandadas desde la vista

            ProductosSedes productosSedes1 = new ProductosSedes();
            for (Integer idSede : idSedes){
            //for (Sedes sede : sedesTotales){
                Sedes sede = sedesRepository.findByIdSedes(idSede);
                //----------------Codigo añadido-------------------------------------------
                sedesVista.add(sede);// Llenamos la lista
                //--------------------------------------------------------------------------
                ProductosSedes productosSedes = productosSedeRepository.findByProductosAndSedes(productos,sede);
                if(productosSedes == null){
                    //ProductosSedes productosSedes1 = new ProductosSedes();
                    ProductosSedesId productosSedesId = new ProductosSedesId();
                    productosSedesId.setIdProductos(productos.getIdProductos());
                    productosSedesId.setIdSedes(sede.getIdSedes());
                    productosSedes1.setId(productosSedesId);
                    productosSedes1.setProductos(productos);
                    productosSedes1.setSedes(sede);
                    productosSedes1.setCantidad(0);
                    productosSedes1.setVisibilidad(1);
                    productosSedeRepository.save(productosSedes1);
                }else{
                    for (Sedes sede1 : sedesTotales){
                        if(Objects.equals(idSede, sede1.getIdSedes())){
                            productosSedes.setVisibilidad(1);
                        }
                    }
                    productosSedeRepository.save(productosSedes);
                }
            }
            sedesTotales.removeAll(sedesVista);// *
            for (Sedes sede : sedesTotales){
                ProductosSedes productosSedes = productosSedeRepository.findByProductosAndSedes(productos,sede);
                if (productosSedes != null){
                    //productosSedeRepository.delete(productosSedes);
                    productosSedes.setVisibilidad(0);
                    productosSedeRepository.save(productosSedes);
                }
            }
            //------------------Aqui finaliza los cambios de prueba del for-----------------------

            attr.addFlashAttribute("msg","Producto actualizado exitosamente.");
            productosRepository.save(productos);
            return "redirect:/superadmin/inventario";
        }else{

            //Sobre la foto de un producto --------------------------------------------------------------------------------
            if (file.isEmpty()) {
                attr.addFlashAttribute("err","Debe subir una foto.");
                return "redirect:/superadmin/inventario";
            }
            String fileName = file.getOriginalFilename();
            if (fileName.contains("..")) {
                attr.addFlashAttribute("err","No se permiten '..' en el archivo");
                return "redirect:/superadmin/inventario";
            }
            try {
                productos.setFoto(file.getBytes());
                productos.setFotonombre(fileName);
                productos.setFotocontenttype(file.getContentType());

            } catch (IOException e) {
                e.printStackTrace();
                attr.addFlashAttribute("err","No se permiten '..' en el archivo");
                return "redirect:/superadmin/inventario";
            }
            //-----------------------------------------------------------------------------------------------------------


            Productos pruebaProducto = productosRepository.findByNombre(productos.getNombre());
            if (pruebaProducto == null){
                //-----------Procesamiento de creacion de codigo de producto--------
                Random random = new Random();
                String letras = productos.getNombre().substring(0,3).toUpperCase();
                int numero = random.nextInt(900)+100;
                String codigo = letras + numero;
                //------------------------------------------------------------------

                productos.setCodigo(codigo); //Seteamos el codigo del producto
                Categorias categoria = categoriasRepository.findById(idCategoria).get();//Obtenemos la categoria del producto
                productos.setCategorias(categoria);//Seteamos la categoria
                productos.setEstadoProducto("Activo");//Seteamos el producto a estado Activo
                productosRepository.save(productos);//Guardamos el producto en la BD

                Productos productoConsultar = productosRepository.findByCodigo(codigo);
                List<Sedes> listaSedes = sedesRepository.findAll();
                //for (Integer idSede : idSedes){
                for (Sedes sede : listaSedes){
                    //Sedes sede = sedesRepository.findByIdSedes(idSede);
                    ProductosSedes productosSedes = new ProductosSedes();
                    ProductosSedesId productosSedesId = new ProductosSedesId();
                    productosSedesId.setIdProductos(productoConsultar.getIdProductos());
                    productosSedesId.setIdSedes(sede.getIdSedes());
                    productosSedes.setId(productosSedesId);
                    productosSedes.setSedes(sede);
                    productosSedes.setProductos(productoConsultar);
                    productosSedes.setCantidad(0);
                    productosSedes.setVisibilidad(0);// Valor por defecto para visibilidad
                    for (Integer idSede : idSedes){
                        if(Objects.equals(idSede, sede.getIdSedes())){
                            productosSedes.setVisibilidad(1);
                        }
                    }
                    productosSedeRepository.save(productosSedes);
                }
                attr.addFlashAttribute("msg","Producto creado exitosamente.");
                return "redirect:/superadmin/inventario";
            }else {
                attr.addFlashAttribute("err","Producto Repetido");
                return "redirect:/superadmin/inventario";
            }

        }

    }
    }

    @PostMapping(value = {"/superadmin/eliminarProducto"})
    public String eliminarProducto(@RequestParam("idProducto") int idProducto, RedirectAttributes attr, HttpSession session){
        Usuarios superadmin = (Usuarios) session.getAttribute("usuario"); // Superadmin Logueado

        Optional<Productos> optProduc =productosRepository.findById(idProducto);
        Productos producto = productosRepository.findById(idProducto).get();
        if(optProduc.isPresent()){
            producto.setEstadoProducto("Eliminado");
            attr.addFlashAttribute("del","Producto eliminado exitosamente.");
            productosRepository.save(producto);
        }
        return "redirect:/superadmin/inventario";
    }

    @GetMapping(value ={"/superadmin/inventario/estado-reposicion"})
    public String estadoReposiciones(Model model,HttpSession session){
        Usuarios superadmin = (Usuarios) session.getAttribute("usuario"); // Superadmin Logueado
        model.addAttribute("superadmin",superadmin);

        return "Superadmin/estadoReposicion";
    }

    @GetMapping(value ={"/superadmin/inventario/restricciones"})
    public String restricciones(Model model,HttpSession session){
        Usuarios superadmin = (Usuarios) session.getAttribute("usuario"); // Superadmin Logueado
        model.addAttribute("superadmin",superadmin);

        return "Superadmin/restricciones";
    }

    @GetMapping(value ={"/superadmin/orden-reposicion"})
    public String ordenReposicion(Model model,HttpSession session){
        Usuarios superadmin = (Usuarios) session.getAttribute("usuario"); // Superadmin Logueado
        model.addAttribute("superadmin",superadmin);


        //Optional<TipoOrden> tipoOrden = tipoOrdenRepository.findById(2);
        List<Ordenes> listaOrdenes = ordenesRepository.encuentraOrdenesPorEstadoOrden(4,10,2);
        model.addAttribute("listaOrdenes",listaOrdenes);

        List<List<DetallesOrden>> listaDetallesOrden = new ArrayList<>();

        for(Ordenes ordenes : listaOrdenes ){
            List<DetallesOrden> lista = detallesOrdenRepository.findByOrdenes(ordenes);
            listaDetallesOrden.add(lista);
        }
        model.addAttribute("listaDetallesDoble",listaDetallesOrden);

        return "Superadmin/ordenReposicion";
    }


    @GetMapping(value ={"/superadmin/solicitudes-reposicion"})
    public String solicitudesReposicion(Model model,HttpSession session){
        Usuarios superadmin = (Usuarios) session.getAttribute("usuario"); // Superadmin Logueado
        model.addAttribute("superadmin",superadmin);


        Optional<TipoOrden> tipoOrden = tipoOrdenRepository.findById(2);
        Optional<EstadoOrden> estadoOrden = estadoOrdenRepository.findById(1);
        List<Ordenes> listaOrdenes = ordenesRepository.findByTipoOrdenAndEstadoOrden(tipoOrden,estadoOrden);

        //Añadiendo detalles de ordenes de reposicion en estado pendiente
        List<List<DetallesOrden>> listaDetallesOrden = new ArrayList<>();
        for(Ordenes ordenes : listaOrdenes ){
            List<DetallesOrden> lista = detallesOrdenRepository.findByOrdenes(ordenes);
            listaDetallesOrden.add(lista);
        }
        model.addAttribute("listaOrdenes",listaOrdenes);
        model.addAttribute("listaDetallesDoble",listaDetallesOrden);
        return "Superadmin/soliReposicion";
    }

    @PostMapping(value = {"/superadmin/cambiarEstadoOrden"})
    public String cambiarEstadoOrden(@RequestParam("accion") int accion,@RequestParam("idOrden") int idOrden,
                                     HttpSession session){
        Usuarios superadmin = (Usuarios) session.getAttribute("usuario"); // Superadmin Logueado

        Ordenes orden = ordenesRepository.findByIdordenes(idOrden);
        orden.setEstadoOrden(estadoOrdenRepository.findByIdEstadoOrden(accion));
        ordenesRepository.save(orden);

        //Verificamos que el estado de la orden sea aceptado -> idEstadoOrden = 4 es ACEPTADO
        if(accion == 4){
            List<DetallesOrden> listaCantProductosPorOrden = detallesOrdenRepository.findByOrdenes(orden);
            listaCantProductosPorOrden.forEach(item -> {
                ProductosSedes productosSedes = productosSedeRepository.findByProductosAndSedes(item.getProductos(), orden.getSedes());
                Integer resultado = productosSedes.getCantidad() + item.getCantidad();//Puede que no exista la relacion producto con sede y me de error verificar que siempre haya una relacion producto sede aunque sea cantidad igual a 0
                System.out.println("EL RESULTADO ES :" + resultado);
                productosSedes.setCantidad(resultado);
                productosSedeRepository.save(productosSedes);
            });
        }

        return "redirect:/superadmin/solicitudes-reposicion";
    }

    @GetMapping(value ={"/superadmin/farmacistas"})
    public String farmacistas(Model model,HttpSession session){
        Usuarios superadmin = (Usuarios) session.getAttribute("usuario"); // Superadmin Logueado
        model.addAttribute("superadmin",superadmin);


        TipoUsuario farmacista = tipoUsuarioRepository.findById("Farmacista").get();
        EstadoUsuario estado = estadoUsuarioRepository.findById("Activo").get();
        List<Usuarios> listaFarmacistas = usuariosRepository.findByTipoUsuarioAndEstadoUsuario(farmacista, estado);
        model.addAttribute("listaFarmacistas", listaFarmacistas);

        List<Sedes> listaSedes = sedesRepository.findAll();
        model.addAttribute("listaSedes",listaSedes);
        List<Distritos> listaDistritos = distritosRepository.findAll();
        model.addAttribute("listaDistritos",listaDistritos);
        return "Superadmin/farmacistas";
    }

    @GetMapping(value ={"/superadmin/farmacistas/solicitudes"})
    public String soliFarmacistas(Model model,HttpSession session){
        Usuarios superadmin = (Usuarios) session.getAttribute("usuario"); // Superadmin Logueado
        model.addAttribute("superadmin",superadmin);


        TipoUsuario farmacista = tipoUsuarioRepository.findById("Farmacista").get();
        EstadoUsuario estado = estadoUsuarioRepository.findById("En revisión").get();
        List<Usuarios> listaFarmacistas = usuariosRepository.findByTipoUsuarioAndEstadoUsuario(farmacista,estado);
        model.addAttribute("listaFarmacistas",listaFarmacistas);
        return "Superadmin/soliFarmacistas";
    }

    @PostMapping(value = {"/superadmin/guardarfarmacista"})
    public String guardarFarmacistas(@Valid Usuarios farmacista,BindingResult bindingResult ,@RequestParam("idSedes") int idSede, RedirectAttributes attr,
                                     HttpSession session){
        Usuarios superadmin = (Usuarios) session.getAttribute("usuario"); // Superadmin Logueado

        Optional<Usuarios> farma = usuariosRepository.findById(farmacista.getIdUsuario());

        if(bindingResult.hasErrors()){
            String errors = bindingResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            attr.addFlashAttribute("err", errors);
            return "redirect:/superadmin/farmacistas";
        }else {

        if(farma.isPresent()){
            if(!validarCodigoColegio(farmacista.getCodigoColegio())){
                attr.addFlashAttribute("err","El código de colegio no es válido.");
                return "redirect:/superadmin/farmacistas";
            }
            if(usuarioYaRegistrado(farmacista.getDni(),farmacista.getIdUsuario(),false)){
                attr.addFlashAttribute("err","El DNI ya está registrado.");
                return "redirect:/superadmin/farmacistas";
            }

            if(correoYaRegistrado(farmacista.getCorreo(),farmacista.getIdUsuario(),false)){
                attr.addFlashAttribute("err","El correo ya está registrado.");
                return "redirect:/superadmin/farmacistas";
            }

            farmacista.setEstadoUsuario(estadoUsuarioRepository.findById("Activo").get());
            farmacista.setContrasena(usuariosRepository.findByIdUsuario(farmacista.getIdUsuario()).getContrasena());
            farmacista.setTipoUsuario(tipoUsuarioRepository.findById("Farmacista").get());
            Sedes sedes = sedesRepository.findById(idSede).get();
            farmacista.setSedes(sedes);
            attr.addFlashAttribute("msg","Farmacista actualizado exitosamente");
            usuariosRepository.save(farmacista);
        }
        return "redirect:/superadmin/farmacistas";
    }
    }

    @PostMapping(value = {"/superadmin/eliminarfarmacistas"})
    public String eliminarFarmacistas(@RequestParam("idFarmacista") Integer id, RedirectAttributes attr, HttpSession session){
        Usuarios superadmin = (Usuarios) session.getAttribute("usuario"); // Superadmin Logueado

        Optional<Usuarios> optSede =usuariosRepository.findById(id);
        Usuarios farmacista = usuariosRepository.findByIdUsuario(id);
        if(optSede.isPresent()){
            farmacista.setEstadoUsuario(estadoUsuarioRepository.findById("Eliminado").get());
            attr.addFlashAttribute("del","Farmacista eliminado exitosamente");
            usuariosRepository.save(farmacista);
        }
        return "redirect:/superadmin/farmacistas";
    }

    @PostMapping(value = {"/superadmin/aceptar-rechazar-farmacista"})
    public String aceptarRechazarFarmacista(@RequestParam("idFarmacista") int idFarmacista, @RequestParam("valor") int valor, RedirectAttributes attr,
                                            HttpSession session){

        Usuarios superadmin = (Usuarios) session.getAttribute("usuario"); // Superadmin Logueado

        Usuarios farmacista = usuariosRepository.findByIdUsuario(idFarmacista);
        if(valor == 1){
            farmacista.setEstadoUsuario(estadoUsuarioRepository.findById("Activo").get());
            attr.addFlashAttribute("msg","Solicitud de registro de farmacista aceptada.");
            usuariosRepository.save(farmacista);
        } else if (valor == 2) {
            farmacista.setEstadoUsuario(estadoUsuarioRepository.findById("Denegado").get());
            attr.addFlashAttribute("err","Solicitud de registro de farmacista denegada.");
            usuariosRepository.save(farmacista);
        }else {
            return "redirect:/superadmin/farmacistas/solicitudes";
        }
        return "redirect:/superadmin/farmacistas/solicitudes";
    }

    @GetMapping(value ={"/superadmin/doctores"})
    public String doctores(Model model,HttpSession session){
        Usuarios superadmin = (Usuarios) session.getAttribute("usuario"); // Superadmin Logueado
        model.addAttribute("superadmin",superadmin);


        TipoUsuario doctores = tipoUsuarioRepository.findById("Doctor").get();
        EstadoUsuario estado = estadoUsuarioRepository.findById("Activo").get();
        List<Usuarios> listaDoctores = usuariosRepository.findByTipoUsuarioAndEstadoUsuario(doctores,estado);
        model.addAttribute("listaDoctores",listaDoctores);

        List<Sedes> listaSedes = sedesRepository.findAll();
        model.addAttribute("listaSedes",listaSedes);

        List<Distritos> listaDistritos = distritosRepository.findAll();
        model.addAttribute("listaDistritos",listaDistritos);

        return "Superadmin/doctores";

    }
    @PostMapping(value = {"/superadmin/guardarDoctor"})
    public String guardarDoctor(@Valid Usuarios doctor, BindingResult bindingResult, @RequestParam("idSede") @Valid int idSede,BindingResult bindingResultSedes,
                                RedirectAttributes attr, HttpSession session){

        Usuarios superadmin = (Usuarios) session.getAttribute("usuario"); // Superadmin Logueado

//         Convertir DNI a String para procesamiento
        String dniStr = DniUtils.formatDni(doctor.getDni());

        if(bindingResult.hasErrors() || idSede<0 ||idSede > 10 ){
            String errors = bindingResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            attr.addFlashAttribute("err", errors);
            return "redirect:/superadmin/doctores";
        }else {

            if(!validarCodigoColegio(doctor.getCodigoColegio())){
                attr.addFlashAttribute("err","El código de colegio no es válido.");
                return "redirect:/superadmin/doctores";
            }

            if(usuarioYaRegistrado(doctor.getDni(),doctor.getIdUsuario(),true)){
                attr.addFlashAttribute("err","El DNI ya está registrado.");
                return "redirect:/superadmin/doctores";
            }

            if(correoYaRegistrado(doctor.getCorreo(), doctor.getIdUsuario(),true)){
                attr.addFlashAttribute("err","El correo ya está registrado.");
                return "redirect:/superadmin/doctores";
            }

            ResultDni resultDni = dniService.obtenerDatosPorDni(dniStr);
            if (resultDni == null || resultDni.getStatus() != 200 || resultDni.getData() == null) {
                attr.addFlashAttribute("errDNI","El DNI ingresado es inválido");
                return "redirect:/registro";
            }
            doctor.setNombre(resultDni.getData().getNombres() + " " + resultDni.getData().getApellido_paterno() + " " + resultDni.getData().getApellido_materno());

            doctor.setEstadoUsuario(estadoUsuarioRepository.findById("Activo").get());
            doctor.setContrasena("Temporal_password");
            doctor.setUsandoContrasenaTemporal(false);
            doctor.setTipoUsuario(tipoUsuarioRepository.findById("Doctor").get());

            Sedes sede = sedesRepository.findById(idSede).get();//Buscamos la sede
            doctor.setSedes(sede);//Asignamos la sede

            Path path = Paths.get("src/main/resources/static/img/Superadmin/doctor_icon.png");
            try {
                byte[] defaultPhoto = Files.readAllBytes(path);
                doctor.setFoto(defaultPhoto);
                doctor.setFotonombre("doctor_icon.png");
                doctor.setFotocontenttype(MediaType.IMAGE_PNG_VALUE);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            usuariosRepository.save(doctor);
                attr.addFlashAttribute("msg","Doctor creado exitosamente");

                return "redirect:/superadmin/doctores";
        }
    }

    @GetMapping(value ={"/superadmin/pacientes"})
    public String pacientes(Model model,HttpSession session){
        Usuarios superadmin = (Usuarios) session.getAttribute("usuario"); // Superadmin Logueado
        model.addAttribute("superadmin",superadmin);


        TipoUsuario paciente = tipoUsuarioRepository.findById("Paciente").get();
        EstadoUsuario estado = estadoUsuarioRepository.findById("Activo").get();
        List<Usuarios> listaPaciente = usuariosRepository.findByTipoUsuarioAndEstadoUsuario(paciente,estado);
        model.addAttribute("listaPacientes",listaPaciente);
        return "Superadmin/pacientes";
    }

    @GetMapping(value ={"/superadmin/perfil"})
    public String perfil(Model model,HttpSession session){
        Usuarios superadmin = (Usuarios) session.getAttribute("usuario"); // Superadmin Logueado
        model.addAttribute("superadmin",superadmin);

        return "Superadmin/editarPerfil";
    }

    @GetMapping(value ={"/superadmin/editar-perfil"})
    public String editarPerfil(Model model,HttpSession session){
        Usuarios superadmin = (Usuarios) session.getAttribute("usuario"); // Superadmin Logueado
        model.addAttribute("superadmin",superadmin);
        List<Distritos> listaDistritos = distritosRepository.findAll();
        model.addAttribute("listaDistritos",listaDistritos);
        return "Superadmin/editar";
    }

    @GetMapping(value ={"/superadmin/cambiar-contra"})
    public String cambiarContra(Model model,HttpSession session){
        Usuarios superadmin = (Usuarios) session.getAttribute("usuario"); // Superadmin Logueado
        model.addAttribute("superadmin",superadmin);

        return "Superadmin/cambiarContra";
    }

    @PostMapping(value = "/superadmin/actualizar-contra")
    public String actualizarContra(@RequestParam(name = "pass1", required = false) String pass1,
                                   @RequestParam(name = "pass2", required = false) String pass2,
                                   RedirectAttributes attr, HttpSession session){

        Usuarios superadmin = (Usuarios) session.getAttribute("usuario"); // Superadmin Logueado


        if(pass1 == null || pass2 == null){
            attr.addFlashAttribute("err","Debe rellenar los campos.");
            return "redirect:/superadmin/cambiar-contra";
        }
        if(pass1.isEmpty() || pass2.isEmpty()){
            attr.addFlashAttribute("err","Debe rellenar los campos.");
            return "redirect:/superadmin/cambiar-contra";
        }


        if(pass1.equalsIgnoreCase(pass2)){
            superadmin.setContrasena(passwordEncoder.encode(pass1));
            usuariosRepository.save(superadmin);
            attr.addFlashAttribute("msg","Contraseña actualizada exitosamente.");
            return "redirect:/superadmin/cambiar-contra";
        }
        else {
            attr.addFlashAttribute("wrn","Las contraseñas no coinciden.");
            return "redirect:/superadmin/cambiar-contra";
        }

    }


    @PostMapping(value = "/superadmin/guardar_perfil")
    public String actualizarPerfil(@RequestParam(name = "direccion", required = false) String direccion,
                                   @RequestParam(name = "distrito", required = false) String distrito,
                                   @RequestParam(name = "correo",required = false) String correo,
                                   @RequestParam("image") MultipartFile file,
                                   RedirectAttributes attr, HttpSession session){

        Usuarios superadmin = (Usuarios) session.getAttribute("usuario"); // Superadmin Logueado

        if(direccion == null || distrito == null || correo == null){
            attr.addFlashAttribute("err","Debe rellenar los campos.");
            return "redirect:/superadmin/editar-perfil";
        }
        if(direccion.isEmpty() || distrito.isEmpty() || correo.isEmpty()){
            attr.addFlashAttribute("err","Debe rellenar los campos.");
            return "redirect:/superadmin/editar-perfil";
        }


        //Sobre la foto de un producto --------------------------------------------------------------------------------
        if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            if (fileName.contains("..")) {
                attr.addFlashAttribute("err","No se permiten '..' en el archivo");
                return "redirect:/superadmin/editar-perfil";
            }
            try {
                superadmin.setFoto(file.getBytes());
                superadmin.setFotonombre(fileName);
                superadmin.setFotocontenttype(file.getContentType());

            } catch (IOException e) {
                e.printStackTrace();
                attr.addFlashAttribute("err","No se permiten '..' en el archivo");
                return "redirect:/superadmin/editar-perfil";
            }
        }
        //-----------------------------------------------------------------------------------------------------------


        superadmin.setCorreo(correo);
        superadmin.setDireccion(direccion);
        superadmin.setDistritoResidencia(distrito);
        usuariosRepository.save(superadmin);
        attr.addFlashAttribute("msg","Perfil actualizado exitosamente.");
        session.setAttribute("usuario",usuariosRepository.findByIdUsuario(superadmin.getIdUsuario()));
        return "redirect:/superadmin/perfil";

    }






    public boolean validarCodigoColegio(String codigo) {

        boolean valido = false;
        Optional<CodigoColegio> opt = codigoColegioRespository.findByCodigo(codigo);
        if(opt.isPresent()){
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

    @PostMapping("/impersonate/{userId}")
    public void impersonateUser(@PathVariable int userId, HttpSession session, HttpServletResponse response) throws IOException {
        // Guardar la identidad original del superadmin
        Usuarios superadmin = (Usuarios) session.getAttribute("usuario");

        // Obtener el usuario a impersonar
        Usuarios userToImpersonate = usuariosRepository.findByIdUsuario(userId);
        if (userToImpersonate == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Usuario no encontrado");
            return;
        }

        // Autenticar como el usuario a impersonar
        Authentication auth = new UsernamePasswordAuthenticationToken(userToImpersonate.getCorreo(), userToImpersonate.getContrasena());
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Guardar la identidad del superadmin en la sesión
        session.setAttribute("originalUser", superadmin);
        session.setAttribute("usuario", userToImpersonate);


        // Redirigir a la página principal del usuario impersonado
        switch (userToImpersonate.getTipoUsuario().getIdTipoUsuario()){
            case "AdministradorDeSede":
                response.sendRedirect("/administradorsede");
                break;
            case "Farmacista":
                response.sendRedirect("/farmacista");
                break;
            case "Paciente":
                response.sendRedirect("/clinicarenacer");
                break;
        }

    }
    @GetMapping("/stopImpersonation")
    public String stopImpersonation(HttpSession session) {
        // Restaurar la identidad original del superadmin
        Usuarios superadmin = (Usuarios) session.getAttribute("originalUser");
        session.setAttribute("usuario", superadmin);
        Authentication auth = new UsernamePasswordAuthenticationToken(superadmin.getCorreo(), superadmin.getContrasena());
        SecurityContextHolder.getContext().setAuthentication(auth);

        return "redirect:/superadmin";
    }
}
