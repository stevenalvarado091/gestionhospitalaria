package com.stiveen.gestionhospitalaria.controller;

import com.stiveen.gestionhospitalaria.dto.request.IngresoRequest;
import com.stiveen.gestionhospitalaria.dto.response.IngresoDetalleResponse;
import com.stiveen.gestionhospitalaria.dto.response.IngresoListadoResponse;
import com.stiveen.gestionhospitalaria.dto.response.TimelineResponse;
import com.stiveen.gestionhospitalaria.entity.Ingreso;
import com.stiveen.gestionhospitalaria.service.IngresoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ingresos")
public class IngresoController {

    private final IngresoService ingresoService;

    public IngresoController(IngresoService ingresoService) {
        this.ingresoService = ingresoService;
    }

    @PostMapping
    public Ingreso crearIngreso(
            @Valid @RequestBody IngresoRequest request) {
        return ingresoService.crearIngreso(request);
    }

    @GetMapping
    public List<IngresoListadoResponse> listarTodos() {
        return ingresoService.listarTodos();
    }

    @GetMapping("/{id}")
    public IngresoDetalleResponse obtenerIngreso(
            @PathVariable Long id) {
        return ingresoService.getDetalleIngreso(id);
    }

    @GetMapping("/numero/{numeroIngreso}")
    public IngresoDetalleResponse obtenerPorNumeroIngreso(
            @PathVariable String numeroIngreso) {

        return ingresoService.getDetalleIngresoPorNumeroIngreso(numeroIngreso);
    }

    @GetMapping("/{id}/timeline")
    public List<TimelineResponse> obtenerTimeline(
            @PathVariable Long id) {

        return ingresoService.obtenerTimeline(id);
    }

}
