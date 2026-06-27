package com.stiveen.gestionhospitalaria.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class IngresoDetalleResponse {

    private Long id;
    private String numeroIngreso;
    private String estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

    // ======================
    // PACIENTE (RESUMIDO)
    // ======================
    private Long pacienteId;
    private String nombrePaciente;
    private String tipoDocumento;
    private String numeroDocumento;
    private String sexo;
    private LocalDate fechaNacimiento;

    // ======================
    // EPS (RESUMIDO)
    // ======================
    private Long epsId;
    private String nombreEps;
    private String usuario;
    private String rolUsuario;

    // ======================
    // HISTORIAL COMPLETO
    // ======================
    private List<ObservacionResponse> observaciones;
    private List<DocumentoResponse> documentos;
    private List<AutorizacionResponse> autorizaciones;
    private List<CorreoResponse> correos;

    // ======================
    // GETTERS Y SETTERS
    // ======================

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroIngreso() {
        return numeroIngreso;
    }

    public void setNumeroIngreso(String numeroIngreso) {
        this.numeroIngreso = numeroIngreso;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public Long getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(Long pacienteId) {
        this.pacienteId = pacienteId;
    }

    public String getNombrePaciente() {
        return nombrePaciente;
    }

    public void setNombrePaciente(String nombrePaciente) {
        this.nombrePaciente = nombrePaciente;
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

    public Long getEpsId() {
        return epsId;
    }

    public void setEpsId(Long epsId) {
        this.epsId = epsId;
    }

    public String getNombreEps() {
        return nombreEps;
    }

    public void setNombreEps(String nombreEps) {
        this.nombreEps = nombreEps;
    }

    public List<ObservacionResponse> getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(List<ObservacionResponse> observaciones) {
        this.observaciones = observaciones;
    }

    public List<DocumentoResponse> getDocumentos() {
        return documentos;
    }

    public void setDocumentos(List<DocumentoResponse> documentos) {
        this.documentos = documentos;
    }

    public List<AutorizacionResponse> getAutorizaciones() {
        return autorizaciones;
    }

    public void setAutorizaciones(List<AutorizacionResponse> autorizaciones) {
        this.autorizaciones = autorizaciones;
    }

    public List<CorreoResponse> getCorreos() {
        return correos;
    }

    public void setCorreos(List<CorreoResponse> correos) {
        this.correos = correos;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
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