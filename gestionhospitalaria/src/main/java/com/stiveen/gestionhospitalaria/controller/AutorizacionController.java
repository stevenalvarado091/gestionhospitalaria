package com.stiveen.gestionhospitalaria.controller;

import com.stiveen.gestionhospitalaria.dto.request.CrearAutorizacionRequest;
import com.stiveen.gestionhospitalaria.dto.response.AutorizacionResponse;
import com.stiveen.gestionhospitalaria.security.user.CustomUserDetails;
import com.stiveen.gestionhospitalaria.service.AutorizacionService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/autorizaciones")
public class AutorizacionController {

    private final AutorizacionService autorizacionService;

    public AutorizacionController(AutorizacionService autorizacionService) {
        this.autorizacionService = autorizacionService;
    }

    @PostMapping(
            value = "/ingreso/{ingresoId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public AutorizacionResponse guardar(

            @PathVariable Long ingresoId,

            @RequestParam(required = false)
            String numeroAutorizacion,

            @RequestParam(required = false)
            String observacion,

            @RequestParam(required = false)
            MultipartFile archivo,

            @AuthenticationPrincipal
            CustomUserDetails usuarioAutenticado

    ) {

        CrearAutorizacionRequest request =
                new CrearAutorizacionRequest();

        request.setIngresoId(ingresoId);
        request.setNumeroAutorizacion(numeroAutorizacion);
        request.setObservacion(observacion);

        return autorizacionService.guardar(
                ingresoId,
                request,
                archivo,
                usuarioAutenticado
        );

    }

    @GetMapping("/ingreso/{ingresoId}")
    public List<AutorizacionResponse> listarPorIngreso(
            @PathVariable Long ingresoId
    ) {

        return autorizacionService.listarPorIngreso(
                ingresoId
        );

    }

    @GetMapping("/{autorizacionId}/descargar")
    public ResponseEntity<Resource> descargar(
            @PathVariable Long autorizacionId
    ) {

        Resource resource =
                autorizacionService.descargar(
                        autorizacionId
                );

        String nombreArchivo =
                URLEncoder.encode(
                        resource.getFilename(),
                        StandardCharsets.UTF_8
                );

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + nombreArchivo + "\""
                )
                .body(resource);

    }

}