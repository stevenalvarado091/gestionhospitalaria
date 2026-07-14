package com.stiveen.gestionhospitalaria.repository;

import com.stiveen.gestionhospitalaria.entity.CorreoEnviado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CorreoEnviadoRepository
        extends JpaRepository<CorreoEnviado, Long> {

    @Query("""
            SELECT DISTINCT c
            FROM CorreoEnviado c
            LEFT JOIN FETCH c.adjuntos
            WHERE c.ingreso.id = :ingresoId
            """)
    List<CorreoEnviado> findByIngresoIdConDetalle(
            @Param("ingresoId") Long ingresoId);
}