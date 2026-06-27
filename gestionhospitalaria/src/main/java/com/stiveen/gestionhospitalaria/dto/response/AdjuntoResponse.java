package com.stiveen.gestionhospitalaria.dto.response;

public class AdjuntoResponse {

    private String nombreArchivo;

    public AdjuntoResponse() {
    }

    public AdjuntoResponse(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }
}
