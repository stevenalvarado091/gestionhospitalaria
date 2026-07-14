package com.stiveen.gestionhospitalaria.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stiveen.gestionhospitalaria.dto.response.RespuestaErrorApi;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Se ejecuta cuando un usuario intenta acceder a un recurso protegido
 * sin estar autenticado o cuando el JWT es inválido.
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public JwtAuthenticationEntryPoint(
            ObjectMapper objectMapper
    ) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(

            HttpServletRequest solicitudHttp,

            HttpServletResponse respuestaHttp,

            AuthenticationException excepcionAutenticacion

    ) throws IOException, ServletException {

        RespuestaErrorApi respuestaErrorApi =
                new RespuestaErrorApi(
                        LocalDateTime.now(),
                        HttpStatus.UNAUTHORIZED.value(),
                        "La sesión ha expirado o el token no es válido.",
                        solicitudHttp.getRequestURI()
                );

        respuestaHttp.setStatus(HttpStatus.UNAUTHORIZED.value());

        respuestaHttp.setContentType(
                MediaType.APPLICATION_JSON_VALUE
        );

        objectMapper.writeValue(
                respuestaHttp.getOutputStream(),
                respuestaErrorApi
        );

    }

}
