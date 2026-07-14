package com.stiveen.gestionhospitalaria.repository;

import com.stiveen.gestionhospitalaria.entity.CorreoEnvioAdjunto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CorreoEnvioAdjuntoRepository
        extends JpaRepository<CorreoEnvioAdjunto, Long> {
}