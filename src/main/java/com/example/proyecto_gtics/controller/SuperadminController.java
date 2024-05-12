package com.example.proyecto_gtics.controller;


import com.example.proyecto_gtics.dto.CantidadTotalPorProducto;
import com.example.proyecto_gtics.entity.*;
import com.example.proyecto_gtics.repository.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.w3c.dom.Attr;

import java.io.IOException;
import java.util.*;

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


    @GetMapping(value ={"/superadmin","/superadmin/administradores-sede"})
    public String dashboard(Model model){
        TipoUsuario adminSede = tipoUsuarioRepository.findById("AdministradorDeSede").get();
        EstadoUsuario estado = estadoUsuarioRepository.findById("Activo").get();
        EstadoUsuario estado2 = estadoUsuarioRepository.findById("Baneado").get();
        List<Usuarios> listaAdminSede = usuariosRepository.findByTipoUsuarioAndEstadoUsuarioOrEstadoUsuario(adminSede,estado,estado2);
        //List<Usuarios> listaAdminSede = usuariosRepository.findByTipoUsuarioAndEstadoUsuario(adminSede, estado);
        model.addAttribute("listaAdminSede", listaAdminSede);

        List<Sedes> listaSedes = sedesRepository.findAll();
        model.addAttribute("listaSedes",listaSedes);
        return "Superadmin/index";
    }

    @PostMapping(value = {"/superadmin/guardarAdminSede"})
    public String guardarAdminSede(@Valid Usuarios adminSede, BindingResult bindingResult, @RequestParam("idSedes") int id,@RequestParam("idUsuario") int idAdminSede, RedirectAttributes attr ){
        Optional<Usuarios> adminsede = usuariosRepository.findById(idAdminSede);
        if(bindingResult.hasErrors()){
            String error = bindingResult.getFieldError().getDefaultMessage().toString();
            attr.addFlashAttribute("err",error);
            return "redirect:/superadmin";
        }else {


        if(adminsede.isPresent()){
            adminSede.setEstadoUsuario(estadoUsuarioRepository.findById("Activo").get());// implementar:agarrar el estado de la base de datos
            adminSede.setContrasena(usuariosRepository.findByIdUsuario(idAdminSede).getContrasena());
            Sedes sedes = sedesRepository.findById(id).get();
            adminSede.setSedes(sedes);
            adminSede.setTipoUsuario(tipoUsuarioRepository.findById("AdministradorDeSede").get());
            attr.addFlashAttribute("msg","Datos del administrador de sede actualizados exitosamente");
        }
        else {
            adminSede.setEstadoUsuario(estadoUsuarioRepository.findById("Activo").get());
            adminSede.setContrasena("Temporal_password");
            Sedes sedes = sedesRepository.findById(id).get();
            adminSede.setSedes(sedes);
            adminSede.setTipoUsuario(tipoUsuarioRepository.findById("AdministradorDeSede").get());
            attr.addFlashAttribute("msg","Administrador de sede agregado exitosamente");
        }

            usuariosRepository.save(adminSede);
        return "redirect:/superadmin/administradores-sede";
    }
    }

    @PostMapping("/superadmin/eliminarAdminSede")
    public String eliminarAdminSede(@RequestParam("idAdminSede") Integer id, RedirectAttributes redirectAttributes) {
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
    public String banearAdminSede( @RequestParam("diasBan") int diasBan,@RequestParam("idAdminSede") int idAdminSede,RedirectAttributes attr){
        System.out.println(diasBan);
        Date fechaActual = new Date();
        Usuarios adminSede = usuariosRepository.findByIdUsuario(idAdminSede);
        Optional<Usuarios> optSede =usuariosRepository.findById(idAdminSede);


        if(optSede.isPresent()){
            adminSede.setEstadoUsuario(estadoUsuarioRepository.findById("Baneado").get());
            adminSede.setDiasBan(diasBan);
            adminSede.setFechaBan(fechaActual);
            attr.addFlashAttribute("ban","Administrador de sede baneado exitosamente");
            usuariosRepository.save(adminSede);
        }
        return "redirect:/superadmin/administradores-sede";
    }


    @GetMapping(value ={"/superadmin/inventario"})
    public String inventario(Model model){

        List<Productos> listaMedicamentos = productosRepository.findByEstadoProducto("Activo");
        //List<Productos> listaMedicamentos = productosRepository.findAll();
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
    public String guardarProducto(Productos productos, @RequestParam("idCategoria") int idCategoria, @RequestParam("IDProducto") int idProducto,
                                  @RequestParam("archivo") MultipartFile file, RedirectAttributes attr, @RequestParam("idSedes") List<Integer> idSedes){


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

        Optional<Productos> optProducto =productosRepository.findById(idProducto);

        if(optProducto.isPresent()){
            Categorias categoria = categoriasRepository.findById(idCategoria).get();
            productos.setCategorias(categoria);
            productos.setEstadoProducto("Activo");

            for (Integer idSede : idSedes){
                Sedes sede = sedesRepository.findByIdSedes(idSede);
                ProductosSedes productosSedes = productosSedeRepository.findByProductosAndSedes(productos,sede);
                //ProductosSedes productosSedes = new ProductosSedes();// Me genera duda
                //ProductosSedesId productosSedesId = new ProductosSedesId();
                //productosSedesId.setIdProductos(productos.getIdProductos());
                //productosSedesId.setIdSedes(idSede);
                //productosSedes.setId(productosSedesId);
                //productosSedes.setProductos(productos);
                if(productosSedes == null){
                    ProductosSedes productosSedes1 = new ProductosSedes();
                    ProductosSedesId productosSedesId = new ProductosSedesId();
                    productosSedesId.setIdProductos(productos.getIdProductos());
                    productosSedesId.setIdSedes(idSede);
                    productosSedes1.setId(productosSedesId);
                    productosSedes1.setProductos(productos);
                    productosSedes1.setSedes(sede);
                    productosSedes1.setSedes(sede);
                    productosSedes1.setCantidad(200);
                    productosSedeRepository.save(productosSedes1);
                }else{
                    productosSedeRepository.save(productosSedes);
                }
                //productosSedes.setSedes(sede);

            }
            attr.addFlashAttribute("msg","Producto actualizado exitosamente.");
            productosRepository.save(productos);
        }else{
            //-----------Procesamiento de creacion de codigo de producto--------
            Random random = new Random();
            String letras = productos.getNombre().substring(0,3).toUpperCase();
            int numero = random.nextInt(900)+100;
            String codigo = letras + numero;
            //------------------------------------------------------------------

            productos.setCodigo(codigo);
            Categorias categoria = categoriasRepository.findById(idCategoria).get();
            productos.setCategorias(categoria);
            productos.setEstadoProducto("Activo");
            attr.addFlashAttribute("msg","Producto creado exitosamente.");
            productosRepository.save(productos);

            Productos productoConsultar = productosRepository.findByCodigo(codigo);
            for (Integer idSede : idSedes){
                Sedes sede = sedesRepository.findByIdSedes(idSede);
                ProductosSedes productosSedes = new ProductosSedes();
                ProductosSedesId productosSedesId = new ProductosSedesId();
                productosSedesId.setIdProductos(productoConsultar.getIdProductos());
                productosSedesId.setIdSedes(idSede);
                productosSedes.setId(productosSedesId);
                productosSedes.setSedes(sede);
                productosSedes.setProductos(productoConsultar);
                productosSedes.setCantidad(200);
                productosSedeRepository.save(productosSedes);
            }
        }
        return "redirect:/superadmin/inventario";
    }

    @PostMapping(value = {"/superadmin/eliminarProducto"})
    public String eliminarProducto(@RequestParam("idProducto") int idProducto, RedirectAttributes attr){
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
    public String estadoReposiciones(){
        return "Superadmin/estadoReposicion";
    }

    @GetMapping(value ={"/superadmin/inventario/restricciones"})
    public String restricciones(){
        return "Superadmin/restricciones";
    }

    @GetMapping(value ={"/superadmin/orden-reposicion"})
    public String ordenReposicion(Model model){
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
    public String solicitudesReposicion(Model model){
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
    public String cambiarEstadoOrden(@RequestParam("accion") int accion,@RequestParam("idOrden") int idOrden){
        Ordenes orden = ordenesRepository.findByIdordenes(idOrden);
        orden.setEstadoOrden(estadoOrdenRepository.findByIdEstadoOrden(accion));
        ordenesRepository.save(orden);
        return "redirect:/superadmin/solicitudes-reposicion";
    }

    @GetMapping(value ={"/superadmin/farmacistas"})
    public String farmacistas(Model model){
        TipoUsuario farmacista = tipoUsuarioRepository.findById("Farmacista").get();
        EstadoUsuario estado = estadoUsuarioRepository.findById("Activo").get();
        List<Usuarios> listaFarmacistas = usuariosRepository.findByTipoUsuarioAndEstadoUsuario(farmacista, estado);
        model.addAttribute("listaFarmacistas", listaFarmacistas);

        List<Sedes> listaSedes = sedesRepository.findAll();
        model.addAttribute("listaSedes",listaSedes);
        return "Superadmin/farmacistas";
    }

        @GetMapping(value ={"/superadmin/farmacistas/solicitudes"})
        public String soliFarmacistas(Model model){

            TipoUsuario farmacista = tipoUsuarioRepository.findById("Farmacista").get();
            EstadoUsuario estado = estadoUsuarioRepository.findById("En revisión").get();
            List<Usuarios> listaFarmacistas = usuariosRepository.findByTipoUsuarioAndEstadoUsuario(farmacista,estado);
            model.addAttribute("listaFarmacistas",listaFarmacistas);
            return "Superadmin/soliFarmacistas";
        }

    @PostMapping(value = {"/superadmin/guardarfarmacista"})
    public String guardarFarmacistas(@Valid Usuarios farmacista,BindingResult bindingResult ,@RequestParam("idSedes") int idSede, RedirectAttributes attr){
        Optional<Usuarios> farma = usuariosRepository.findById(farmacista.getIdUsuario());

        if(bindingResult.hasErrors()){
            String error = bindingResult.getFieldError().getDefaultMessage().toString();
            attr.addFlashAttribute("err",error);
            return "redirect:/superadmin/farmacistas";
        }else {

        if(farma.isPresent()){
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
    public String eliminarFarmacistas(@RequestParam("idFarmacista") Integer id, RedirectAttributes attr){
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
    public String aceptarRechazarFarmacista(@RequestParam("idFarmacista") int idFarmacista, @RequestParam("valor") int valor, RedirectAttributes attr){

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
    public String doctores(Model model){

        TipoUsuario doctores = tipoUsuarioRepository.findById("Doctor").get();
        EstadoUsuario estado = estadoUsuarioRepository.findById("Activo").get();
        List<Usuarios> listaDoctores = usuariosRepository.findByTipoUsuarioAndEstadoUsuario(doctores,estado);
        model.addAttribute("listaDoctores",listaDoctores);

        List<Sedes> listaSedes = sedesRepository.findAll();
        model.addAttribute("listaSedes",listaSedes);
        return "Superadmin/doctores";

    }
    @PostMapping(value = {"/superadmin/guardarDoctor"})
    public String guardarDoctor(@Valid Usuarios doctor, BindingResult bindingResult, @RequestParam("idSede") @Valid int idSede,BindingResult bindingResultSedes, RedirectAttributes attr){

        if(bindingResult.hasErrors() || idSede<0 ||idSede > 10 ){
           String error = bindingResult.getFieldError().getDefaultMessage().toString();
           attr.addFlashAttribute("err",error);
            return "redirect:/superadmin/doctores";
        }else {


        doctor.setEstadoUsuario(estadoUsuarioRepository.findById("Activo").get());
        doctor.setContrasena("Temporal_password");
        doctor.setTipoUsuario(tipoUsuarioRepository.findById("Doctor").get());

        Sedes sede = sedesRepository.findById(idSede).get();//Buscamos la sede
        doctor.setSedes(sede);//Asignamos la sede
        usuariosRepository.save(doctor);
            attr.addFlashAttribute("msg","Doctor creado exitosamente");

            return "redirect:/superadmin/doctores";
    }
    }

    @GetMapping(value ={"/superadmin/pacientes"})
    public String pacientes(Model model){

        TipoUsuario paciente = tipoUsuarioRepository.findById("Paciente").get();
        EstadoUsuario estado = estadoUsuarioRepository.findById("Activo").get();
        List<Usuarios> listaPaciente = usuariosRepository.findByTipoUsuarioAndEstadoUsuario(paciente,estado);
        model.addAttribute("listaPacientes",listaPaciente);
        return "Superadmin/pacientes";
    }

    @GetMapping(value ={"/superadmin/perfil"})
    public String perfil(){
        return "Superadmin/editarPerfil";
    }

    @GetMapping(value ={"/superadmin/editar-perfil"})
    public String editarPerfil(){
        return "Superadmin/editar";
    }

    @GetMapping(value ={"/superadmin/cambiar-contra"})
    public String cambiarContra(){
        return "Superadmin/cambiarContra";
    }






}
