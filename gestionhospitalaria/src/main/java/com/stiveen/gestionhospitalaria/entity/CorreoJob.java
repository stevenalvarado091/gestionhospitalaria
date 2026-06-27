package com.stiveen.gestionhospitalaria.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "correo_job", uniqueConstraints = {
                @UniqueConstraint(name = "uk_correo_envio_numero",
                        columnNames = {"correo_envio_id", "numero_envio"
                })
        })

public class CorreoJob extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "correo_envio_id", nullable = false)
    private CorreoEnvio correoEnvio;

    @Column(name = "numero_envio", nullable = false)
    private Integer numeroEnvio;

    @Column(nullable = false)
    private LocalDateTime programadoPara;

    @Column(nullable = false)
    private Boolean enProceso = false;

    @Column(nullable = false)
    private Boolean ejecutado = false;

    @Column
    private LocalDateTime bloqueadoEn;

    @Column(nullable = false)
    private Integer intentos = 0;

    @Column(nullable = false)
    private Integer maxIntentos = 3;

    @Column
    private LocalDateTime siguienteIntento;

    @Column
    private String ultimoError;

    // GETTERS / SETTERS

    public CorreoEnvio getCorreoEnvio() {
        return correoEnvio;
    }

    public void setCorreoEnvio(CorreoEnvio correoEnvio) {
        this.correoEnvio = correoEnvio;
    }

    public Integer getNumeroEnvio() {
        return numeroEnvio;
    }

    public void setNumeroEnvio(Integer numeroEnvio) {
        this.numeroEnvio = numeroEnvio;
    }

    public LocalDateTime getProgramadoPara() {
        return programadoPara;
    }

    public void setProgramadoPara(LocalDateTime programadoPara) {
        this.programadoPara = programadoPara;
    }

    public Boolean getEnProceso() {
        return enProceso;
    }

    public void setEnProceso(Boolean enProceso) {
        this.enProceso = enProceso;
    }

    public Boolean getEjecutado() {
        return ejecutado;
    }

    public void setEjecutado(Boolean ejecutado) {
        this.ejecutado = ejecutado;
    }

    public LocalDateTime getBloqueadoEn() {
        return bloqueadoEn;
    }

    public void setBloqueadoEn(LocalDateTime bloqueadoEn) {
        this.bloqueadoEn = bloqueadoEn;
    }

    public Integer getIntentos() {
        return intentos;
    }

    public void setIntentos(Integer intentos) {
        this.intentos = intentos;
    }

    public Integer getMaxIntentos() {
        return maxIntentos;
    }

    public void setMaxIntentos(Integer maxIntentos) {
        this.maxIntentos = maxIntentos;
    }

    public LocalDateTime getSiguienteIntento() {
        return siguienteIntento;
    }

    public void setSiguienteIntento(LocalDateTime siguienteIntento) {
        this.siguienteIntento = siguienteIntento;
    }

    public String getUltimoError() {
        return ultimoError;
    }

    public void setUltimoError(String ultimoError) {
        this.ultimoError = ultimoError;
    }
}


