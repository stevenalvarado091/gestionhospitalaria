package com.stiveen.gestionhospitalaria.repository;

import com.stiveen.gestionhospitalaria.entity.Ingreso;
import com.stiveen.gestionhospitalaria.enums.TipoDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IngresoRepository extends JpaRepository<Ingreso, Long> {

    Optional<Ingreso> findByNumeroIngreso(String numeroIngreso);

    List<Ingreso> findByPacienteId(Long pacienteId);

    @Query("""
        SELECT i
        FROM Ingreso i
        JOIN FETCH i.paciente p
        JOIN FETCH i.eps e
        WHERE p.tipoDocumento = :tipoDocumento
        AND p.numeroDocumento = :numeroDocumento
        ORDER BY i.fechaActualizacion DESC
    """)
    List<Ingreso> buscarPorDocumento(
            @Param("tipoDocumento") TipoDocumento tipoDocumento,
            @Param("numeroDocumento") String numeroDocumento
    );


    @Query("""
    SELECT i
    FROM Ingreso i
    JOIN FETCH i.paciente p
    JOIN FETCH i.eps e
    WHERE UPPER(
        CONCAT(
            p.primerNombre,' ',
            COALESCE(p.segundoNombre,''),' ',
            p.primerApellido,' ',
            COALESCE(p.segundoApellido,'')
        )
    ) LIKE UPPER(CONCAT('%', :nombre, '%'))
    ORDER BY i.fechaActualizacion DESC
""")
    List<Ingreso> buscarPorNombre(
            @Param("nombre") String nombre
    );

}
