package com.example.proyecto_gtics.config;


import com.example.proyecto_gtics.entity.Usuarios;
import com.example.proyecto_gtics.repository.UsuariosRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class CustomPasswordChangeFilter extends OncePerRequestFilter {

    final UsuariosRepository usuariosRepository;

    public CustomPasswordChangeFilter(UsuariosRepository usuariosRepository) {
        this.usuariosRepository = usuariosRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String uri = request.getRequestURI();

        // Excluir static resources
        if (uri.startsWith("/css/") || uri.startsWith("/js/") || uri.startsWith("/images/")) {
            filterChain.doFilter(request, response);
            return;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            Optional<Usuarios> usuarioOpt = usuariosRepository.findByCorreo(username);

            if(usuarioOpt.isPresent()){
                Usuarios usuario = usuarioOpt.get();
                if (usuario.getUsandoContrasenaTemporal() && !request.getRequestURI().contains("/cambiar-contrasena")){
                    response.sendRedirect("/cambiar-contrasena?token=" + usuario.getToken());
                }

            }

        }

        filterChain.doFilter(request, response);
    }


}
