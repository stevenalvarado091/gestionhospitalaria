package com.stiveen.gestionhospitalaria.configuracion;

import com.stiveen.gestionhospitalaria.entity.Rol;
import com.stiveen.gestionhospitalaria.entity.Usuario;
import com.stiveen.gestionhospitalaria.repository.RolRepository;
import com.stiveen.gestionhospitalaria.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DatosIniciales {

    @Bean
    CommandLineRunner inicializarUsuarios(
            RolRepository rolRepository,
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder
    ) {

        return args -> {

            Rol rolAdmin = rolRepository
                    .findByNombre("ADMIN")
                    .orElseGet(() -> {

                        Rol rol = new Rol();

                        rol.setNombre("ADMIN");
                        rol.setDescripcion("Administrador del sistema");

                        return rolRepository.save(rol);
                    });

            if (usuarioRepository.findByUsuario("admin").isEmpty()) {

                Usuario usuario = new Usuario();

                usuario.setUsuario("admin");
                usuario.setNombreCompleto("ADMINISTRADOR");
                usuario.setCorreo("admin@sistema.local");

                usuario.setPassword(
                        passwordEncoder.encode("Admin123*")
                );

                usuario.setRol(rolAdmin);
                usuario.setActivo(true);

                usuarioRepository.save(usuario);

                System.out.println(
                        "Usuario administrador creado correctamente"
                );
            }

        };
    }
}