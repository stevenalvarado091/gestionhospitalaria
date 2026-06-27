package com.stiveen.gestionhospitalaria.dto.request;

public class CrearAutorizacionRequest {

    private Long ingresoId;

    private Long epsDestinoId;

    private String tipoAutorizacion;

    private String asunto;

    private String descripcion;

    private String usuario;

    private String rolUsuario;

    public Long getIngresoId() {
        return ingresoId;
    }

    public void setIngresoId(Long ingresoId) {
        this.ingresoId = ingresoId;
    }

    public Long getEpsDestinoId() {
        return epsDestinoId;
    }

    public void setEpsDestinoId(Long epsDestinoId) {
        this.epsDestinoId = epsDestinoId;
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
}