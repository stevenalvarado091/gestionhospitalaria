package com.stiveen.gestionhospitalaria.repository;

import com.stiveen.gestionhospitalaria.entity.CorreoAdjunto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CorreoAdjuntoRepository
        extends JpaRepository<CorreoAdjunto, Long> {

    List<CorreoAdjunto> findByCorreoId(Long correoId);
}
