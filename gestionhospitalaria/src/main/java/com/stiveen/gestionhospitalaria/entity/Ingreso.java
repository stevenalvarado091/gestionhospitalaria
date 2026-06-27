package com.stiveen.gestionhospitalaria.entity;

import com.stiveen.gestionhospitalaria.enums.EstadoIngreso;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ingreso")
public class Ingreso extends BaseEntity {

    @OneToMany(
            mappedBy = "ingreso",
            cascade = CascadeType.ALL,
            orphanRemoval = false
    )
    private List<Autorizacion> autorizaciones = new ArrayList<>();

    @Column(nullable = false, unique = true)
    private String numeroIngreso;

    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "eps_id", nullable = false)
    private Eps eps;

    @Column(nullable = false)
    private LocalDateTime fechaIngreso;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoIngreso estado;

    @Column(nullable = false)
    private Boolean activo = true;

    @Column(nullable = false, length = 150)
    private String usuario;

    @Column(nullable = false, length = 50)
    private String rolUsuario;

    public String getNumeroIngreso() {
        return numeroIngreso;
    }

    public void setNumeroIngreso(String numeroIngreso) {
        this.numeroIngreso = numeroIngreso;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Eps getEps() {
        return eps;
    }

    public void setEps(Eps eps) {
        this.eps = eps;
    }

    public LocalDateTime getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDateTime fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public EstadoIngreso getEstado() {
        return estado;
    }

    public void setEstado(EstadoIngreso estado) {
        this.estado = estado;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public List<Autorizacion> getAutorizaciones() {
        return autorizaciones;
    }

    public void setAutorizaciones(List<Autorizacion> autorizaciones) {
        this.autorizaciones = autorizaciones;
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
