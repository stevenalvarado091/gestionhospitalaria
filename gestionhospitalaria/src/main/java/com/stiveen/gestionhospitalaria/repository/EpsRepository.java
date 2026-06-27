package com.stiveen.gestionhospitalaria.repository;

import com.stiveen.gestionhospitalaria.entity.Eps;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EpsRepository extends JpaRepository<Eps, Long> {

    Optional<Eps> findByCodigo(String codigo);

}