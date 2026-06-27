package com.stiveen.gestionhospitalaria.repository;

import com.stiveen.gestionhospitalaria.entity.CorreoDestinatario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CorreoDestinatarioRepository
        extends JpaRepository<CorreoDestinatario, Long> {

    List<CorreoDestinatario> findByCorreoEnviadoId(Long correoId);
}