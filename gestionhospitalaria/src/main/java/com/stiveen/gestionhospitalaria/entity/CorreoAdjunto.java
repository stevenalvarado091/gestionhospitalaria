package com.stiveen.gestionhospitalaria.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "correo_adjunto")
public class CorreoAdjunto extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "correo_id", nullable = false)
    private CorreoEnviado correo;

    @Column(nullable = false, length = 300)
    private String nombreArchivo;

    @Column(nullable = false, length = 500)
    private String rutaArchivo;

    public CorreoEnviado getCorreo() {
        return correo;
    }

    public void setCorreo(CorreoEnviado correo) {
        this.correo = correo;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public String getRutaArchivo() {
        return rutaArchivo;
    }

    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }
}