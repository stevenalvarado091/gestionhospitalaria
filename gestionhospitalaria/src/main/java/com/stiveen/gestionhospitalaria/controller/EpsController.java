package com.stiveen.gestionhospitalaria.controller;

import com.stiveen.gestionhospitalaria.entity.Eps;
import com.stiveen.gestionhospitalaria.service.EpsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/eps")
public class EpsController {

    private final EpsService epsService;

    public EpsController(EpsService epsService) {
        this.epsService = epsService;
    }

    @PostMapping
    public Eps guardar(@RequestBody Eps eps) {
        return epsService.guardar(eps);
    }

    @GetMapping
    public List<Eps> listarTodas() {
        return epsService.listarTodas();
    }

    @GetMapping("/codigo/{codigo}")
    public Eps buscarPorCodigo(@PathVariable String codigo) {
        return epsService.buscarPorCodigo(codigo);
    }
}
