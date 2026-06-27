package com.stiveen.gestionhospitalaria.dto.response;

import java.time.LocalDateTime;

public class AutorizacionResponse {

    private Long id;
    private String tipoAutorizacion;
    private String asunto;
    private String descripcion;
    private String usuario;
    private String rolUsuario;
    private String numeroIngreso;
    private String epsDestino;
    private Integer cantidadAdjuntos;
    private LocalDateTime fechaCreacion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipoAutorizacion() {
        return tipoAutorizacion;
    }

    public void setTipoAutorizacion(String tipoAutorizacion) {
        this.tipoAutorizacion = tipoAutorizacion;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    public String getNumeroIngreso() {
        return numeroIngreso;
    }

    public void setNumeroIngreso(String numeroIngreso) {
        this.numeroIngreso = numeroIngreso;
    }

    public String getEpsDestino() {
        return epsDestino;
    }

    public void setEpsDestino(String epsDestino) {
        this.epsDestino = epsDestino;
    }

    public Integer getCantidadAdjuntos() {
        return cantidadAdjuntos;
    }

    public void setCantidadAdjuntos(Integer cantidadAdjuntos) {
        this.cantidadAdjuntos = cantidadAdjuntos;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
