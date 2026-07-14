package com.stiveen.gestionhospitalaria.exception;

import com.stiveen.gestionhospitalaria.dto.response.RespuestaErrorApi;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PacienteDuplicadoException.class)
    public ResponseEntity<Map<String, String>> manejarPacienteDuplicado(PacienteDuplicadoException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("mensaje", ex.getMessage()));
    }


    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Map<String, String>> manejarRecursoNoEncontrado(
            RecursoNoEncontradoException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("mensaje", ex.getMessage()));
    }


    @ExceptionHandler(IngresoDuplicadoException.class)
    public ResponseEntity<Map<String, String>> manejarIngresoDuplicado(
            IngresoDuplicadoException ex) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("mensaje", ex.getMessage()));
    }

    @ExceptionHandler(ValidacionException.class)
    public ResponseEntity<RespuestaErrorApi> manejarValidacion(
            ValidacionException excepcion,
            HttpServletRequest solicitudHttp
    ) {

        return construirRespuestaError(
                HttpStatus.BAD_REQUEST,
                excepcion.getMessage(),
                solicitudHttp
        );

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> manejarValidaciones(
            MethodArgumentNotValidException ex) {

        String mensaje = ex.getBindingResult()
                .getFieldErrors()
                .get(0)
                .getDefaultMessage();

        return ResponseEntity.badRequest()
                .body(Map.of("mensaje", mensaje));
    }

    @ExceptionHandler(EpsNoEncontradaException.class)
    public ResponseEntity<Map<String, String>> manejarEpsNoEncontrada(
            EpsNoEncontradaException ex) {

        return ResponseEntity.badRequest()
                .body(Map.of("mensaje", ex.getMessage()));
    }

    @ExceptionHandler(IngresoNoEncontradoException.class)
    public ResponseEntity<RespuestaErrorApi> manejarIngresoNoEncontrado(
            IngresoNoEncontradoException excepcion,
            HttpServletRequest solicitudHttp
    ) {

        return construirRespuestaError(
                HttpStatus.NOT_FOUND,
                excepcion.getMessage(),
                solicitudHttp
        );

    }


    private ResponseEntity<RespuestaErrorApi> construirRespuestaError(
            HttpStatus estadoHttp,
            String mensaje,
            HttpServletRequest solicitudHttp
    ) {

        RespuestaErrorApi respuestaErrorApi =
                new RespuestaErrorApi(
                        LocalDateTime.now(),
                        estadoHttp.value(),
                        mensaje,
                        solicitudHttp.getRequestURI()
                );

        return ResponseEntity
                .status(estadoHttp)
                .body(respuestaErrorApi);

    }

}

