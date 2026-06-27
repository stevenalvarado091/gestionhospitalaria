package com.stiveen.gestionhospitalaria.repository;

import com.stiveen.gestionhospitalaria.entity.CorreoDestino;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface EpsCorreoRepository extends JpaRepository<CorreoDestino, Long> {

    @Query("""
        SELECT cd.correo
        FROM CorreoDestino cd
        WHERE cd.eps.id = (
            SELECT i.eps.id
            FROM Ingreso i
            WHERE i.id = :epsDestinoId
        )
        AND cd.activo = true
    """)
    List<String> findEmailsByEpsId(Long epsDestinoId);

}
