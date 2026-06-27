package com.stiveen.gestionhospitalaria.entity;

import com.stiveen.gestionhospitalaria.enums.TipoAutorizacion;
import jakarta.persistence.*;

@Entity
@Table(name = "autorizaciones")
public class Autorizacion extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private TipoAutorizacion tipoAutorizacion;

    @Column(nullable = false, length = 300)
    private String asunto;

    @Column(nullable = false, length = 3000)
    private String descripcion;

    @Column(nullable = false, length = 150)
    private String usuario;

    @Column(nullable = false, length = 50)
    private String rolUsuario;

    @Column(nullable = false)
    private Integer cantidadAdjuntos = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingreso_id", nullable = false)
    private Ingreso ingreso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eps_destino_id", nullable = false)
    private Eps epsDestino;

    // getters y setters

    public TipoAutorizacion getTipoAutorizacion() {
        return tipoAutorizacion;
    }

    public void setTipoAutorizacion(TipoAutorizacion tipoAutorizacion) {
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

    public Integer getCantidadAdjuntos() {
        return cantidadAdjuntos;
    }

    public void setCantidadAdjuntos(Integer cantidadAdjuntos) {
        this.cantidadAdjuntos = cantidadAdjuntos;
    }

    public Ingreso getIngreso() {
        return ingreso;
    }

    public void setIngreso(Ingreso ingreso) {
        this.ingreso = ingreso;
    }

    public Eps getEpsDestino() {
        return epsDestino;
    }

    public void setEpsDestino(Eps epsDestino) {
        this.epsDestino = epsDestino;
    }

}

