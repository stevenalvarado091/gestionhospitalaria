package com.stiveen.gestionhospitalaria.security.jwt;

import com.stiveen.gestionhospitalaria.entity.Usuario;
import com.stiveen.gestionhospitalaria.repository.UsuarioRepository;
import com.stiveen.gestionhospitalaria.security.user.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.ExpiredJwtException;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final UsuarioRepository usuarioRepository;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            UsuarioRepository usuarioRepository
    ) {

        this.jwtService = jwtService;
        this.usuarioRepository = usuarioRepository;

    }

    @Override
    protected void doFilterInternal(

            HttpServletRequest solicitudHttp,

            HttpServletResponse respuestaHttp,

            FilterChain cadenaFiltros

    ) throws ServletException, IOException {

        String encabezadoAutorizacion =
                solicitudHttp.getHeader("Authorization");

        if (

                encabezadoAutorizacion == null ||

                        !encabezadoAutorizacion.startsWith("Bearer ")

        ) {

            cadenaFiltros.doFilter(
                    solicitudHttp,
                    respuestaHttp
            );

            return;

        }

        String tokenJwt =
                encabezadoAutorizacion.substring(7);

        String numeroDocumento;

        try {

            numeroDocumento =
                    jwtService.obtenerIdentificadorUsuario(tokenJwt);

        } catch (ExpiredJwtException ex) {

            SecurityContextHolder.clearContext();
            respuestaHttp.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Token expirado"
            );
            return;

        } catch (Exception ex) {

            SecurityContextHolder.clearContext();
            respuestaHttp.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Token inválido"
            );
            return;

        }

        if (

                numeroDocumento != null &&

                        SecurityContextHolder.getContext()
                                .getAuthentication() == null

        ) {

            Usuario usuario =
                    usuarioRepository
                            .findByNumeroDocumento(numeroDocumento)
                            .orElse(null);

            if (

                    usuario != null &&

                            jwtService.esTokenValido(
                                    tokenJwt,
                                    usuario
                            )

            ) {

                CustomUserDetails usuarioAutenticado =
                        new CustomUserDetails(usuario);

                UsernamePasswordAuthenticationToken autenticacion =
                        new UsernamePasswordAuthenticationToken(

                                usuarioAutenticado,

                                null,

                                usuarioAutenticado.getAuthorities()

                        );

                autenticacion.setDetails(

                        new WebAuthenticationDetailsSource()
                                .buildDetails(solicitudHttp)

                );

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(autenticacion);

            }

        }

        cadenaFiltros.doFilter(
                solicitudHttp,
                respuestaHttp
        );

    }

}
