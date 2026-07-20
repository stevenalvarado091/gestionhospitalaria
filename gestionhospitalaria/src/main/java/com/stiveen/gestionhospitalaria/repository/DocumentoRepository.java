package com.stiveen.gestionhospitalaria.repository;

import com.stiveen.gestionhospitalaria.entity.Documento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentoRepository extends JpaRepository<Documento, Long> {

    List<Documento> findByIngresoIdOrderByFechaCreacionDesc(Long ingresoId);
}
