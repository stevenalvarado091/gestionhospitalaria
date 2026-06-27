package com.stiveen.gestionhospitalaria.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class HistorialCuenta extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "ingreso_id", nullable = false)
    private Ingreso ingreso;

    private String tipoEvento;
    // ASIGNACION_FACTURADOR, DEVOLUCION, CORRECCION, etc

    private Long usuarioOrigenId;

    private Long usuarioDestinoId;

    private LocalDateTime fechaInicio;

    private LocalDateTime fechaFin;

    private String descripcion;

    // getters y setters
}
