package com.stiveen.gestionhospitalaria.configuracion;

import com.stiveen.gestionhospitalaria.security.user.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Registra los Beans necesarios para el proceso de autenticación
 * de usuarios mediante Spring Security.
 */
@Configuration
public class AuthenticationBeansConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public AuthenticationBeansConfig(
            CustomUserDetailsService customUserDetailsService
    ) {
        this.customUserDetailsService = customUserDetailsService;
    }

    /**
     * Codificador de contraseñas utilizado por la aplicación.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();

    }

    /**
     * Proveedor encargado de autenticar usuarios consultando
     * la base de datos mediante CustomUserDetailsService.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider proveedorAutenticacion =
                new DaoAuthenticationProvider();

        proveedorAutenticacion.setUserDetailsService(
                customUserDetailsService
        );

        proveedorAutenticacion.setPasswordEncoder(
                passwordEncoder()
        );

        return proveedorAutenticacion;

    }

    /**
     * Administrador principal de autenticación utilizado
     * por Spring Security.
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuracionAutenticacion
    ) throws Exception {

        return configuracionAutenticacion
                .getAuthenticationManager();

    }

}