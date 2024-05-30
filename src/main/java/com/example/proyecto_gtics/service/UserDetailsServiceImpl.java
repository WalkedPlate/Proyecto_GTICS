package com.example.proyecto_gtics.service;

import com.example.proyecto_gtics.entity.Usuarios;
import com.example.proyecto_gtics.repository.UsuariosRepository;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserDetailsServiceImpl {

    /*
    private final UsuariosRepository usuariosRepository;

    public UserDetailsServiceImpl(UsuariosRepository usuariosRepository) {
        this.usuariosRepository = usuariosRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Usuarios> optionalUsuario = usuariosRepository.findByCorreo(username);

        if (!optionalUsuario.isPresent()) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }

        Usuarios usuario = optionalUsuario.get();

        if (!"ACTIVO".equals(usuario.getEstadoUsuario().getIdEstadoUsuario())) {
            throw new DisabledException("Usuario no activo");
        }

        if (!"ACTIVO".equals(usuario.getEstadoUsuario().getIdEstadoUsuario())) {
            throw new DisabledException("Usuario no activo");
        }

        if (!"ACTIVO".equals(usuario.getEstadoUsuario().getIdEstadoUsuario())) {
            throw new DisabledException("Usuario no activo");
        }


        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + usuario.getTipoUsuario().getIdTipoUsuario()));

        return new org.springframework.security.core.userdetails.User(usuario.getCorreo(), usuario.getContrasena(), authorities);
    }

     */
}
