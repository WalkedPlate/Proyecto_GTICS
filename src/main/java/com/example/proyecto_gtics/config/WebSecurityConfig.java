package com.example.proyecto_gtics.config;

import com.example.proyecto_gtics.repository.UsuariosRepository;
import com.example.proyecto_gtics.service.UserDetailsServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;

import javax.sql.DataSource;

@Configuration
public class WebSecurityConfig {


    final DataSource dataSource;
    final UsuariosRepository usuariosRepository;
    final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    public WebSecurityConfig(DataSource dataSource, UsuariosRepository usuariosRepository,
                             CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler) {
        this.dataSource = dataSource;
        this.usuariosRepository = usuariosRepository;
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
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


        http.formLogin(formLogin ->
                formLogin
                        .loginPage("/login")
                        .loginProcessingUrl("/submitLoginForm")
                        .successHandler(customAuthenticationSuccessHandler)
                        );


        http.authorizeHttpRequests((authorize) -> authorize
                .anyRequest().permitAll()
        )
        ;

        http.logout(out -> out
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login").permitAll()
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true));

        //http.csrf(a -> a.disable());

        return http.build();
    }



}
