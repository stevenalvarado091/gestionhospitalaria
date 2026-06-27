package com.stiveen.gestionhospitalaria.dto.response;

import com.stiveen.gestionhospitalaria.enums.TipoDocumento;

import java.time.LocalDate;

public class PacienteResponse {

    private Long id;
//    private String eps;
    private TipoDocumento tipoDocumento;
    private String numeroDocumento;
    private String nombreCompleto;
    private LocalDate fechaNacimiento;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    public String getEps() {
//        return eps;
//    }
//
//    public void setEps(String eps) {
//        this.eps = eps;
//    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }


}
