package com.example.proyecto_gtics.controller;

import com.example.proyecto_gtics.entity.Ordenes;
import com.example.proyecto_gtics.entity.Productos;
import com.example.proyecto_gtics.entity.Usuarios;
import com.example.proyecto_gtics.repository.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Controller
public class ImageController {

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

    public ImageController(CategoriasRepository categoriasRepository, DetallesOrdenRepository detallesOrdenRepository,
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


    @GetMapping("/imageProduct/{id}")
    public ResponseEntity<byte[]> mostrarImagenProducto(@PathVariable("id") Integer id) {
        Optional<Productos> opt = productosRepository.findById(id);
        if (opt.isPresent()) {
            Productos p = opt.get();

            byte[] imagenComoBytes = p.getFoto();

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(
                    MediaType.parseMediaType(p.getFotocontenttype()));

            return new ResponseEntity<>(
                    imagenComoBytes,
                    httpHeaders,
                    HttpStatus.OK);
        } else {
            return null;
        }
    }

    @GetMapping("/imageUser/{id}")
    public ResponseEntity<byte[]> mostrarImagenUsuario(@PathVariable("id") Integer id) throws IOException {
        Optional<Usuarios> opt = usuariosRepository.findById(id);
        if (opt.isPresent()) {
            Usuarios u = opt.get();

            if(u.getFotonombre() == null){
                // Si el usuario no existe, retornar la imagen predeterminada
                Resource imgFile = new ClassPathResource("/static/img/Superadmin/user_icon.png");
                InputStream in = imgFile.getInputStream();
                byte[] imagenComoBytes = StreamUtils.copyToByteArray(in);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_PNG);

                System.out.println("-------------------------------------------------------------------------------");
                return new ResponseEntity<>(imagenComoBytes, headers, HttpStatus.OK);
            }

            byte[] imagenComoBytes = u.getFoto();

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(
                    MediaType.parseMediaType(u.getFotocontenttype()));

            return new ResponseEntity<>(
                    imagenComoBytes,
                    httpHeaders,
                    HttpStatus.OK);
        } else {
            return null;
        }
    }

    @GetMapping("/imageReceta/{id}")
    public ResponseEntity<byte[]> mostrarImagenRecetaMedica(@PathVariable("id") Integer id) {
        Optional<Ordenes> opt = ordenesRepository.findById(id);
        if (opt.isPresent()) {
            Ordenes o = opt.get();

            byte[] imagenComoBytes = o.getFotoReceta();

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(
                    MediaType.parseMediaType(o.getFotocontenttype()));

            return new ResponseEntity<>(
                    imagenComoBytes,
                    httpHeaders,
                    HttpStatus.OK);
        } else {
            return null;
        }
    }




}
