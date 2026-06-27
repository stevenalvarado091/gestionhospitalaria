package com.stiveen.gestionhospitalaria.dto.response;

import java.time.LocalDateTime;

public class IngresoResumenResponse {

    private Long id;
    private String numeroIngreso;
    private String estado;
    private LocalDateTime fechaIngreso;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroIngreso() {
        return numeroIngreso;
    }

    public void setNumeroIngreso(String numeroIngreso) {
        this.numeroIngreso = numeroIngreso;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDateTime fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }
}