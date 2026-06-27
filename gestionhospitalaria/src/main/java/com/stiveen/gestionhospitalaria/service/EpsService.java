package com.stiveen.gestionhospitalaria.service;

import com.stiveen.gestionhospitalaria.entity.Eps;
import com.stiveen.gestionhospitalaria.repository.EpsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EpsService {

    private final EpsRepository epsRepository;

    public EpsService(EpsRepository epsRepository) {
        this.epsRepository = epsRepository;
    }

    public Eps guardar(Eps eps) {
        return epsRepository.save(eps);
    }

    public List<Eps> listarTodas() {
        return epsRepository.findAll();
    }

    public Eps buscarPorCodigo(String codigo) {
        return epsRepository.findByCodigo(codigo)
                .orElseThrow(() ->
                        new RuntimeException("EPS no encontrada"));
    }
}
