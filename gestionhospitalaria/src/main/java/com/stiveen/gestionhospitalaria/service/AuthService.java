package com.stiveen.gestionhospitalaria.service;

import com.stiveen.gestionhospitalaria.dto.request.LoginRequest;
import com.stiveen.gestionhospitalaria.dto.response.LoginResponse;
import com.stiveen.gestionhospitalaria.entity.Usuario;
import com.stiveen.gestionhospitalaria.repository.UsuarioRepository;
import com.stiveen.gestionhospitalaria.security.jwt.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(
            UsuarioRepository usuarioRepository,
            AuthenticationManager authenticationManager,
            JwtService jwtService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest request) {

        Usuario usuario = usuarioRepository
                .findByNumeroDocumento(request.getNumeroDocumento())
                .orElseThrow(() ->
                        new RuntimeException("Usuario o contraseña incorrectos")
                );

        if (!usuario.getActivo()) {
            throw new RuntimeException("Usuario inactivo");
        }

        authenticationManager.authenticate(

                new UsernamePasswordAuthenticationToken(

                        request.getNumeroDocumento(),

                        request.getPassword()

                )

        );

        usuario.setUltimoIngreso(LocalDateTime.now());

        usuarioRepository.save(usuario);

        String token = jwtService.generarToken(usuario);

        return new LoginResponse(
                token,
                usuario.getNumeroDocumento(),
                usuario.getNombreCompleto(),
                usuario.getRol().getNombre()
        );
    }
}
