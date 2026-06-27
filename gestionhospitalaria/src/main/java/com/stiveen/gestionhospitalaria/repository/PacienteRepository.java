package com.stiveen.gestionhospitalaria.repository;

import com.stiveen.gestionhospitalaria.entity.Paciente;
import com.stiveen.gestionhospitalaria.enums.TipoDocumento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    Optional<Paciente> findByTipoDocumentoAndNumeroDocumento(
            TipoDocumento tipoDocumento,
            String numeroDocumento
    );

    Optional<Paciente> findByNumeroDocumento(String numeroDocumento);

}
