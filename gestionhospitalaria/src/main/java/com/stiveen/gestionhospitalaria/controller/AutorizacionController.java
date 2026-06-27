package com.stiveen.gestionhospitalaria.controller;

import com.stiveen.gestionhospitalaria.dto.request.CrearAutorizacionRequest;
import com.stiveen.gestionhospitalaria.dto.response.AutorizacionResponse;
import com.stiveen.gestionhospitalaria.service.AutorizacionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/autorizaciones")
public class AutorizacionController {

    private final AutorizacionService autorizacionService;

    public AutorizacionController(
            AutorizacionService autorizacionService
    ) {
        this.autorizacionService = autorizacionService;
    }

    @PostMapping
    public AutorizacionResponse crear(
            @RequestBody CrearAutorizacionRequest request
    ) {
        return autorizacionService.crearAutorizacion(request);
    }

    @GetMapping("/{id}")
    public AutorizacionResponse obtenerPorId(
            @PathVariable Long id
    ) {
        return autorizacionService.obtenerPorId(id);
    }

    @GetMapping("/ingreso/{ingresoId}")
    public List<AutorizacionResponse> listarPorIngreso(
            @PathVariable Long ingresoId
    ) {
        return autorizacionService.listarPorIngreso(
                ingresoId
        );
    }
}