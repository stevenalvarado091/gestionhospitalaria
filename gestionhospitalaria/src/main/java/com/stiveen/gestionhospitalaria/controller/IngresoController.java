package com.stiveen.gestionhospitalaria.controller;

import com.stiveen.gestionhospitalaria.dto.request.IngresoRequest;
import com.stiveen.gestionhospitalaria.dto.response.IngresoDetalleResponse;
import com.stiveen.gestionhospitalaria.dto.response.IngresoListadoResponse;
import com.stiveen.gestionhospitalaria.dto.response.TimelineResponse;
import com.stiveen.gestionhospitalaria.entity.Ingreso;
import com.stiveen.gestionhospitalaria.enums.TipoDocumento;
import com.stiveen.gestionhospitalaria.service.IngresoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
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

    @GetMapping("/buscar-documento")
    public List<IngresoListadoResponse> buscarPorDocumento(

            @RequestParam TipoDocumento tipoDocumento,

            @RequestParam String numeroDocumento) {

        return ingresoService.buscarPorDocumento(
                tipoDocumento,
                numeroDocumento);
    }

    @GetMapping("/{id}/timeline")
    public List<TimelineResponse> obtenerTimeline(
            @PathVariable Long id) {

        return ingresoService.obtenerTimeline(id);
    }

    @GetMapping("/pagina")
    public Page<IngresoListadoResponse> listarPaginado(

            @RequestParam(defaultValue = "0")
            int pagina,

            @RequestParam(defaultValue = "10")
            int tamanio) {

        return ingresoService.listarPaginado(
                pagina,
                tamanio);
    }

    @GetMapping("/buscar-nombre")
    public List<IngresoListadoResponse> buscarPorNombre(
            @RequestParam String nombre){

        return ingresoService.buscarPorNombre(nombre);

    }

}
