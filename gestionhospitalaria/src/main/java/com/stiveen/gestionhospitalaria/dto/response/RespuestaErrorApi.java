package com.stiveen.gestionhospitalaria.dto.response;

import java.time.LocalDateTime;

/**
 * Representa la estructura estándar utilizada por la API
 * para responder cuando ocurre un error.
 *
 * Todas las excepciones del sistema deberán retornar este
 * mismo formato para mantener consistencia entre módulos.
 */
public class RespuestaErrorApi {

    /**
     * Fecha y hora en que ocurrió el error.
     */
    private LocalDateTime fechaHora;

    /**
     * Código HTTP de la respuesta.
     */
    private Integer codigoEstado;

    /**
     * Descripción del error.
     */
    private String mensaje;

    /**
     * Ruta solicitada.
     */
    private String ruta;

    public RespuestaErrorApi() {
    }

    public RespuestaErrorApi(
            LocalDateTime fechaHora,
            Integer codigoEstado,
            String mensaje,
            String ruta
    ) {
        this.fechaHora = fechaHora;
        this.codigoEstado = codigoEstado;
        this.mensaje = mensaje;
        this.ruta = ruta;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Integer getCodigoEstado() {
        return codigoEstado;
    }

    public void setCodigoEstado(Integer codigoEstado) {
        this.codigoEstado = codigoEstado;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }
}