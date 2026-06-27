package com.stiveen.gestionhospitalaria.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "eps")
public class Eps extends BaseEntity {

    @JsonIgnore
    @OneToMany(mappedBy = "eps")
    private List<CorreoDestino> correosDestino = new ArrayList<>();

    @Column(nullable = false, unique = true, length = 20)
    private String codigo;

    @Column(nullable = false, unique = true, length = 200)
    private String nombre;

    @Column(length = 200)
    private String correoEscalamiento;

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre == null ? null : nombre.trim().toUpperCase();
    }

    public List<CorreoDestino> getCorreosDestino() {
        return correosDestino;
    }

    public void setCorreosDestino(List<CorreoDestino> correosDestino) {
        this.correosDestino = correosDestino;
    }

    public String getCorreoEscalamiento() {
        return correoEscalamiento;
    }

    public void setCorreoEscalamiento(String correoEscalamiento) {
        this.correoEscalamiento = correoEscalamiento;
    }
}