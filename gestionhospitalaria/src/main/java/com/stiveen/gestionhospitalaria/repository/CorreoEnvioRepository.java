package com.stiveen.gestionhospitalaria.repository;

import com.stiveen.gestionhospitalaria.entity.CorreoEnviado;
import com.stiveen.gestionhospitalaria.entity.CorreoEnvio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

public interface CorreoEnvioRepository
        extends JpaRepository<CorreoEnvio, Long> {

    @Query("""
            SELECT c FROM CorreoEnviado c
            LEFT JOIN FETCH c.adjuntos
            LEFT JOIN FETCH c.destinatarios
            LEFT JOIN FETCH c.envios
            WHERE c.ingreso.id = :id
            """)
    List<CorreoEnviado> findByIngresoId(Long id);

}
