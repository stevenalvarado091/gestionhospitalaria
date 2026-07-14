package com.stiveen.gestionhospitalaria.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "correo_envio_adjunto")
public class CorreoEnvioAdjunto extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "correo_envio_id", nullable = false)
    private CorreoEnvio correoEnvio;

    @Column(nullable = false, length = 300)
    private String nombreArchivo;

    @Column(nullable = false, length = 500)
    private String rutaArchivo;

    @Column(length = 20)
    private String extension;

    private Long tamanoArchivo;

    @Column(length = 30)
    private String tamano;

    // getters y setters


    public CorreoEnvio getCorreoEnvio() {
        return correoEnvio;
    }

    public void setCorreoEnvio(CorreoEnvio correoEnvio) {
        this.correoEnvio = correoEnvio;
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

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public Long getTamanoArchivo() {
        return tamanoArchivo;
    }

    public void setTamanoArchivo(Long tamanoArchivo) {
        this.tamanoArchivo = tamanoArchivo;
    }

    public String getTamano() {
        return tamano;
    }

    public void setTamano(String tamano) {
        this.tamano = tamano;
    }
}
