package com.stiveen.gestionhospitalaria.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "correo_enviado")
public class CorreoEnviado extends BaseEntity {



    @Column(nullable = false, length = 300)
    private String asunto;

    @Column(nullable = false, length = 5000)
    private String mensaje;

    @Column(nullable = false, length = 150)
    private String usuario;

    @Column(nullable = false, length = 100)
    private String rolUsuario;

    @ManyToOne
    @JoinColumn(name = "eps_destino_id")
    private Eps epsDestino;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingreso_id", nullable = false)
    private Ingreso ingreso;

    @OneToMany(
            mappedBy = "correoEnviado",
            cascade = CascadeType.ALL,
            orphanRemoval = false
    )
    private List<CorreoEnvio> envios = new ArrayList<>();

    @OneToMany(
            mappedBy = "correoEnviado",
            cascade = CascadeType.ALL,
            orphanRemoval = false
    )
    private List<CorreoDestinatario> destinatarios = new ArrayList<>();

    @OneToMany(
            mappedBy = "correo",
            cascade = CascadeType.ALL,
            orphanRemoval = false
    )
    private List<CorreoAdjunto> adjuntos = new ArrayList<>();

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
        this.rolUsuario = rolUsuario == null? null : rolUsuario.trim().toUpperCase();
    }

    public Ingreso getIngreso() {
        return ingreso;
    }

    public void setIngreso(Ingreso ingreso) {
        this.ingreso = ingreso;
    }

    public List<CorreoEnvio> getEnvios() {
        return envios;
    }

    public void setEnvios(List<CorreoEnvio> envios) {
        this.envios = envios;
    }

    public List<CorreoDestinatario> getDestinatarios() {
        return destinatarios;
    }

    public void setDestinatarios(List<CorreoDestinatario> destinatarios) {
        this.destinatarios = destinatarios;
    }

    public List<CorreoAdjunto> getAdjuntos() {
        return adjuntos;
    }

    public void setAdjuntos(List<CorreoAdjunto> adjuntos) {
        this.adjuntos = adjuntos;
    }

    public Eps getEpsDestino() {
        return epsDestino;
    }

    public void setEpsDestino(Eps epsDestino) {
        this.epsDestino = epsDestino;
    }
}