package com.stiveen.gestionhospitalaria.security.user;

import com.stiveen.gestionhospitalaria.entity.Usuario;
import com.stiveen.gestionhospitalaria.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Servicio encargado de obtener un usuario desde la base de datos
 * para que Spring Security pueda realizar el proceso de autenticación.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(
            UsuarioRepository usuarioRepository
    ) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Busca un usuario por su nombre de usuario.
     * Este metodo es invocado automáticamente por Spring Security
     * cuando necesita autenticar un usuario.
     */
    @Override
    public UserDetails loadUserByUsername(
            String nombreUsuario
    ) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository
                .findByNumeroDocumento(nombreUsuario)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "No existe un usuario registrado con el documento: "
                                        + nombreUsuario
                        )
                );

        return new CustomUserDetails(usuario);
    }

}
