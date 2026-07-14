package com.stiveen.gestionhospitalaria.service;

import com.stiveen.gestionhospitalaria.dto.request.CrearAutorizacionRequest;
import com.stiveen.gestionhospitalaria.dto.response.AutorizacionResponse;
import com.stiveen.gestionhospitalaria.entity.Autorizacion;
import com.stiveen.gestionhospitalaria.entity.Ingreso;
import com.stiveen.gestionhospitalaria.exception.RecursoNoEncontradoException;
import com.stiveen.gestionhospitalaria.repository.AutorizacionRepository;
import com.stiveen.gestionhospitalaria.repository.IngresoRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.List;

@Service
public class AutorizacionService {

    private static final String CARPETA_BASE = "uploads/autorizaciones";

    private final AutorizacionRepository autorizacionRepository;
    private final IngresoRepository ingresoRepository;

    public AutorizacionService(
            AutorizacionRepository autorizacionRepository,
            IngresoRepository ingresoRepository
    ) {

        this.autorizacionRepository = autorizacionRepository;
        this.ingresoRepository = ingresoRepository;

    }

    @Transactional
    public AutorizacionResponse guardar(

            Long ingresoId,

            CrearAutorizacionRequest request,

            MultipartFile archivo

    ) {

        Ingreso ingreso = ingresoRepository.findById(ingresoId)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Ingreso no encontrado."
                        ));


        validarRegistro(request, archivo);

        Autorizacion autorizacion = new Autorizacion();

        autorizacion.setIngreso(ingreso);

        autorizacion.setNumeroAutorizacion(
                limpiar(request.getNumeroAutorizacion())
        );

        autorizacion.setObservacion(
                limpiar(request.getObservacion())
        );

        autorizacion.setUsuario(
                request.getUsuario()
        );

        autorizacion.setRolUsuario(
                request.getRolUsuario()
        );

        guardarArchivo(

                autorizacion,

                ingresoId,

                archivo

        );

        Autorizacion guardada =
                autorizacionRepository.save(
                        autorizacion
                );

        return mapToResponse(
                guardada
        );

    }

    private void validarRegistro(

            CrearAutorizacionRequest request,

            MultipartFile archivo

    ) {

        boolean tieneNumero =

                request.getNumeroAutorizacion() != null

                        &&

                        !request.getNumeroAutorizacion().isBlank();

        boolean tieneObservacion =

                request.getObservacion() != null

                        &&

                        !request.getObservacion().isBlank();

        boolean tieneDocumento =

                archivo != null

                        &&

                        !archivo.isEmpty();

        if (!tieneNumero && !tieneObservacion && !tieneDocumento) {

            throw new IllegalArgumentException(

                    "Debe registrar un número de autorización, una observación o un documento."

            );

        }

    }

    private void guardarArchivo(

            Autorizacion autorizacion,

            Long ingresoId,

            MultipartFile archivo

    ) {

        if (archivo == null || archivo.isEmpty()) {
            return;
        }

        try {

            Path carpetaIngreso = Paths.get(
                    CARPETA_BASE,
                    ingresoId.toString()
            );

            Files.createDirectories(carpetaIngreso);

            String nombreOriginal = archivo.getOriginalFilename();

            String extension = obtenerExtension(nombreOriginal);

            String nombreFisico =

                    System.currentTimeMillis()

                            + "_"

                            + nombreOriginal;

            Path rutaArchivo = carpetaIngreso.resolve(
                    nombreFisico
            );

            Files.copy(

                    archivo.getInputStream(),

                    rutaArchivo,

                    StandardCopyOption.REPLACE_EXISTING

            );

            autorizacion.setNombreArchivo(
                    nombreFisico
            );

            autorizacion.setNombreOriginalArchivo(
                    nombreOriginal
            );

            autorizacion.setRutaArchivo(
                    rutaArchivo.toString()
            );

            autorizacion.setExtension(
                    extension
            );

            autorizacion.setTamanoArchivo(
                    archivo.getSize()
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "No fue posible guardar el archivo.",
                    e
            );

        }

    }

    private String obtenerExtension(String nombreArchivo) {

        if (nombreArchivo == null) {
            return "";
        }

        int ultimoPunto = nombreArchivo.lastIndexOf(".");

        if (ultimoPunto < 0) {
            return "";
        }

        return nombreArchivo
                .substring(ultimoPunto + 1)
                .toUpperCase();

    }

    private String limpiar(String texto) {

        if (texto == null) {
            return null;
        }

        texto = texto.trim();

        if (texto.isBlank()) {
            return null;
        }

        return texto;

    }


    @Transactional(readOnly = true)
    public List<AutorizacionResponse> listarPorIngreso(Long ingresoId) {

        Ingreso ingreso = ingresoRepository.findById(ingresoId)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Ingreso no encontrado."
                        ));

        return autorizacionRepository
                .findByIngresoOrderByFechaCreacionDesc(ingreso)
                .stream()
                .map(this::mapToResponse)
                .toList();

    }

    @Transactional(readOnly = true)
    public Resource descargar(Long autorizacionId) {

        Autorizacion autorizacion = autorizacionRepository.findById(autorizacionId)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Autorización no encontrada."
                        ));

        if (autorizacion.getRutaArchivo() == null) {

            throw new IllegalArgumentException(
                    "La autorización no tiene documento asociado."
            );

        }

        try {

            Path path = Paths.get(
                    autorizacion.getRutaArchivo()
            );

            Resource resource = new UrlResource(
                    path.toUri()
            );

            if (!resource.exists() || !resource.isReadable()) {

                throw new RuntimeException(
                        "No fue posible leer el archivo."
                );

            }

            return resource;

        } catch (MalformedURLException e) {

            throw new RuntimeException(
                    "Ruta del archivo inválida.",
                    e
            );

        }

    }

    private AutorizacionResponse mapToResponse(
            Autorizacion autorizacion
    ) {

        AutorizacionResponse response =
                new AutorizacionResponse();

        //==============================
        // Información de la autorización
        //==============================

        response.setId(
                autorizacion.getId()
        );

        response.setNumeroAutorizacion(
                autorizacion.getNumeroAutorizacion()
        );

        response.setObservacion(
                autorizacion.getObservacion()
        );

        //==============================
        // Información del documento
        //==============================

        response.setNombreArchivo(
                autorizacion.getNombreOriginalArchivo()
        );

        response.setExtension(
                autorizacion.getExtension()
        );

        response.setTamanoArchivo(
                autorizacion.getTamanoArchivo()
        );

        response.setTamano(

                convertirTamano(
                        autorizacion.getTamanoArchivo()
                )

        );

        //==============================
        // Información del ingreso
        //==============================

        response.setIngresoId(
                autorizacion.getIngreso().getId()
        );

        response.setNumeroIngreso(
                autorizacion.getIngreso().getNumeroIngreso()
        );

        //==============================
        // Trazabilidad
        //==============================

        response.setUsuario(
                autorizacion.getUsuario()
        );

        response.setRolUsuario(
                autorizacion.getRolUsuario()
        );

        response.setFechaCreacion(
                autorizacion.getFechaCreacion()
        );

        return response;

    }

    private String convertirTamano(Long bytes) {

        if (bytes == null) {
            return "";
        }

        double kb = bytes / 1024.0;

        if (kb < 1024) {
            return String.format("%.2f KB", kb);
        }

        double mb = kb / 1024.0;

        if (mb < 1024) {
            return String.format("%.2f MB", mb);
        }

        double gb = mb / 1024.0;

        return String.format("%.2f GB", gb);

    }

}