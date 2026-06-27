package com.stiveen.gestionhospitalaria.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "correo_destino")
public class CorreoDestino extends BaseEntity {

    @Column(nullable = false, length = 200)
    private String correo;

    @Column(nullable = false)
    private Boolean activo = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eps_id", nullable = false)
    private Eps eps;

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Eps getEps() {
        return eps;
    }

    public void setEps(Eps eps) {
        this.eps = eps;
    }
}
