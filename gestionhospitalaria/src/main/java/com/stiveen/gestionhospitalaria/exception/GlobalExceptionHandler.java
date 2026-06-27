package com.stiveen.gestionhospitalaria.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;

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
    public ResponseEntity<Map<String, String>> manejarValidacion(
            ValidacionException ex) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("mensaje", ex.getMessage()));
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

}

