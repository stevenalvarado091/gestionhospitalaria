package com.stiveen.gestionhospitalaria.repository;

import com.stiveen.gestionhospitalaria.entity.CorreoJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface CorreoJobRepository extends JpaRepository<CorreoJob, Long> {


    boolean existsByCorreoEnvioIdAndNumeroEnvio(Long correoEnvioId, int numeroEnvio);

    @Query("""
            SELECT j FROM CorreoJob j
            JOIN FETCH j.correoEnvio ce
            JOIN FETCH ce.correoEnviado c
            JOIN FETCH c.destinatarios
            WHERE j.ejecutado = false
            AND j.programadoPara <= :ahora
            AND (j.siguienteIntento IS NULL OR j.siguienteIntento <= :ahora)
            AND (j.enProceso = false OR j.enProceso = null OR j.bloqueadoEn < :limite)
            """)
    List<CorreoJob> findPendientes(LocalDateTime ahora, LocalDateTime limite);

}
