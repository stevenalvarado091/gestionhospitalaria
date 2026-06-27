package com.stiveen.gestionhospitalaria.controller;

import com.stiveen.gestionhospitalaria.dto.response.CorreoEnviadoResponse;
import com.stiveen.gestionhospitalaria.dto.response.CorreoResponse;
import com.stiveen.gestionhospitalaria.entity.Paciente;
import com.stiveen.gestionhospitalaria.enums.TipoCorreo;
import com.stiveen.gestionhospitalaria.service.CorreoService;
import org.springframework.http.ResponseEntity;
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
            @RequestParam String usuario,
            @RequestParam String rolUsuario,
            @RequestParam(required = false)
            MultipartFile[] archivos
    ) {

        CorreoEnviadoResponse response = correoService.enviarCorreo(
                        ingresoId,
                        epsDestinoId,
                        tipoCorreo,
                        usuario,
                        rolUsuario,
                        archivos);

        return ResponseEntity.ok(response);
    }

    private String nombreCompleto(Paciente paciente) {

        return String.join(" ",
                paciente.getPrimerNombre(),
                paciente.getSegundoNombre() == null ? "" : paciente.getSegundoNombre(),
                paciente.getPrimerApellido(),
                paciente.getSegundoApellido() == null ? "" : paciente.getSegundoApellido()
        ).replaceAll("\\s+", " ").trim();
    }


    @GetMapping("/ingreso/{ingresoId}")
    public ResponseEntity<List<CorreoResponse>> listarPorIngreso(
            @PathVariable Long ingresoId){

        return ResponseEntity.ok(
                correoService.listarPorIngreso(ingresoId)
        );
    }
}