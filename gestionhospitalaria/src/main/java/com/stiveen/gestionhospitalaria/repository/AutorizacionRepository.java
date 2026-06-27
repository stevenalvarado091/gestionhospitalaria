package com.stiveen.gestionhospitalaria.repository;

import com.stiveen.gestionhospitalaria.entity.Autorizacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AutorizacionRepository
        extends JpaRepository<Autorizacion, Long> {

    List<Autorizacion> findByIngresoId(Long ingresoId);

}
