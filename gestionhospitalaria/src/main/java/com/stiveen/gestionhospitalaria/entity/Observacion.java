package com.stiveen.gestionhospitalaria.entity;

import com.stiveen.gestionhospitalaria.enums.TipoObservacion;
import jakarta.persistence.*;

@Entity
@Table(name = "observaciones")
public class Observacion extends BaseEntity {

    @Column(nullable = false, length = 2000)
    private String descripcion;

    @Column(nullable = false, length = 150)
    private String usuario;

    @Column(nullable = false, length = 50)
    private String rolUsuario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private TipoObservacion tipoObservacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingreso_id", nullable = false)
    private Ingreso ingreso;


    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion == null ? null : descripcion.trim().toUpperCase();
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getRolUsuario() {
        return rolUsuario;
    }

    public void setRolUsuario(String rolUsuario) {
        this.rolUsuario = rolUsuario;
    }

    public TipoObservacion getTipoObservacion() {
        return tipoObservacion;
    }

    public void setTipoObservacion(TipoObservacion tipoObservacion) {
        this.tipoObservacion = tipoObservacion;
    }

    public Ingreso getIngreso() {
        return ingreso;
    }

    public void setIngreso(Ingreso ingreso) {
        this.ingreso = ingreso;
    }
}