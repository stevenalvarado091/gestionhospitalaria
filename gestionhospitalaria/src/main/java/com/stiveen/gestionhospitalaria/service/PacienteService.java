package com.stiveen.gestionhospitalaria.service;

import com.stiveen.gestionhospitalaria.entity.Paciente;
import com.stiveen.gestionhospitalaria.enums.TipoDocumento;
import com.stiveen.gestionhospitalaria.exception.PacienteDuplicadoException;
import com.stiveen.gestionhospitalaria.repository.IngresoRepository;
import com.stiveen.gestionhospitalaria.repository.PacienteRepository;
import org.springframework.stereotype.Service;
import com.stiveen.gestionhospitalaria.dto.response.PacienteDetalleResponse;
import com.stiveen.gestionhospitalaria.dto.response.IngresoResumenResponse;
import com.stiveen.gestionhospitalaria.entity.Ingreso;

import java.util.List;


@Service
public class PacienteService {

    private final PacienteRepository pacienteRepository;
    private final IngresoRepository ingresoRepository;

    public PacienteService(PacienteRepository pacienteRepository, IngresoRepository ingresoRepository) {
        this.pacienteRepository = pacienteRepository;
        this.ingresoRepository = ingresoRepository;
    }

    public Paciente guardarPaciente(Paciente paciente){

        boolean existe = pacienteRepository
                .findByTipoDocumentoAndNumeroDocumento(
                        paciente.getTipoDocumento(),
                        paciente.getNumeroDocumento()).isPresent();

        if(existe){
            throw new PacienteDuplicadoException(
                    "Ya existe un paciente con ese tipo y número de documento");
        }
        return pacienteRepository.save(paciente);
    }

    public List<Paciente> listarTodos() {
        return pacienteRepository.findAll();
    }

    public Paciente buscarPorDocumento(String numeroDocumento){
        return pacienteRepository.findByNumeroDocumento(numeroDocumento).orElse(null);
    }

    public Paciente buscarPorTipoYDocumento(
            TipoDocumento tipoDocumento,
            String numeroDocumento) {

        return pacienteRepository
                .findByTipoDocumentoAndNumeroDocumento(
                        tipoDocumento,
                        numeroDocumento)
                .orElse(null);
        }

    public PacienteDetalleResponse buscarDetallePaciente(
            TipoDocumento tipoDocumento,
            String numeroDocumento) {

        Paciente paciente = pacienteRepository
                .findByTipoDocumentoAndNumeroDocumento(tipoDocumento, numeroDocumento).orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        PacienteDetalleResponse dto = new PacienteDetalleResponse();

        dto.setId(paciente.getId());
        dto.setNombreCompleto(paciente.getPrimerNombre() + " " + paciente.getPrimerApellido());
        dto.setTipoDocumento(paciente.getTipoDocumento().name());
        dto.setNumeroDocumento(paciente.getNumeroDocumento());

        List<IngresoResumenResponse> ingresos = ingresoRepository.findByPacienteId(paciente.getId())
                        .stream()
                        .map(this::mapIngresoResumen)
                        .toList();

        dto.setIngresos(ingresos);
        return dto;
    }

    private IngresoResumenResponse mapIngresoResumen(Ingreso ingreso) {

        IngresoResumenResponse dto = new IngresoResumenResponse();

        dto.setId(ingreso.getId());
        dto.setNumeroIngreso(ingreso.getNumeroIngreso());
        dto.setEstado(ingreso.getEstado().name());
        dto.setFechaIngreso(ingreso.getFechaIngreso());

        return dto;
    }

    }
