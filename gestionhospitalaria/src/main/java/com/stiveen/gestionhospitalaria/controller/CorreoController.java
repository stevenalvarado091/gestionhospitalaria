package com.stiveen.gestionhospitalaria.controller;

import com.stiveen.gestionhospitalaria.dto.response.CorreoEnviadoResponse;
import com.stiveen.gestionhospitalaria.dto.response.CorreoResponse;
import com.stiveen.gestionhospitalaria.enums.TipoCorreo;
import com.stiveen.gestionhospitalaria.security.user.CustomUserDetails;
import com.stiveen.gestionhospitalaria.service.CorreoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/correos")
public class CorreoController {

    private final CorreoService correoService;

    public CorreoController(CorreoService correoService) {
        this.correoService = correoService;
    }

    @PostMapping("/enviar")
    public ResponseEntity<CorreoEnviadoResponse> enviarCorreo(

            @RequestParam Long ingresoId,
            @RequestParam Long epsDestinoId,
            @RequestParam TipoCorreo tipoCorreo,
            @RequestParam(required = false) MultipartFile[] archivos,

            @AuthenticationPrincipal CustomUserDetails usuarioAutenticado

    ) {

        CorreoEnviadoResponse response = correoService.enviarCorreo(
                ingresoId,
                epsDestinoId,
                tipoCorreo,
                archivos,
                usuarioAutenticado
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/ingreso/{ingresoId}")
    public ResponseEntity<List<CorreoResponse>> listarPorIngreso(
            @PathVariable Long ingresoId) {

        return ResponseEntity.ok(
                correoService.listarPorIngreso(ingresoId)
        );
    }

    @GetMapping("/adjuntos/{id}/descargar")
    public ResponseEntity<?> descargarAdjunto(
            @PathVariable Long id) {

        return correoService.descargarAdjunto(id);
    }
}