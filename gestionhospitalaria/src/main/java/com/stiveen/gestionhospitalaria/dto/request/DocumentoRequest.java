package com.stiveen.gestionhospitalaria.dto.request;

import com.stiveen.gestionhospitalaria.enums.TipoDocumentoArchivo;

public class DocumentoRequest {

    private Long ingresoId;
    private String nombre;
    private TipoDocumentoArchivo tipoDocumento;
    private String usuario;
    private String rolUsuario;

    public Long getIngresoId() {
        return ingresoId;
    }

    public void setIngresoId(Long ingresoId) {
        this.ingresoId = ingresoId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public TipoDocumentoArchivo getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumentoArchivo tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
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
