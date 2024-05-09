package com.example.proyecto_gtics.controller;


import com.example.proyecto_gtics.entity.*;
import com.example.proyecto_gtics.repository.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String guardarAdminSede(Usuarios adminSede,@RequestParam("idSedes") int id,@RequestParam("idUsuario") int idAdminSede){
        Optional<Usuarios> adminsede = usuariosRepository.findById(idAdminSede);
        if(adminsede.isPresent()){
            adminSede.setEstadoUsuario(estadoUsuarioRepository.findById("activo").get());
            adminSede.setContrasena(usuariosRepository.findByIdUsuario(idAdminSede).getContrasena());
            Sedes sedes = sedesRepository.findById(id).get();
            adminSede.setSedes(sedes);
            adminSede.setTipoUsuario(tipoUsuarioRepository.findById("AdministradorDeSede").get());
        }
        else {
            adminSede.setEstadoUsuario(estadoUsuarioRepository.findById("En revisión").get());
            adminSede.setContrasena("Temporal_password");
            Sedes sedes = sedesRepository.findById(id).get();
            adminSede.setSedes(sedes);
            adminSede.setTipoUsuario(tipoUsuarioRepository.findById("AdministradorDeSede").get());
        }

        usuariosRepository.save(adminSede);
        return "redirect:/superadmin/administradores-sede";
    }

    @PostMapping("/superadmin/eliminarAdminSede")
    public String eliminarAdminSede(@RequestParam("idAdminSede") Integer id){
        Optional<Usuarios> optSede =usuariosRepository.findById(id);
        Usuarios adminSede = usuariosRepository.findByIdUsuario(id);
        if(optSede.isPresent()){
            adminSede.setEstadoUsuario(estadoUsuarioRepository.findById("Eliminado").get());
            usuariosRepository.save(adminSede);
            //usuariosRepository.deleteById(id);
        }
        return "redirect:/superadmin/administradores-sede";
    }

    @PostMapping(value = {"/superadmin/banearAdminSede"})
    public String banearAdminSede(@RequestParam("diasBan") int diasBan,@RequestParam("idAdminSede") int idAdminSede){
        Date fechaActual = new Date();
        Usuarios adminSede = usuariosRepository.findByIdUsuario(idAdminSede);
        Optional<Usuarios> optSede =usuariosRepository.findById(idAdminSede);
        if(optSede.isPresent()){
            adminSede.setEstadoUsuario(estadoUsuarioRepository.findById("Baneado").get());
            adminSede.setDiasBan(diasBan);
            adminSede.setFechaBan(fechaActual);
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
        return "Superadmin/inventario";
    }

    @PostMapping(value = {"/superadmin/guardarProducto"})
    public String guardarProducto(Productos productos,@RequestParam("idCategoria") int idCategoria,@RequestParam("IDProducto") int idProducto){
        Optional<Productos> optProducto =productosRepository.findById(idProducto);
        if(optProducto.isPresent()){
            Categorias categoria = categoriasRepository.findById(idCategoria).get();
            productos.setCategorias(categoria);
            productos.setEstadoProducto("Activo");
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
            productosRepository.save(productos);
        }
        return "redirect:/superadmin/inventario";
    }

    @PostMapping(value = {"/superadmin/eliminarProducto"})
    public String eliminarProducto(@RequestParam("idProducto") int idProducto){
        Optional<Productos> optProduc =productosRepository.findById(idProducto);
        Productos producto = productosRepository.findById(idProducto).get();
        if(optProduc.isPresent()){
            producto.setEstadoProducto("Eliminado");
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
        //Modificar el html soliReposicion para que se muestren los medicamentos por cada orden de reposicion
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
    public String guardarFarmacistas(Usuarios farmacista,@RequestParam("idSedes") int idSede){
        Optional<Usuarios> farma = usuariosRepository.findById(farmacista.getIdUsuario());
        if(farma.isPresent()){
            farmacista.setEstadoUsuario(estadoUsuarioRepository.findById("Activo").get());
            farmacista.setContrasena(usuariosRepository.findByIdUsuario(farmacista.getIdUsuario()).getContrasena());
            farmacista.setTipoUsuario(tipoUsuarioRepository.findById("Farmacista").get());
            Sedes sedes = sedesRepository.findById(idSede).get();
            farmacista.setSedes(sedes);
            usuariosRepository.save(farmacista);
        }
        return "redirect:/superadmin/farmacistas";
    }

    @PostMapping(value = {"/superadmin/eliminarfarmacistas"})
    public String eliminarFarmacistas(@RequestParam("idFarmacista") Integer id){
        Optional<Usuarios> optSede =usuariosRepository.findById(id);
        Usuarios farmacista = usuariosRepository.findByIdUsuario(id);
        if(optSede.isPresent()){
            farmacista.setEstadoUsuario(estadoUsuarioRepository.findById("Eliminado").get());
            usuariosRepository.save(farmacista);
        }
        return "redirect:/superadmin/farmacistas";
    }

    @PostMapping(value = {"/superadmin/aceptar-rechazar-farmacista"})
    public String aceptarRechazarFarmacista(@RequestParam("idFarmacista") int idFarmacista,@RequestParam("valor") int valor){

        Usuarios farmacista = usuariosRepository.findByIdUsuario(idFarmacista);
        if(valor == 1){
            farmacista.setEstadoUsuario(estadoUsuarioRepository.findById("Activo").get());
            usuariosRepository.save(farmacista);
        } else if (valor == 2) {
            farmacista.setEstadoUsuario(estadoUsuarioRepository.findById("Denegado").get());
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
    public String guardarDoctor(Usuarios doctor,@RequestParam("idSede") int idSede){
        doctor.setEstadoUsuario(estadoUsuarioRepository.findById("Activo").get());
        doctor.setContrasena("Temporal_password");
        doctor.setTipoUsuario(tipoUsuarioRepository.findById("Doctor").get());

        Sedes sede = sedesRepository.findById(idSede).get();//Buscamos la sede
        doctor.setSedes(sede);//Asignamos la sede
        usuariosRepository.save(doctor);
        return "redirect:/superadmin/doctores";
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
