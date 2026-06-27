package com.stiveen.gestionhospitalaria.dto.response;

import java.util.List;

public class PacienteDetalleResponse {

    private Long id;
    private String nombreCompleto;
    private String tipoDocumento;
    private String numeroDocumento;

    private List<IngresoResumenResponse> ingresos;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public List<IngresoResumenResponse> getIngresos() {
        return ingresos;
    }

    public void setIngresos(List<IngresoResumenResponse> ingresos) {
        this.ingresos = ingresos;
    }
}
