package com.stiveen.gestionhospitalaria.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class CorreoResponse {

    private Long id;
    private String destinatario;
    private String asunto;
    private String mensaje;
    private Long ingresoId;
    private String usuario;
    private String rolUsuario;
    private LocalDateTime fechaCreacion;

    private List<AdjuntoResponse> adjuntos;
    private List<EnvioResponse> envios;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
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

    public Long getIngresoId() {
        return ingresoId;
    }

    public void setIngresoId(Long ingresoId) {
        this.ingresoId = ingresoId;
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

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public List<AdjuntoResponse> getAdjuntos() {
        return adjuntos;
    }

    public void setAdjuntos(List<AdjuntoResponse> adjuntos) {
        this.adjuntos = adjuntos;
    }

    public List<EnvioResponse> getEnvios() {
        return envios;
    }

    public void setEnvios(List<EnvioResponse> envios) {
        this.envios = envios;
    }
}