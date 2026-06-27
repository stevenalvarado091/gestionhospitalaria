package com.stiveen.gestionhospitalaria.dto.response;

import java.time.LocalDateTime;

public class EnvioResponse {

    private Integer intento;
    private LocalDateTime fechaEnvio;
    private String estado;


    public Integer getIntento() {
        return intento;
    }

    public void setIntento(Integer intento) {
        this.intento = intento;
    }

    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(LocalDateTime fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
