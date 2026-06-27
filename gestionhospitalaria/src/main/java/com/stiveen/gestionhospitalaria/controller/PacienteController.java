package com.stiveen.gestionhospitalaria.controller;

import com.stiveen.gestionhospitalaria.entity.Paciente;
import com.stiveen.gestionhospitalaria.enums.TipoDocumento;
import com.stiveen.gestionhospitalaria.service.PacienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.stiveen.gestionhospitalaria.dto.response.PacienteDetalleResponse;

import java.util.List;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    private final PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @PostMapping
    public Paciente guardarPaciente(@RequestBody Paciente paciente){
        return pacienteService.guardarPaciente(paciente);
    }

    @GetMapping
    public List<Paciente> listarTodos() {
        return pacienteService.listarTodos();
    }

    @GetMapping("/documento/{numeroDocumento}")
    public Paciente buscarPorDocumento(@PathVariable String numeroDocumento) {
        return pacienteService.buscarPorDocumento(numeroDocumento);
    }

    @GetMapping("/buscar")
    public ResponseEntity<Paciente> buscarPaciente(
            @RequestParam TipoDocumento tipoDocumento,
            @RequestParam String numeroDocumento) {

        Paciente paciente =
                pacienteService.buscarPorTipoYDocumento(
                        tipoDocumento,
                        numeroDocumento);

        if (paciente == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(paciente);
    }

    @GetMapping("/detalle")
    public PacienteDetalleResponse detallePaciente(
            @RequestParam TipoDocumento tipoDocumento,
            @RequestParam String numeroDocumento) {

        return pacienteService.buscarDetallePaciente(
                tipoDocumento,
                numeroDocumento);
    }

}
