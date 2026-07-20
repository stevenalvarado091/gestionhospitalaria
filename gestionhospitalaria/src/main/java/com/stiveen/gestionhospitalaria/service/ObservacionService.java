package com.stiveen.gestionhospitalaria.service;

import com.stiveen.gestionhospitalaria.dto.response.ObservacionResponse;
import com.stiveen.gestionhospitalaria.entity.Ingreso;
import com.stiveen.gestionhospitalaria.entity.Observacion;
import com.stiveen.gestionhospitalaria.repository.IngresoRepository;
import com.stiveen.gestionhospitalaria.repository.ObservacionRepository;
import org.springframework.stereotype.Service;
import com.stiveen.gestionhospitalaria.dto.request.ObservacionRequest;
import com.stiveen.gestionhospitalaria.entity.Usuario;
import com.stiveen.gestionhospitalaria.security.user.CustomUserDetails;
import com.stiveen.gestionhospitalaria.enums.TipoObservacion;

import java.util.List;

@Service
public class ObservacionService {

    private final ObservacionRepository observacionRepository;
    private final IngresoRepository ingresoRepository;

    public ObservacionService(ObservacionRepository observacionRepository,
                              IngresoRepository ingresoRepository) {
        this.observacionRepository = observacionRepository;
        this.ingresoRepository = ingresoRepository;
    }

    // GUARDAR
    public ObservacionResponse guardarObservacion(
            Long ingresoId,
            ObservacionRequest request,
            CustomUserDetails usuarioAutenticado){

        Ingreso ingreso = ingresoRepository.findById(ingresoId)
                .orElseThrow(() -> new RuntimeException("Ingreso no encontrado"));

        Observacion observacion = new Observacion();

        Usuario usuario = usuarioAutenticado.getUsuario();

        observacion.setIngreso(ingreso);
        observacion.setDescripcion(request.getDescripcion());

        observacion.setTipoObservacion(
                TipoObservacion.valueOf(request.getTipoObservacion())
        );

        observacion.setUsuario(usuario.getNombreCompleto());
        observacion.setRolUsuario(usuario.getRol().getNombre());

        Observacion saved = observacionRepository.save(observacion);

        return mapToResponse(saved);
    }

    // LISTAR POR INGRESO
    public List<ObservacionResponse> listarPorIngreso(Long ingresoId) {

        return observacionRepository.findByIngresoIdOrderByFechaCreacionDesc(ingresoId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // MAPPER
    private ObservacionResponse mapToResponse(Observacion observacion) {

        ObservacionResponse dto = new ObservacionResponse();

        dto.setId(observacion.getId());
        dto.setDescripcion(observacion.getDescripcion());
        dto.setUsuario(observacion.getUsuario());
        dto.setRolUsuario(observacion.getRolUsuario());
        dto.setTipoObservacion(observacion.getTipoObservacion().name());
        dto.setIngresoId(observacion.getIngreso().getId());

        dto.setFechaCreacion(
                observacion.getFechaCreacion()
        );

        return dto;
    }
}