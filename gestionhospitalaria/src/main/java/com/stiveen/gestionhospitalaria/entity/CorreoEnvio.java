package com.stiveen.gestionhospitalaria.entity;

import com.stiveen.gestionhospitalaria.enums.TipoEnvioCorreo;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "correo_envio")
public class CorreoEnvio extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "correo_enviado_id", nullable = false)
    private CorreoEnviado correoEnviado;

    @Column(nullable = false)
    private Integer numeroEnvio;

    @Column(nullable = false)
    private LocalDateTime fechaEnvio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoEnvioCorreo tipoEnvio;

    @Column(nullable = false)
    private Boolean automatico;

    @Column(nullable = false)
    private Boolean activo = true;

    public CorreoEnviado getCorreoEnviado() {
        return correoEnviado;
    }

    public void setCorreoEnviado(CorreoEnviado correoEnviado) {
        this.correoEnviado = correoEnviado;
    }

    public Integer getNumeroEnvio() {
        return numeroEnvio;
    }

    public void setNumeroEnvio(Integer numeroEnvio) {
        this.numeroEnvio = numeroEnvio;
    }

    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(LocalDateTime fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public Boolean getAutomatico() {
        return automatico;
    }

    public void setAutomatico(Boolean automatico) {
        this.automatico = automatico;
    }

    public TipoEnvioCorreo getTipoEnvio() {
        return tipoEnvio;
    }

    public void setTipoEnvio(TipoEnvioCorreo tipoEnvio) {
        this.tipoEnvio = tipoEnvio;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}