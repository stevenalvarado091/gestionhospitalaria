package com.stiveen.gestionhospitalaria.security.user;

import com.stiveen.gestionhospitalaria.entity.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Representa el usuario autenticado dentro del contexto de Spring Security.
 *
 * Esta clase adapta la entidad Usuario al contrato requerido por
 * Spring Security para realizar la autenticación y autorización.
 */
public class CustomUserDetails implements UserDetails {

    private final Usuario usuario;

    public CustomUserDetails(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * Retorna los permisos asociados al usuario.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return List.of(
                new SimpleGrantedAuthority(
                        "ROLE_" + usuario.getRol().getNombre()
                )
        );
    }

    /**
     * Retorna la contraseña encriptada del usuario.
     */
    @Override
    public String getPassword() {
        return usuario.getPassword();
    }

    /**
     * Retorna el nombre de usuario utilizado para iniciar sesión.
     */
    @Override
    public String getUsername() {
        return usuario.getUsuario();
    }

    /**
     * Indica si la cuenta no ha expirado.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indica si la cuenta no está bloqueada.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indica si las credenciales siguen siendo válidas.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indica si el usuario está activo.
     */
    @Override
    public boolean isEnabled() {
        return Boolean.TRUE.equals(usuario.getActivo());
    }

    /**
     * Permite acceder a la entidad completa cuando sea necesario.
     */
    public Usuario getUsuario() {
        return usuario;
    }
}
