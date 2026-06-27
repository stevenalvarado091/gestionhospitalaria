package com.stiveen.gestionhospitalaria.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class CuentaIngreso extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "ingreso_id", nullable = false)
    private Ingreso ingreso;

    private String estadoCuenta;
    // EN_FACTURACION, DEVUELTO, EN_REVISION, CERRADO

    private Long facturadorActualId;

    private Long tecnicoActualId;

    private LocalDateTime fechaAsignacionFacturador;

    private LocalDateTime fechaAsignacionTecnico;

    // getters y setters


    public Ingreso getIngreso() {
        return ingreso;
    }

    public void setIngreso(Ingreso ingreso) {
        this.ingreso = ingreso;
    }

    public String getEstadoCuenta() {
        return estadoCuenta;
    }

    public void setEstadoCuenta(String estadoCuenta) {
        this.estadoCuenta = estadoCuenta;
    }

    public Long getFacturadorActualId() {
        return facturadorActualId;
    }

    public void setFacturadorActualId(Long facturadorActualId) {
        this.facturadorActualId = facturadorActualId;
    }

    public Long getTecnicoActualId() {
        return tecnicoActualId;
    }

    public void setTecnicoActualId(Long tecnicoActualId) {
        this.tecnicoActualId = tecnicoActualId;
    }

    public LocalDateTime getFechaAsignacionFacturador() {
        return fechaAsignacionFacturador;
    }

    public void setFechaAsignacionFacturador(LocalDateTime fechaAsignacionFacturador) {
        this.fechaAsignacionFacturador = fechaAsignacionFacturador;
    }

    public LocalDateTime getFechaAsignacionTecnico() {
        return fechaAsignacionTecnico;
    }

    public void setFechaAsignacionTecnico(LocalDateTime fechaAsignacionTecnico) {
        this.fechaAsignacionTecnico = fechaAsignacionTecnico;
    }
}