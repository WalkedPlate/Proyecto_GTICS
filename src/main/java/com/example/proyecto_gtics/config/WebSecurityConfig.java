package com.example.proyecto_gtics.config;

import com.example.proyecto_gtics.entity.Usuarios;
import com.example.proyecto_gtics.repository.UsuariosRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;

import javax.sql.DataSource;

@Configuration
public class WebSecurityConfig {


    final DataSource dataSource;
    final UsuariosRepository usuariosRepository;
    final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    //final CustomPasswordChangeFilter customPasswordChangeFilter;


    public WebSecurityConfig(DataSource dataSource, UsuariosRepository usuariosRepository,
                             CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler
                             /*CustomPasswordChangeFilter customPasswordChangeFilter*/) {
        this.dataSource = dataSource;
        this.usuariosRepository = usuariosRepository;
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
        //this.customPasswordChangeFilter = customPasswordChangeFilter;

    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsManager users(DataSource dataSource){
        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
        String sql1 = "select u.correo, u.contrasena, (u.estado_idestado ='Activo') as 'status' from usuarios u WHERE u.correo = ?; ";
        String sql2 = "select u.correo, u.tipo_usuario_idtipo_usuario from usuarios u WHERE u.correo = ?; ";

        users.setUsersByUsernameQuery(sql1);
        users.setAuthoritiesByUsernameQuery(sql2);
        return users;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //http.addFilterBefore(customPasswordChangeFilter, UsernamePasswordAuthenticationFilter.class);

        http.formLogin(formLogin ->
                formLogin
                        .loginPage("/login")
                        .loginProcessingUrl("/submitLoginForm")
                        .successHandler((request, response, authentication) -> {

                            DefaultSavedRequest defaultSavedRequest =
                                    (DefaultSavedRequest) request.getSession().getAttribute("SPRING_SECURITY_SAVED_REQUEST");

                            Usuarios usuario = usuariosRepository.findByCorreo(authentication.getName()).get();

                            HttpSession session = request.getSession();
                            session.setAttribute("usuario", usuario);


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


                        ));


        http.authorizeHttpRequests((authorize) -> authorize
                .requestMatchers("/farmacista","/farmacista/**").hasAnyAuthority("Farmacista")
                .requestMatchers("/superadmin","/superadmin/**").hasAnyAuthority("SuperAdmin")
                .requestMatchers("/administradorsede","/administradorsede/**").hasAnyAuthority("AdministradorDeSede")
                .anyRequest().permitAll()
        )
        ;

        http.logout(out -> out
                .logoutUrl("/logout")
                .logoutSuccessUrl("/logout").permitAll()
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true));

        //http.csrf(a -> a.disable());

        return http.build();
    }



}
