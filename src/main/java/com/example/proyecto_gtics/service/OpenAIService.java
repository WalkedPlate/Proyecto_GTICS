package com.example.proyecto_gtics.service;

import com.example.proyecto_gtics.entity.DetallesOrden;
import com.example.proyecto_gtics.entity.Ordenes;
import com.example.proyecto_gtics.entity.Productos;
import com.example.proyecto_gtics.entity.Usuarios;
import com.example.proyecto_gtics.repository.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class OpenAIService {

    @Autowired
    ProductosRepository productosRepository;
    @Autowired
    DetallesOrdenRepository detallesOrdenRepository;
    @Autowired
    OrdenesRepository ordenesRepository;
    @Autowired
    TipoOrdenRepository tipoOrdenRepository;
    @Autowired
    EstadoOrdenRepository estadoOrdenRepository;
    @Autowired
    UsuariosRepository usuariosRepository;
    @Autowired
    TipoUsuarioRepository tipoUsuarioRepository;
    @Autowired
    EstadoUsuarioRepository estadoUsuarioRepository;
    @Autowired
    TipoCobroRepository tipoCobroRepository;
    @Autowired
    SedesRepository sedesRepository;

    //Formatear strings a dates
    DateTimeFormatter formatStringToDate = new DateTimeFormatterBuilder().append(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toFormatter();
    DateTimeFormatter formatDateToSring = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final String apiKey = "";

    private static final String OPENAI_API_URL  = "https://api.openai.com/v1/chat/completions";
    private List<JSONObject> messageHistory;
    private String prompt = "Eres un chatbot para un servicio de venta de medicamentos de una farmacia web (Clinica Renacer). A ti te hablarán solo clientes (pacientes). Tu puedes generar órdenes de venta de medicamentos y también puedes realizar breves diagnósticos médicos para recomendar medicamentos al paciente.\n" +
            "A continuación, te doy el flujo inicial con el que interactuarás con el paciente/cliente.\n" +
            "\n" +
            "Flujo inicial (en cada petición o pregunta espera que el paciente ingrese el dato)::\n" +
            "- Primero pregúntale si quiere que le recomiendes medicamentos según síntomas que tenga el paciente o si desea empezar con el flujo de generar una orden.\n" +
            "- Si el paciente te indica que quiere generar una orden/pedido, comienzas con el flujo de generar orden.\n" +
            "- Si el paciente te indica que quiere las recomendaciones, asegúrate de que los medicamentos que recomiendes estén en la base de datos  y exista una cantidad razonable (mayor a 20 unidades) para poder ofrecérselos al paciente. Recuerda que estas solo son recomendaciones (no preguntes si desea añadirlas a su pedido o algo similar). Por otro lado, en cada interacción  debes preguntarle al paciente si ya está listo para generar una orden o pedido. Cuando el paciente esté listo, empezarás el flujo del registro de su pedido. \n" +
            "\n" +
            "Flujo generar orden o pedido (en cada petición o pregunta espera que el paciente ingrese el dato):\n" +
            "- Primero, pídele que ingrese su nombre y sus apellidos.\n" +
            "- Pídele que ingrese el nombre de su seguro médico (en caso lo tuviera)\n" +
            "- Pídele que ingrese su dirección\n" +
            "- Pídele que ingrese su distrito (Valida que sea un distrito de Lima/Perú)\n" +
            "- Pídele que ingrese su número de teléfono \n" +
            "- Pídele que ingrese su DNI (verifica que sea un número de 8 dígitos)\n" +
            "- Ahora, infórmale que ingrese los medicamentos y las cantidades de cada uno de estos. Guarda esta información y muéstrale al paciente los medicamentos y cantidades que ha ingresado hasta el momento. No olvides de que en cada interacción, debes preguntar si ya terminó de ingresar los medicamentos que desea. Además, si el paciente pide una cantidad de unidades de un medicamento que sobrepasa las existencias en la base de datos (en este caso asume que el paciente no puede pedir más de 20 unidades de un mismo producto), puedes recomendarle otro medicamento con componentes similares (este medicamento también debe estar en base de datos).\n" +
            "\n" +
            "-  Una vez el paciente te diga que ya ingresó sus medicamentos y cantidades respectivas, le pides que ingrese una hora de entrega tentativa.\n" +
            "- Luego, pídele que ingrese la foto de una receta médica.\n" +
            "- Finalmente, muéstrale al paciente un resumen de su pedido (solo muestra los medicamentos y cantidades) y pregúntale si requiere cambios o está conforme para generar la orden. Pídele al paciente que confirme su orden. Cuando el paciente te confirme la generación de la orden, debes  confirmarle al paciente el recibimiento de su orden y mostrarle la orden con el formato final que te indico después. El formato con el que presentarás este resumen final es (Debes seguir estrictamente este formato y solo debes mostrarlo UNA vez en todo el chat):\n" +
            "\n" +
            "\"\n" +
            "Entiendo, ahora te mostraré el resumen de tu pedido:\n" +
            "------------------------------------------\n" +
            "Nombre y Apellidos: (El nombre y apellidos ingresados)\n" +
            "Seguro médico: (nombre del seguro o 'false' si no tiene seguro médico)\n" +
            "Dirección: (Dirección)\n" +
            "Distrito: (Distrito Ingresado)\n" +
            "Número de teléfono: (número de teléfono ingresado)\n" +
            "DNI: (DNI ingresado)\n" +
            "------------------------------------------\n" +
            "> (Medicamento 1): (cantidad ordenada)\n" +
            "> (Medicamento 2): (cantidad ordenada y así hasta mostrar todos los medicamento ordenados) \n" +
            "\n" +
            "\"\n" +
            "\n" +
            "Te paso una tabla que resume los medicamentos, la cantidad disponible de estos, sus categorías y su precio. Úsalo como guía de la base de datos.\n" +
            "\n" +
            "MEDICINA      | CANTIDAD DISPONIBLE | PRECIO X UNID. (S/.) | CATEGORIA\n" +
            "--------------|---------------------|----------------------|-----------------\n" +
            "PARACETAMOL   | 580                 | 1.00                 | Antiséptico\n" +
            "IBUPROFENO    | 100                 | 7.20                 | Antiinflamatorios\n" +
            "ASPIRINA      | 200                 | 3.80                 | Farmacos\n" +
            "AMOXICILINA   | 300                 | 12.40                | Farmacos\n" +
            "LORATADINA    | 400                 | 6.75                 | Farmacos\n" +
            "OMEPRAZOL     | 250                 | 8.90                 | Farmacos\n" +
            "SIMVASTATINA  | 170                 | 15.30                | Farmacos\n" +
            "METFORMINA    | 180                 | 9.10                 | Farmacos\n" +
            "ATENOLOL      | 170                 | 5.60                 | Farmacos\n" +
            "DIAZEPAM      | 160                 | 11.75                | Farmacos\n" +
            "SERTRALINA    | 140                 | 13.20                | Farmacos\n" +
            "FUROSEMIDA    | 0                   | 7.90                 | Farmacos\n" +
            "DIGOXINA      | 0                   | 14.60                | Farmacos\n" +
            "ALPRAZOLAM    | 0                   | 10.85                | Farmacos\n" +
            "CLOPIDOGREL   | 0                   | 17.40                | Farmacos\n" +
            "TRAZODONA     | 0                   | 12.75                | Farmacos\n" +
            "METRONIDAZOL  | 0                   | 6.30                 | Farmacos\n" +
            "CITALOPRAM    | 0                   | 11.20                | Farmacos\n" +
            "LEVOTIROXINA  | 0                   | 8.15                 | Farmacos\n" +
            "PREDNISONA    | 0                   | 9.80                 | Farmacos\n" +
            "METOCLOPRAMIDA| 0                   | 7.30                 | Farmacos\n" +
            "FENITOINA     | 0                   | 10.50                | Farmacos\n" +
            "CETINICINA    | 1997                | 12.00                | Antiinflamatorios\n" +
            "CLONAZEPAM    | 600                 | 23.00                | Farmacos\n" +
            "MELATONINA    | 0                   | 75.90                | Farmacos\n" +
            "AGUA OXIGENADA| 0                   | 5.49                 | Antiséptico\n" +
            "MAGNESOL      | 0                   | 27.72                | Farmacos\n" +
            "PANADOL       | 0                   | 2.76                 | Farmacos\n" +
            "MENTHOLATUM   | 0                   | 14.57                | Antigripales\n" +
            "TRIMICOT      | 0                   | 10.04                | Farmacos";

    public OpenAIService() {
        this.messageHistory = new ArrayList<>();
        // Inicializar con el prompt
        JSONObject promptJson = new JSONObject();
        promptJson.put("role", "system");
        promptJson.put("content", prompt);
        this.messageHistory.add(promptJson);
    }

    public String getChatResponse(String userMessage) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer " + apiKey);


        // Añadir el mensaje del usuario al historial
        JSONObject messageJson = new JSONObject();
        messageJson.put("role", "user");
        messageJson.put("content", userMessage);
        this.messageHistory.add(messageJson);

        // Crear el objeto JSON para la solicitud
        JSONObject requestJson = new JSONObject();
        requestJson.put("model", "gpt-3.5-turbo");
        requestJson.put("messages", new JSONArray(this.messageHistory));


        HttpEntity<String> request = new HttpEntity<>(requestJson.toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(OPENAI_API_URL, HttpMethod.POST, request, String.class);

        JSONObject responseJson = new JSONObject(response.getBody());
        String responseMessage = responseJson.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");

        // Añadir la respuesta del chatbot al historial
        JSONObject responseMessageJson = new JSONObject();
        responseMessageJson.put("role", "assistant");
        responseMessageJson.put("content", responseMessage);
        this.messageHistory.add(responseMessageJson);

        // Detectar el resumen del pedido
        if (responseMessage.contains("------------------------------------------")) {
            // Procesar y guardar el pedido
            procesarResumenPedido(responseMessage);
        }


        return responseMessage;
    }

    private void procesarResumenPedido(String respuestaChatbot) {
        // Definir patrones de expresiones regulares
        Pattern nombrePattern = Pattern.compile("Nombre y Apellidos: (.*)");
        Pattern seguroPattern = Pattern.compile("Seguro médico: (.*)");
        Pattern direccionPattern = Pattern.compile("Dirección: (.*)");
        Pattern distritoPattern = Pattern.compile("Distrito: (.*)");
        Pattern telefonoPattern = Pattern.compile("Número de teléfono: (.*)");
        Pattern dniPattern = Pattern.compile("DNI: (.*)");

        Pattern medicamentoPattern = Pattern.compile("> (.*): (\\d+) unidades");

        // Inicializar variables para almacenar datos del pedido
        String nombre = null;
        String seguroMedico = null;
        String direccion = null;
        String distrito = null;
        String telefono = null;
        String dni = null;
        List<DetallesOrden> detallesOrdenList = new ArrayList<>();
        LocalDateTime horaEntrega = null;

        // Buscar coincidencias en la respuesta del chatbot
        Matcher matcher;

        matcher = nombrePattern.matcher(respuestaChatbot);
        if (matcher.find()) {
            nombre = matcher.group(1).trim();
        }

        matcher = seguroPattern.matcher(respuestaChatbot);
        if (matcher.find()) {
            seguroMedico = matcher.group(1).trim();
        }

        matcher = direccionPattern.matcher(respuestaChatbot);
        if (matcher.find()) {
            direccion = matcher.group(1).trim();
        }

        matcher = distritoPattern.matcher(respuestaChatbot);
        if (matcher.find()) {
            distrito = matcher.group(1).trim();
        }

        matcher = telefonoPattern.matcher(respuestaChatbot);
        if (matcher.find()) {
            telefono = matcher.group(1).trim();
        }

        matcher = dniPattern.matcher(respuestaChatbot);
        if (matcher.find()) {
            dni = matcher.group(1).trim();
        }

        // Buscar medicamentos solicitados
        matcher = medicamentoPattern.matcher(respuestaChatbot);
        while (matcher.find()) {
            String nombreMedicamento = matcher.group(1).trim();
            int cantidad = Integer.parseInt(matcher.group(2).trim());

            // Crear objeto Medicamento y añadirlo a la lista
            DetallesOrden detallesOrden = new DetallesOrden();
            Productos medicamento = productosRepository.findByNombre(nombreMedicamento);
            detallesOrden.setCantidad(cantidad);
            detallesOrden.setProductos(medicamento);
            detallesOrden.setMontoParcial(cantidad*medicamento.getPrecio());
            detallesOrdenList.add(detallesOrden);

        }

        // Procesar hora tentativa de entrega (opcional)
        // Aquí podrías implementar la lógica para extraer y convertir la hora en formato LocalDateTime

        //Crear al paciente:
        Usuarios paciente = new Usuarios();

        if (!usuarioYaRegistrado(Integer.parseInt(dni),1,true)){ //Caso crear un paciente / el paciente no está registrado aún en el sistema
            paciente.setTipoUsuario(tipoUsuarioRepository.findById("Paciente").get());
            paciente.setSedes(sedesRepository.findById(4).get());
            LocalDate fechaActual = LocalDateTime.now(ZoneId.of("America/Lima")).toLocalDate(); //sacamos la fecha actual
            paciente.setFechaRegistro(Date.from(fechaActual.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            paciente.setContrasena("Temporal_password");
            paciente.setCorreo(UUID.randomUUID().toString());
            paciente.setEstadoUsuario(estadoUsuarioRepository.findById("Activo").get());
            usuariosRepository.save(paciente);
            paciente = usuariosRepository.findFirstByOrderByIdUsuarioDesc();

        }
        else {
            paciente = usuariosRepository.findByDni(Integer.parseInt(dni)).get();
        }


        crearOrdenChatBot(paciente,4,4,1039); // creamos la orden (tipo chatbot / estado Aceptado)
        Ordenes ordenChatBot = ordenesRepository.findFirstByOrderByIdordenesDesc(); //Recuperamos la orden que acabamos de crear
        float monto = 0;
        for(DetallesOrden detallesOrden: detallesOrdenList){
            detallesOrden.setOrdenes(ordenChatBot);
            detallesOrdenRepository.save(detallesOrden);
            monto += detallesOrden.getMontoParcial();
        }
        ordenChatBot.setMonto(monto);
        ordenChatBot.setDireccion(direccion);
        ordenesRepository.save(ordenChatBot);

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


    public void crearOrdenChatBot(Usuarios usuario, Integer tipoOrden, Integer estadoOrden, Integer idDoctor) {
        Ordenes orden = new Ordenes();
        orden.setEstadoOrden(estadoOrdenRepository.findById(estadoOrden).get()); //Estado se configura en aceptado (no importa mucho en una orden carrito)
        orden.setTipoOrden(tipoOrdenRepository.findById(tipoOrden).get()); // Tipo de orden
        orden.setUsuarios(usuario);
        orden.setTipoCobro(tipoCobroRepository.findById(1).get()); // Asignamos un tipo de cobro
        orden.setSedes(sedesRepository.findById(4).get());
        orden.setDoctor(usuariosRepository.findByIdUsuario(idDoctor));

        orden.setCodigo(UUID.randomUUID().toString());
        LocalDate fechaActual = LocalDateTime.now(ZoneId.of("America/Lima")).toLocalDate(); //sacamos la fecha actual
        orden.setFechaRegistro(fechaActual.format(formatDateToSring));
        LocalDate fechaEntrega = fechaActual.plusDays(20);
        orden.setFechaEntrega(fechaEntrega.format(formatDateToSring));

        ordenesRepository.save(orden); // creamos la

    }

}
