package com.stiveen.gestionhospitalaria.service;

import com.stiveen.gestionhospitalaria.dto.request.CrearAutorizacionRequest;
import com.stiveen.gestionhospitalaria.dto.response.AutorizacionResponse;
import com.stiveen.gestionhospitalaria.entity.Autorizacion;
import com.stiveen.gestionhospitalaria.entity.Eps;
import com.stiveen.gestionhospitalaria.entity.Ingreso;
import com.stiveen.gestionhospitalaria.enums.TipoAutorizacion;
import com.stiveen.gestionhospitalaria.exception.RecursoNoEncontradoException;
import com.stiveen.gestionhospitalaria.repository.AutorizacionRepository;
import com.stiveen.gestionhospitalaria.repository.EpsRepository;
import com.stiveen.gestionhospitalaria.repository.IngresoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AutorizacionService {

    private final AutorizacionRepository autorizacionRepository;
    private final IngresoRepository ingresoRepository;
    private final EpsRepository epsRepository;

    public AutorizacionService(
            AutorizacionRepository autorizacionRepository,
            IngresoRepository ingresoRepository,
            EpsRepository epsRepository) {
        this.autorizacionRepository = autorizacionRepository;
        this.ingresoRepository = ingresoRepository;
        this.epsRepository = epsRepository;
    }

    public AutorizacionResponse crearAutorizacion(
            CrearAutorizacionRequest request) {

        Ingreso ingreso = ingresoRepository.findById(
                request.getIngresoId()).orElseThrow(() -> new RecursoNoEncontradoException(
                        "Ingreso no encontrado"));

        Eps epsDestino = epsRepository.findById(request.getEpsDestinoId()).orElseThrow(() -> new RecursoNoEncontradoException(
                        "EPS no encontrada"));

        Autorizacion autorizacion = new Autorizacion();

        autorizacion.setIngreso(ingreso);
        autorizacion.setEpsDestino(epsDestino);
        autorizacion.setTipoAutorizacion(TipoAutorizacion.valueOf(request.getTipoAutorizacion()));
        autorizacion.setAsunto(request.getAsunto());
        autorizacion.setDescripcion(request.getDescripcion());
        autorizacion.setUsuario(request.getUsuario());
        autorizacion.setRolUsuario(request.getRolUsuario());
        autorizacion.setCantidadAdjuntos(0);

        autorizacion = autorizacionRepository.save(autorizacion);

        return mapAutorizacion(autorizacion);
    }

    public AutorizacionResponse obtenerPorId(Long id) {

        Autorizacion autorizacion = autorizacionRepository.findById(id)
                        .orElseThrow(() -> new RecursoNoEncontradoException("Autorización no encontrada"));

        return mapAutorizacion(autorizacion);
    }

    public List<AutorizacionResponse> listarPorIngreso(
            Long ingresoId) {

        return autorizacionRepository
                .findByIngresoId(ingresoId)
                .stream()
                .map(this::mapAutorizacion)
                .toList();
    }

    private AutorizacionResponse mapAutorizacion(
            Autorizacion autorizacion
    ) {

        AutorizacionResponse dto =
                new AutorizacionResponse();

        dto.setId(autorizacion.getId());
        dto.setTipoAutorizacion(autorizacion.getTipoAutorizacion().name());
        dto.setAsunto(autorizacion.getAsunto());
        dto.setDescripcion(autorizacion.getDescripcion());
        dto.setUsuario(autorizacion.getUsuario());
        dto.setRolUsuario(autorizacion.getRolUsuario());
        dto.setNumeroIngreso(autorizacion.getIngreso().getNumeroIngreso());
        dto.setEpsDestino(autorizacion.getEpsDestino().getNombre());
        dto.setCantidadAdjuntos(autorizacion.getCantidadAdjuntos());
        dto.setFechaCreacion(autorizacion.getFechaCreacion());

        return dto;
    }
}