package com.example.proyecto_gtics.config;

import com.example.proyecto_gtics.entity.DetallesOrden;
import com.example.proyecto_gtics.entity.Ordenes;
import com.example.proyecto_gtics.entity.Usuarios;
import com.example.proyecto_gtics.repository.UsuariosRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UsuariosRepository usuariosRepository;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.Authentication authentication) throws IOException, ServletException {
        String username = authentication.getName();
        Usuarios usuario = usuariosRepository.findByCorreo(username).get();

        DefaultSavedRequest defaultSavedRequest =
                (DefaultSavedRequest) request.getSession().getAttribute("SPRING_SECURITY_SAVED_REQUEST");


        HttpSession session = request.getSession();
        session.setAttribute("usuario", usuario);
        session.setAttribute("carrito", new ArrayList<DetallesOrden>());


        if (defaultSavedRequest != null) {

            String targetURl = defaultSavedRequest.getRequestURL();
            new DefaultRedirectStrategy().sendRedirect(request, response, targetURl);
        } else { //estoy viniendo del botón de login

            // Verificar si la contraseña es la temporal
            if (usuario.getUsandoContrasenaTemporal()) {
                response.sendRedirect("/cambiar-contrasena?token=" + usuario.getToken());
            }
            else {
                String rol = "";
                for (GrantedAuthority role : authentication.getAuthorities()) {
                    rol = role.getAuthority();
                    break;
                }

                if (rol.equals("AdministradorDeSede")) {
                    response.sendRedirect("/administradorsede");
                } else if (rol.equals("Farmacista")) {
                    response.sendRedirect("/farmacista");
                } else if (rol.equals("Paciente")){
                    response.sendRedirect("/clinicarenacer");
                } else if (rol.equals("SuperAdmin")) {
                    response.sendRedirect("/superadmin");
                } else{
                    response.sendRedirect("/clinicarenacer");
                }
            }



        }
    }
}
