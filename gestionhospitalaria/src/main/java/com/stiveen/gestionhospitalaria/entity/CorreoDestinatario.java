package com.stiveen.gestionhospitalaria.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "correo_destinatario")
public class CorreoDestinatario extends BaseEntity {

    @Column(nullable = false, length = 200)
    private String correo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "correo_id", nullable = false)
    private CorreoEnviado correoEnviado;

    // getters y setters


    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }


    public void setCorreoEnviado(CorreoEnviado correoEnviado) {
        this.correoEnviado = correoEnviado;
    }
}
