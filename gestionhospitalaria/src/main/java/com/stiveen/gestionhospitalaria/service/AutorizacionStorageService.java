package com.stiveen.gestionhospitalaria.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;

@Service
public class AutorizacionStorageService {

    private static final String CARPETA_BASE = "uploads/autorizaciones";

    public String guardar(Long ingresoId, MultipartFile archivo) throws IOException {

        Path carpeta = Paths.get(CARPETA_BASE, ingresoId.toString());

        Files.createDirectories(carpeta);

        String nombreOriginal = archivo.getOriginalFilename();

        String nombreFisico = System.currentTimeMillis() + "_" + nombreOriginal;

        Path destino = carpeta.resolve(nombreFisico);

        Files.copy(
                archivo.getInputStream(),
                destino,
                StandardCopyOption.REPLACE_EXISTING
        );

        return destino.toString();
    }

    public Path obtenerRuta(String rutaArchivo) {
        return Paths.get(rutaArchivo);
    }

}