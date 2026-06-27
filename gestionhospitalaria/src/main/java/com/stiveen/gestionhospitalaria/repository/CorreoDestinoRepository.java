package com.stiveen.gestionhospitalaria.repository;

import com.stiveen.gestionhospitalaria.entity.CorreoDestino;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CorreoDestinoRepository
        extends JpaRepository<CorreoDestino, Long> {

    List<CorreoDestino> findByEpsIdAndActivoTrue(Long epsId);

}
