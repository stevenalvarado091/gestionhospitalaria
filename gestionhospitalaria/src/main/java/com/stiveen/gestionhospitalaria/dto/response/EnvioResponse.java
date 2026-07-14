package com.stiveen.gestionhospitalaria.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class EnvioResponse {


    private Long id;

    private Integer numeroEnvio;

    private String tipoEnvio;

    private Boolean activo;

    private Integer intento;

    private String tipo;

    private String estado;

    private LocalDateTime fechaProgramada;

    private LocalDateTime fechaEjecutada;

    private String asunto;

    private String mensaje;

    private List<String> destinatarios;

    private List<AdjuntoResponse> adjuntos;

    private String usuario;

    private String rolUsuario;

    private Boolean automatico;

    private String titulo;

    private String descripcion;

    private Boolean escalamiento;

    public EnvioResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumeroEnvio() {
        return numeroEnvio;
    }

    public void setNumeroEnvio(Integer numeroEnvio) {
        this.numeroEnvio = numeroEnvio;
    }

    public String getTipoEnvio() {
        return tipoEnvio;
    }

    public void setTipoEnvio(String tipoEnvio) {
        this.tipoEnvio = tipoEnvio;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Integer getIntento() {
        return intento;
    }

    public void setIntento(Integer intento) {
        this.intento = intento;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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

    public List<String> getDestinatarios() {
        return destinatarios;
    }

    public void setDestinatarios(List<String> destinatarios) {
        this.destinatarios = destinatarios;
    }

    public List<AdjuntoResponse> getAdjuntos() {
        return adjuntos;
    }

    public void setAdjuntos(List<AdjuntoResponse> adjuntos) {
        this.adjuntos = adjuntos;
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

    public Boolean getAutomatico() {
        return automatico;
    }

    public void setAutomatico(Boolean automatico) {
        this.automatico = automatico;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getEscalamiento() {
        return escalamiento;
    }

    public void setEscalamiento(Boolean escalamiento) {
        this.escalamiento = escalamiento;
    }
}