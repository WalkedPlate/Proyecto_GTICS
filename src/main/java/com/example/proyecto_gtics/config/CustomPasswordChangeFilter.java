package com.example.proyecto_gtics.config;



import com.example.proyecto_gtics.entity.Usuarios;
import com.example.proyecto_gtics.repository.UsuariosRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.cglib.core.Local;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Objects;
import java.util.Optional;

@Component
public class CustomPasswordChangeFilter extends OncePerRequestFilter {
    //Formatear strings a dates
    DateTimeFormatter formatStringToDate = new DateTimeFormatterBuilder().append(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toFormatter();
    DateTimeFormatter formatDateToSring = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    final UsuariosRepository usuariosRepository;

    public CustomPasswordChangeFilter(UsuariosRepository usuariosRepository) {
        this.usuariosRepository = usuariosRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String uri = request.getRequestURI();

        // Excluir static resources
        if (uri.startsWith("/css/") || uri.startsWith("/js/") || uri.startsWith("/images/") || uri.startsWith("/static/") || uri.startsWith("/webjars/")) {
            filterChain.doFilter(request, response);
            return;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            Optional<Usuarios> usuarioOpt = usuariosRepository.findByCorreo(username);

            if(usuarioOpt.isPresent()){
                Usuarios usuario = usuarioOpt.get();
                String path = request.getRequestURI();

                if(usuario.getUsandoContrasenaTemporal()){
                    if(path.startsWith("/clinicarenacer/paciente") || path.startsWith("/clinicarenacer")){
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado");
                        return;
                    }
                    if(path.startsWith("/superadmin") ){
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado");
                        return;
                    }
                    if(path.startsWith("/administradorsede") ){
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado");
                        return;
                    }
                    if(path.startsWith("/farmacista") ){
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado");
                        return;
                    }

                }
                if(Objects.equals(usuario.getEstadoUsuario().getIdEstadoUsuario(), "Baneado")){
                    LocalDate fechaDesbaneo = LocalDate.parse(usuario.getFechaDesbaneo(),formatStringToDate);
                    LocalDate fechaActual = LocalDateTime.now(ZoneId.of("America/Lima")).toLocalDate(); //sacamos la fecha actual

                    if(fechaDesbaneo.isBefore(fechaActual)){
                        usuario.getEstadoUsuario().setIdEstadoUsuario("Activo");
                        usuariosRepository.save(usuario);
                        return;
                    }else{
                        HttpSession session = request.getSession(false);
                        if (session != null) {
                            session.invalidate();
                        }
                        // Eliminar las cookies de sesión
                        Cookie[] cookies = request.getCookies();
                        if (cookies != null) {
                            for (Cookie cookie : cookies) {
                                if (cookie.getName().equals("JSESSIONID")) {
                                    cookie.setMaxAge(0);
                                    cookie.setPath("/");
                                    response.addCookie(cookie);
                                    break;
                                }
                            }
                        }
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Acceso denegado");
                        return;
                    }
                }


            }

        }

        filterChain.doFilter(request, response);
    }




}


