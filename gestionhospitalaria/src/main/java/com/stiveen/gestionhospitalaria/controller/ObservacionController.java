package com.stiveen.gestionhospitalaria.controller;

import com.stiveen.gestionhospitalaria.dto.request.ObservacionRequest;
import com.stiveen.gestionhospitalaria.dto.response.ObservacionResponse;
import com.stiveen.gestionhospitalaria.entity.Observacion;
import com.stiveen.gestionhospitalaria.security.user.CustomUserDetails;
import com.stiveen.gestionhospitalaria.service.ObservacionService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/observaciones")
public class ObservacionController {

    private final ObservacionService observacionService;

    public ObservacionController(
            ObservacionService observacionService
    ) {
        this.observacionService = observacionService;
    }

    @PostMapping("/ingresos/{ingresoId}")
    public ObservacionResponse guardar(
            @PathVariable Long ingresoId,
            @RequestBody ObservacionRequest request,
            @AuthenticationPrincipal CustomUserDetails usuarioAutenticado) {

        return observacionService.guardarObservacion(
                ingresoId,
                request,
                usuarioAutenticado
        );
    }

    @GetMapping("/ingresos/{ingresoId}")
    public List<ObservacionResponse> listar(@PathVariable Long ingresoId) {
        return observacionService.listarPorIngreso(ingresoId);
    }
}