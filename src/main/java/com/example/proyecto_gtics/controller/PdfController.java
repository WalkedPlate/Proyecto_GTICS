package com.example.proyecto_gtics.controller;

import com.example.proyecto_gtics.entity.*;
import com.example.proyecto_gtics.repository.EstadoUsuarioRepository;
import com.example.proyecto_gtics.repository.SedesRepository;
import com.example.proyecto_gtics.repository.TipoUsuarioRepository;
import com.example.proyecto_gtics.repository.UsuariosRepository;
import com.example.proyecto_gtics.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class PdfController {

    @Autowired
    private PdfService pdfService;
    @Autowired
    private UsuariosRepository usuariosRepository;
    @Autowired
    private EstadoUsuarioRepository estadoUsuarioRepository;
    @Autowired
    private SedesRepository sedesRepository;
    @Autowired
    private TipoUsuarioRepository tipoUsuarioRepository;

    @GetMapping("/pdf/usuarios")
    public ResponseEntity<byte[]> descargarPdf() throws IOException {

        EstadoUsuario estadoUsuario = estadoUsuarioRepository.findById("Activo").get();
        Sedes sede = sedesRepository.findByIdSedes(2);
        TipoUsuario tipo = tipoUsuarioRepository.findById("Farmacista").get();

        List<Usuarios> lista = usuariosRepository.findByTipoUsuarioAndSedesAndEstadoUsuario(tipo,sede,estadoUsuario);



        /*

        List<Usuarios> lista = usuariosFiltrados.stream()
                .map(map -> {
                    Usuarios usuario = new Usuarios();
                    EstadoUsuario estadoUsuario = new EstadoUsuario();
                    estadoUsuario.setIdEstadoUsuario(map.get("estadoUsuario."));
                    usuario.setEstadoUsuario(estadoUsuario);
                    usuario.setNombre(map.get("nombre"));

                    return usuario;
                })
                .collect(Collectors.toList());


         */
        ByteArrayInputStream bis = pdfService.generarPdfUsuarios(lista,"Lista de usuarios test");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=productos.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(bis.readAllBytes());
    }
}
