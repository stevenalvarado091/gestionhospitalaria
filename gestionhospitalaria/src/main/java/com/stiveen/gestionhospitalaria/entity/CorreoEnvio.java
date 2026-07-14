package com.stiveen.gestionhospitalaria.entity;

import com.stiveen.gestionhospitalaria.enums.TipoEnvioCorreo;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "correo_envio")
public class CorreoEnvio extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "correo_enviado_id", nullable = false)
    private CorreoEnviado correoEnviado;

    @Column(nullable = false)
    private Integer numeroEnvio;

    @Column
    private LocalDateTime fechaProgramada;

    @Column
    private LocalDateTime fechaEjecutada;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoEnvioCorreo tipoEnvio;

    @Column(nullable = false)
    private Boolean automatico;

    @Column(nullable = false)
    private Boolean activo = true;

    @OneToMany(
            mappedBy = "correoEnvio",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<CorreoEnvioAdjunto> adjuntos = new ArrayList<>();

// ===== Snapshot del envío =====

    @Column(nullable = false, length = 300)
    private String asunto;

    @Column(columnDefinition = "TEXT")
    private String mensaje;

    @Column(nullable = false, length = 150)
    private String usuario;

    @Column(nullable = false, length = 80)
    private String rolUsuario;

    @Column(columnDefinition = "TEXT")
    private String destinatarios;

    @Column(nullable = false)
    private Boolean escalamiento = false;



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

    public LocalDateTime getFechaProgramada() {
        return fechaProgramada;
    }

    public void setFechaProgramada(LocalDateTime fechaProgramada) {
        this.fechaProgramada = fechaProgramada;
    }

    public LocalDateTime getFechaEjecutada() {
        return fechaEjecutada;
    }

    public void setFechaEjecutada(LocalDateTime fechaEjecutada) {
        this.fechaEjecutada = fechaEjecutada;
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

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
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

    public String getDestinatarios() {
        return destinatarios;
    }

    public void setDestinatarios(String destinatarios) {
        this.destinatarios = destinatarios;
    }

    public Boolean getEscalamiento() {
        return escalamiento;
    }

    public void setEscalamiento(Boolean escalamiento) {
        this.escalamiento = escalamiento;
    }

    public List<CorreoEnvioAdjunto> getAdjuntos() {
        return adjuntos;
    }

    public void setAdjuntos(List<CorreoEnvioAdjunto> adjuntos) {
        this.adjuntos = adjuntos;
    }
}