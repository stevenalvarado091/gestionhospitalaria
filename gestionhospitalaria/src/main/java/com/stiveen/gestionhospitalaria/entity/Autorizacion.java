package com.stiveen.gestionhospitalaria.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "autorizaciones")
public class Autorizacion extends BaseEntity {

    @Column(length = 100)
    private String numeroAutorizacion;

    @Column(length = 3000)
    private String observacion;

    @Column(length = 255)
    private String nombreArchivo;

    @Column(length = 255)
    private String nombreOriginalArchivo;

    @Column(length = 20)
    private String extension;

    @Column
    private Long tamanoArchivo;

    @Column(length = 500)
    private String rutaArchivo;

    @Column(nullable = false, length = 150)
    private String usuario;

    @Column(nullable = false, length = 50)
    private String rolUsuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingreso_id", nullable = false)
    private Ingreso ingreso;

    public String getNumeroAutorizacion() {
        return numeroAutorizacion;
    }

    public void setNumeroAutorizacion(String numeroAutorizacion) {
        this.numeroAutorizacion = numeroAutorizacion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
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

    public String getRutaArchivo() {
        return rutaArchivo;
    }

    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
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

    public Ingreso getIngreso() {
        return ingreso;
    }

    public void setIngreso(Ingreso ingreso) {
        this.ingreso = ingreso;
    }

    public String getNombreOriginalArchivo() {
        return nombreOriginalArchivo;
    }

    public void setNombreOriginalArchivo(String nombreOriginalArchivo) {
        this.nombreOriginalArchivo = nombreOriginalArchivo;
    }
}
