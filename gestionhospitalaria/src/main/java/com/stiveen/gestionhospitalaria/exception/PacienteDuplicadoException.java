package com.stiveen.gestionhospitalaria.exception;

public class PacienteDuplicadoException extends RuntimeException {

    public PacienteDuplicadoException(String mensaje) {
        super(mensaje);
    }
}