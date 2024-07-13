package com.example.proyecto_gtics.controller;

import com.example.proyecto_gtics.dto.ResultDni;
import com.example.proyecto_gtics.entity.*;
import com.example.proyecto_gtics.repository.*;
import com.example.proyecto_gtics.service.DniService;
import com.example.proyecto_gtics.service.MessageService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import java.util.stream.Collectors;

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
    private DniService dniService;

    //Formatear strings a dates
    DateTimeFormatter formatStringToDate = new DateTimeFormatterBuilder().append(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toFormatter();
    DateTimeFormatter formatDateToSring = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    @GetMapping(value ={"/farmacista"})
    public String paginaPrincipal(Model model, HttpSession session){

        Usuarios farmacista = (Usuarios) session.getAttribute("usuario"); // Farmacista logueado
        Usuarios superAdmin = (Usuarios) session.getAttribute("originalUser");//Superadmin logueado
        //Verificamos que el superadmin no pueda acceder a un farmacista sin una sesion
        if(Objects.equals(farmacista.getTipoUsuario().getIdTipoUsuario(), "SuperAdmin")){
            return "redirect:/superadmin";
        }
        //-------------------------------------------------------------------------------------
        if(superAdmin != null){
            model.addAttribute("superAdmin",superAdmin);
        }
        model.addAttribute("farmacista",farmacista);

        Sedes sede = farmacista.getSedes();
        TipoUsuario doctor = tipoUsuarioRepository.findById("Doctor").get();
        EstadoUsuario activo = estadoUsuarioRepository.findById("Activo").get();

        //List<ProductosSedes> listaProductos = productosSedeRepository.findBySedes(sede);
        List<ProductosSedes> listaProductos = productosSedeRepository.findBySedesAndVisibilidad(sede,1);
        model.addAttribute("listaProductos",listaProductos);
        model.addAttribute("listaDoctores",usuariosRepository.findByTipoUsuarioAndSedesAndEstadoUsuario(doctor,sede,activo));
        return "Farmacista/index";
    }

    @PostMapping(value = "/farmacista/guardarOrden")
    public String guardarOrden(@RequestParam("listaIdsProductos") List<Integer> listaIdsProductos, @RequestParam("listaCantidades") List<String> listaCantidades,
                               /*@RequestParam("checkbox") List<String> listCheckbox,*/ @Valid Usuarios paciente, BindingResult bindingResult,
                               @RequestParam("fechaEntregaStr") String fechaEntregaStr, @RequestParam("idDoctor") Integer idDoctor,
                               RedirectAttributes attr, HttpSession session){

        Usuarios farmacista = (Usuarios) session.getAttribute("usuario"); // Farmacista logueado
        //Verificamos que el superadmin no pueda acceder a un farmacista sin una sesion
        if(Objects.equals(farmacista.getTipoUsuario().getIdTipoUsuario(), "SuperAdmin")){
            return "redirect:/superadmin";
        }
        //-------------------------------------------------------------------------------------

        if (bindingResult.hasErrors()) {
            String errors = bindingResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            attr.addFlashAttribute("err", errors);

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

        ResultDni resultDni = dniService.obtenerDatosPorDni(paciente.getDni().toString());
        if (resultDni == null || resultDni.getStatus() != 200 || resultDni.getData() == null) {
            attr.addFlashAttribute("err","DNI inválido");
            return "redirect:/farmacista";
        }

        if (!usuarioYaRegistrado(paciente.getDni(),1,true)){ //Caso crear un paciente / el paciente no está registrado aún en el sistema

            paciente.setNombre(resultDni.getData().getNombres() + " " + resultDni.getData().getApellido_paterno() + " " + resultDni.getData().getApellido_materno());


            paciente.setSedes(farmacista.getSedes()); // asignamos la sede actual
            paciente.setTipoUsuario(tipoUsuarioRepository.findById("Paciente").get());
            LocalDate fechaActual = LocalDateTime.now(ZoneId.of("America/Lima")).toLocalDate(); //sacamos la fecha actual
            paciente.setFechaRegistro(Date.from(fechaActual.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            paciente.setContrasena("Temporal_password");
            paciente.setCorreo(paciente.getDni() + "@renacer.pe");
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
        //Creacion del codigo de orden de reposicion:
        ordenCreada.setCodigo(crearCodigo(ordenCreada.getIdordenes()));
        ordenesRepository.save(ordenCreada);

        int index = 0;
        float monto = 0;
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
                    detallesOrdenRepository.save(detallesOrden); // Guardrooms los productos y detalles de orden
                    monto += cantidad*p.getPrecio();
                }

            }
            catch (NumberFormatException n){
                attr.addFlashAttribute("err","Las cantidades deben ser números.");
                return "redirect:/farmacista";
            }


            index++;
        }
        ordenCreada.setTipoOrden(tipoOrdenRepository.findById(1).get()); // Finalmente cambiamos el tipo de orden a orden presencial
        ordenCreada.setMonto(monto);
        ordenesRepository.save(ordenCreada);

        attr.addFlashAttribute("msg","Orden Registrada exitosamente");
        attr.addFlashAttribute("wrn","Solo se añadieron las medicinas en las cuales se ingresó un número entero positivo en la cantidad solicitada.");
        return "redirect:/farmacista";

    }

    @GetMapping(value ={"/farmacista/ordenes-linea"})
    public String ordenesLinea(Model model, HttpSession session){

        Usuarios farmacista = (Usuarios) session.getAttribute("usuario"); // Farmacista logueado
        Usuarios superAdmin = (Usuarios) session.getAttribute("originalUser");//Superadmin logueado
        //Verificamos que el superadmin no pueda acceder a un farmacista sin una sesion
        if(Objects.equals(farmacista.getTipoUsuario().getIdTipoUsuario(), "SuperAdmin")){
            return "redirect:/superadmin";
        }
        //-------------------------------------------------------------------------------------
        if(superAdmin != null){
            model.addAttribute("superAdmin",superAdmin);
        }
        model.addAttribute("farmacista",farmacista);

        Optional<TipoOrden> tipoOrden1 = tipoOrdenRepository.findById(3);
        Optional<TipoOrden> tipoOrden2 = tipoOrdenRepository.findById(4);
        Optional<EstadoOrden> estadoOrden = estadoOrdenRepository.findById(1);
        List<Ordenes> listaOrdenes = ordenesRepository.findByEstadoOrdenAndTipoOrdenOrTipoOrden(estadoOrden,tipoOrden1,tipoOrden2);
        model.addAttribute("listaOrdenes",listaOrdenes);
        return "Farmacista/OrdenesLinea";
    }

    @GetMapping(value ={"/farmacista/ordenes-venta"})
    public String ordenesVenta(Model model, HttpSession session){
        Usuarios farmacista = (Usuarios) session.getAttribute("usuario"); // Farmacista logueado
        Usuarios superAdmin = (Usuarios) session.getAttribute("originalUser");//Superadmin logueado
        //Verificamos que el superadmin no pueda acceder a un farmacista sin una sesion
        if(Objects.equals(farmacista.getTipoUsuario().getIdTipoUsuario(), "SuperAdmin")){
            return "redirect:/superadmin";
        }
        //-------------------------------------------------------------------------------------
        if(superAdmin != null){
            model.addAttribute("superAdmin",superAdmin);
        }
        model.addAttribute("farmacista",farmacista);
        //List<Ordenes> listaOrdenes = ordenesRepository.encuentraOrdenesPorEstadosOrdenes(4,10,3,4,1);
        List<Ordenes> listaOrdenes = ordenesRepository.encuentraOrdenesPorEstadosOrdenes(4,10,3,4,1,farmacista.getSedes().getIdSedes());
        model.addAttribute("listaOrdenes",listaOrdenes);
        return "Farmacista/OrdenesVenta";
    }

    @PostMapping(value ={"/farmacista/ordenes-linea/ver-orden"})
    public String verOrden(Model model, @RequestParam("idOrden") Integer idOrden, HttpSession session){
        Usuarios farmacista = (Usuarios) session.getAttribute("usuario"); // Farmacista logueado
        //Verificamos que el superadmin no pueda acceder a un farmacista sin una sesion
        if(Objects.equals(farmacista.getTipoUsuario().getIdTipoUsuario(), "SuperAdmin")){
            return "redirect:/superadmin";
        }
        //-------------------------------------------------------------------------------------
        model.addAttribute("farmacista",farmacista);

        Ordenes orden = ordenesRepository.findByIdordenes(idOrden);
        List<DetallesOrden> listaDetallesOrden = detallesOrdenRepository.findByOrdenes(orden);
        model.addAttribute("posible",comprobarStock(listaDetallesOrden,farmacista.getSedes()));
        model.addAttribute("listaDetallesOrden",listaDetallesOrden);
        model.addAttribute("orden",orden);
        return "Farmacista/verOrdenLinea";
    }

    @PostMapping(value ={"/farmacista/ordenes-venta/ver-orden"})
    public String verOrdenVenta(Model model, @RequestParam("idOrden") Integer idOrden, HttpSession session){
        Usuarios farmacista = (Usuarios) session.getAttribute("usuario"); // Farmacista logueado
        //Verificamos que el superadmin no pueda acceder a un farmacista sin una sesion
        if(Objects.equals(farmacista.getTipoUsuario().getIdTipoUsuario(), "SuperAdmin")){
            return "redirect:/superadmin";
        }
        //-------------------------------------------------------------------------------------
        model.addAttribute("farmacista",farmacista);

        Ordenes orden = ordenesRepository.findByIdordenes(idOrden);
        List<DetallesOrden> listaDetallesOrden = detallesOrdenRepository.findByOrdenes(orden);
        model.addAttribute("listaDetallesOrden",listaDetallesOrden);
        model.addAttribute("orden",orden);
        return "Farmacista/verOrdenVenta";
    }

    @PostMapping(value ={"/farmacista/ordenes-linea/aprobar"})
    public String aprobarOrdenDeLinea(@RequestParam("idOrden") Integer idOrden, RedirectAttributes attr, HttpSession session){

        Usuarios farmacista = (Usuarios) session.getAttribute("usuario"); // Farmacista logueado
        //Verificamos que el superadmin no pueda acceder a un farmacista sin una sesion
        if(Objects.equals(farmacista.getTipoUsuario().getIdTipoUsuario(), "SuperAdmin")){
            return "redirect:/superadmin";
        }
        //-------------------------------------------------------------------------------------


        Optional<Ordenes> opt = ordenesRepository.findById(idOrden);
        if(opt.isPresent()){
            Ordenes ordenes = opt.get();
            ordenes.setEstadoOrden(estadoOrdenRepository.findById(4).get());
            ordenes.setSedes(farmacista.getSedes());
            ordenesRepository.save(ordenes);

            List<DetallesOrden> listaCantProductosPorOrden = detallesOrdenRepository.findByOrdenes(ordenes);
            listaCantProductosPorOrden.forEach(item -> {
                ProductosSedes productosSedes = productosSedeRepository.findByProductosAndSedes(item.getProductos(), farmacista.getSedes());
                Integer resultado = productosSedes.getCantidad()- item.getCantidad();
                System.out.println("EL RESULTADO ES :" + resultado);
                productosSedes.setCantidad(resultado);
                productosSedeRepository.save(productosSedes);
            });
            return "redirect:/farmacista/ordenes-linea";
        }
        else {
            return "redirect:/farmacista/ordenes-linea";
        }


    }


    @GetMapping(value ={"/farmacista/contactarPaciente"})
    public String contactarPaciente(HttpSession session, Model model, @RequestParam(name = "idPaciente", required = false) Integer idPaciente){
        Usuarios farmacista = (Usuarios) session.getAttribute("usuario"); // Farmacista logueado
        Usuarios superAdmin = (Usuarios) session.getAttribute("originalUser");//Superadmin logueado
        //Verificamos que el superadmin no pueda acceder a un farmacista sin una sesion
        if(Objects.equals(farmacista.getTipoUsuario().getIdTipoUsuario(), "SuperAdmin")){
            return "redirect:/superadmin";
        }
        //-------------------------------------------------------------------------------------
        if(superAdmin != null){
            model.addAttribute("superAdmin",superAdmin);
        }
        model.addAttribute("farmacista",farmacista);
        Usuarios paciente = usuariosRepository.findByIdUsuario(idPaciente);

        //Comprobamos si el chat ya existe
        Optional<Chat> optional1 = chatRepository.findByUsuario1AndAndUsuario2(farmacista,paciente);
        Optional<Chat> optional2 = chatRepository.findByUsuario1AndAndUsuario2(paciente,farmacista);

        if(optional1.isPresent()){
            return "redirect:/farmacista/chat?chatId="+optional1.get().getIdChat();
        }
        if(optional2.isPresent()){
            return "redirect:/farmacista/chat?chatId="+optional2.get().getIdChat();
        }
        //En el caso de que no exista el chat, creamos este

        //Tipo de chat = 1 : Farmacista - Paciente
        TipoChat tipoChat = tipoChatRepository.findById(1).get();
        //Crear el chat
        Chat chat = new Chat();
        chat.setTipoChat(tipoChat);
        chat.setUsuario1(farmacista); // Farmacista
        chat.setUsuario2(paciente); // paciente
        chatRepository.save(chat);

        //Recuperar el chat creado:
        Chat chatRecuperado = chatRepository.findFirstByOrderByIdChatDesc();

        return "redirect:/farmacista/chat?chatId="+chatRecuperado.getIdChat();
    }

    @GetMapping(value ={"/farmacista/chat"})
    public String chat(HttpSession session, Model model, @RequestParam(name = "chatId", required = false) Integer chatId,
                       RedirectAttributes attr){
        Usuarios farmacista = (Usuarios) session.getAttribute("usuario"); // Farmacista logueado
        Usuarios superAdmin = (Usuarios) session.getAttribute("originalUser");//Superadmin logueado
        //Verificamos que el superadmin no pueda acceder a un farmacista sin una sesion
        if(Objects.equals(farmacista.getTipoUsuario().getIdTipoUsuario(), "SuperAdmin")){
            return "redirect:/superadmin";
        }
        //-------------------------------------------------------------------------------------
        if(superAdmin != null){
            model.addAttribute("superAdmin",superAdmin);
        }
        model.addAttribute("farmacista",farmacista);

        if(chatId == null){
            Optional<Chat> opt = chatRepository.findFirstByUsuario1OrderByIdChatDesc(farmacista);
            if(opt.isPresent()){
                Chat chat = opt.get();
                if(messageService.verificarAccesoChat(chat.getIdChat(),farmacista)){
                    List<Chat> listaChatsFarmacista = chatRepository.findByUsuario1OrderByIdChatDesc(farmacista);
                    List<Mensajes> listaUltimosMensajes = new ArrayList<>();

                    for(Chat ch: listaChatsFarmacista){
                        Mensajes ultimoMensaje = mensajesRepository.findFirstByChatAndSenderOrderByIdMensajesDesc(ch,2);
                        listaUltimosMensajes.add(ultimoMensaje);
                    }

                    model.addAttribute("listaUltimosMensajes",listaUltimosMensajes);
                    model.addAttribute("listaChatsFarmacista",listaChatsFarmacista);
                    model.addAttribute("chat",chat);
                    return "Farmacista/Chat";
                }
                else {
                    attr.addFlashAttribute("err","No tienes acceso a ese chat.");
                    return "redirect:/farmacista";
                }
            }
            else {
                attr.addFlashAttribute("err","El usuario aún no tiene chats.");
                return "redirect:/farmacista";
            }

        }

        if(messageService.verificarAccesoChat(chatId,farmacista)){
            Chat chat = chatRepository.findById(chatId).get();
            List<Chat> listaChatsFarmacista = chatRepository.findByUsuario1OrderByIdChatDesc(farmacista);
            List<Mensajes> listaUltimosMensajes = new ArrayList<>();

            for(Chat ch: listaChatsFarmacista){
                Mensajes ultimoMensaje = mensajesRepository.findFirstByChatAndSenderOrderByIdMensajesDesc(ch,2);
                listaUltimosMensajes.add(ultimoMensaje);
            }

            model.addAttribute("listaUltimosMensajes",listaUltimosMensajes);
            model.addAttribute("listaChatsFarmacista",listaChatsFarmacista);
            model.addAttribute("chat",chat);
            return "Farmacista/Chat";
        }
        else {
            attr.addFlashAttribute("err","No tienes acceso a ese chat.");
            return "redirect:/farmacista";
        }

    }


    @GetMapping(value ={"/farmacista/perfil"})
    public String perfil(Model model, HttpSession session){
        Usuarios farmacista = (Usuarios) session.getAttribute("usuario"); // Farmacista logueado
        Usuarios superAdmin = (Usuarios) session.getAttribute("originalUser");//Superadmin logueado
        //Verificamos que el superadmin no pueda acceder a farmacista sin una sesion
        if(Objects.equals(farmacista.getTipoUsuario().getIdTipoUsuario(), "SuperAdmin")){
            return "redirect:/superadmin";
        }
        //-------------------------------------------------------------------------------------
        if(superAdmin != null){
            model.addAttribute("superAdmin",superAdmin);
        }
        model.addAttribute("farmacista",farmacista);

        return "Farmacista/perfil";
    }

    @GetMapping(value ={"/farmacista/editar-perfil"})
    public String editarPerfil(Model model, HttpSession session){
        Usuarios farmacista = (Usuarios) session.getAttribute("usuario"); // Farmacista logueado
        Usuarios superAdmin = (Usuarios) session.getAttribute("originalUser");//Superadmin logueado
        //Verificamos que el superadmin no pueda acceder a administrador de sede sin una sesion
        if(Objects.equals(farmacista.getTipoUsuario().getIdTipoUsuario(), "SuperAdmin")){
            return "redirect:/superadmin";
        }
        //-------------------------------------------------------------------------------------
        if(superAdmin != null){
            model.addAttribute("superAdmin",superAdmin);
        }
        model.addAttribute("farmacista",farmacista);

        return "Farmacista/editarPerfil";
    }

    @PostMapping(value = "/farmacista/guardar_perfil")
    public String actualizarPerfil(@RequestParam(name = "direccion", required = false) String direccion,
                                   @RequestParam(name = "distrito", required = false) String distrito,
                                   @RequestParam(name = "correo",required = false) String correo,
                                   @RequestParam("image") MultipartFile file,
                                   RedirectAttributes attr, HttpSession session){

        Usuarios farmacista = (Usuarios) session.getAttribute("usuario"); // Farmacista logueado

        if(direccion == null || distrito == null || correo == null){
            attr.addFlashAttribute("msg","Debe rellenar los campos.");
            return "redirect:/farmacista/editar-perfil";
        }
        if(direccion.isEmpty() || distrito.isEmpty() || correo.isEmpty()){
            attr.addFlashAttribute("msg","Debe rellenar los campos.");
            return "redirect:/farmacista/editar-perfil";
        }


        //Sobre la foto de un producto --------------------------------------------------------------------------------
        if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            if (fileName.contains("..")) {
                attr.addFlashAttribute("err","No se permiten '..' en el archivo");
                return "redirect:/farmacista/editar-perfil";
            }
            try {
                farmacista.setFoto(file.getBytes());
                farmacista.setFotonombre(fileName);
                farmacista.setFotocontenttype(file.getContentType());

            } catch (IOException e) {
                e.printStackTrace();
                attr.addFlashAttribute("err","No se permiten '..' en el archivo");
                return "redirect:/farmacista/editar-perfil";
            }
        }
        //-----------------------------------------------------------------------------------------------------------


        farmacista.setCorreo(correo);
        farmacista.setDireccion(direccion);
        farmacista.setDistritoResidencia(distrito);
        usuariosRepository.save(farmacista);
        attr.addFlashAttribute("msg","Perfil actualizado exitosamente.");
        session.setAttribute("usuario",usuariosRepository.findByIdUsuario(farmacista.getIdUsuario()));
        return "redirect:/farmacista/perfil";

    }

    @GetMapping(value ={"/farmacista/cambiar-contra"})
    public String cambiarContra(Model model, HttpSession session){
        Usuarios farmacista = (Usuarios) session.getAttribute("usuario"); // Farmacista logueado
        Usuarios superAdmin = (Usuarios) session.getAttribute("originalUser");//Superadmin logueado
        //Verificamos que el superadmin no pueda acceder a administrador de sede sin una sesion
        if(Objects.equals(farmacista.getTipoUsuario().getIdTipoUsuario(), "SuperAdmin")){
            return "redirect:/superadmin";
        }
        //-------------------------------------------------------------------------------------
        if(superAdmin != null){
            model.addAttribute("superAdmin",superAdmin);
        }
        model.addAttribute("farmacista",farmacista);

        return "Farmacista/cambiarContra";
    }

    @PostMapping(value = "/farmacista/actualizar-contra")
    public String actualizarContra(@RequestParam(name = "pass1", required = false) String pass1,
                                   @RequestParam(name = "pass2", required = false) String pass2,
                                   RedirectAttributes attr, HttpSession session){

        Usuarios farmacista = (Usuarios) session.getAttribute("usuario"); // Farmacista logueado


        if(pass1 == null || pass2 == null){
            attr.addFlashAttribute("err","Debe rellenar los campos.");
            return "redirect:/farmacista/cambiar-contra";
        }
        if(pass1.isEmpty() || pass2.isEmpty()){
            attr.addFlashAttribute("err","Debe rellenar los campos.");
            return "redirect:/farmacista/cambiar-contra";
        }


        if(pass1.equalsIgnoreCase(pass2)){
            farmacista.setContrasena(passwordEncoder.encode(pass1));
            usuariosRepository.save(farmacista);
            attr.addFlashAttribute("msg","Contraseña actualizada exitosamente.");
            return "redirect:/farmacista/cambiar-contra";
        }
        else {
            attr.addFlashAttribute("wrn","Las contraseñas no coinciden.");
            return "redirect:/farmacista/cambiar-contra";
        }

    }


    public void crearOrden(Usuarios usuario, Integer tipoOrden, Integer estadoOrden, Integer idDoctor) {
        Ordenes orden = new Ordenes();
        orden.setEstadoOrden(estadoOrdenRepository.findById(estadoOrden).get()); //Estado se configura en aceptado (no importa mucho en una orden carrito)
        orden.setTipoOrden(tipoOrdenRepository.findById(tipoOrden).get()); // Tipo de orden
        orden.setUsuarios(usuario);
        orden.setTipoCobro(tipoCobroRepository.findById(1).get()); // Asignamos un tipo de cobro
        orden.setSedes(sedesRepository.findById(usuario.getSedes().getIdSedes()).get());
        orden.setDoctor(usuariosRepository.findByIdUsuario(idDoctor));

        orden.setCodigo(UUID.randomUUID().toString());
        LocalDate fechaActual = LocalDateTime.now(ZoneId.of("America/Lima")).toLocalDate(); //sacamos la fecha actual
        orden.setFechaRegistro(fechaActual.format(formatDateToSring));
        LocalDate fechaEntrega = fechaActual.plusDays(20);
        orden.setFechaEntrega(fechaEntrega.format(formatDateToSring));

        ordenesRepository.save(orden); // creamos la

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

    public int comprobarStock(List<DetallesOrden> listaDetallesOrden,Sedes sede){
        int posible=1;
        for (DetallesOrden item : listaDetallesOrden){
            ProductosSedes productosSedes = productosSedeRepository.findByProductosAndSedes(item.getProductos(),sede);
            if(item.getCantidad()>productosSedes.getCantidad()){
                posible=0;
            }
        }
        return posible;
    }

    public String crearCodigo(Integer idOrden){
        return "ORD-PRESEC-"+idOrden.toString();
    }


}
