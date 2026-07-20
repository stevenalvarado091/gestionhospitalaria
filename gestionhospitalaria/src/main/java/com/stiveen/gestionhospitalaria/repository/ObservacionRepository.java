package com.stiveen.gestionhospitalaria.repository;

import com.stiveen.gestionhospitalaria.entity.Observacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ObservacionRepository
        extends JpaRepository<Observacion, Long> {

    List<Observacion> findByIngresoIdOrderByFechaCreacionDesc(Long ingresoId);

}
