package com.stiveen.gestionhospitalaria.service;

import com.stiveen.gestionhospitalaria.dto.response.DocumentoResponse;
import com.stiveen.gestionhospitalaria.entity.Documento;
import com.stiveen.gestionhospitalaria.entity.Ingreso;
import com.stiveen.gestionhospitalaria.enums.TipoDocumentoArchivo;
import com.stiveen.gestionhospitalaria.exception.IngresoNoEncontradoException;
import com.stiveen.gestionhospitalaria.repository.DocumentoRepository;
import com.stiveen.gestionhospitalaria.repository.IngresoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import java.nio.file.Path;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class DocumentoService {

    private final DocumentoRepository documentoRepository;
    private final IngresoRepository ingresoRepository;

    public DocumentoService(DocumentoRepository documentoRepository,
                            IngresoRepository ingresoRepository) {
        this.documentoRepository = documentoRepository;
        this.ingresoRepository = ingresoRepository;
    }

    @Transactional
    public DocumentoResponse guardar(
            Long ingresoId,
            TipoDocumentoArchivo tipoDocumento,
            String usuario,
            String rolUsuario,
            MultipartFile archivo
    ) {

        try {

            Ingreso ingreso = ingresoRepository.findById(ingresoId)
                    .orElseThrow(() ->
                            new IngresoNoEncontradoException(
                                    "No existe un ingreso con ese número."));

            String carpeta = "uploads/documentos/" + ingresoId;

            Files.createDirectories(Paths.get(carpeta));

// Nombre original
            String nombreOriginal = archivo.getOriginalFilename();

// Extensión
            String extension = "";

            int punto = nombreOriginal.lastIndexOf(".");

            if (punto > 0) {
                extension = nombreOriginal.substring(punto + 1).toUpperCase();
            }

// Nombre físico único
            String nombreFisico = System.currentTimeMillis() + "_" + nombreOriginal;

            String rutaArchivo = carpeta + "/" + nombreFisico;

// Guardar archivo
            Files.copy(
                    archivo.getInputStream(),
                    Paths.get(rutaArchivo),
                    StandardCopyOption.REPLACE_EXISTING
            );

            Documento documento = new Documento();

            documento.setIngreso(ingreso);
            documento.setNombre(nombreOriginal);
            documento.setTipoDocumento(tipoDocumento);
            documento.setRutaArchivo(rutaArchivo);
            documento.setTamanoArchivo(archivo.getSize());
            documento.setExtension(extension);
            documento.setUsuario(usuario);
            documento.setRolUsuario(rolUsuario);
            Documento saved = documentoRepository.save(documento);

            return mapToResponse(saved);

        } catch (Exception e) {

            throw new RuntimeException("Error guardando documento", e);
        }
    }

    public List<DocumentoResponse> listarPorIngreso(Long ingresoId) {

        return documentoRepository.findByIngresoId(ingresoId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private String convertirTamano(long bytes) {

        if (bytes < 1024) {
            return bytes + " B";
        }

        double kb = bytes / 1024.0;

        if (kb < 1024) {
            return String.format("%.1f KB", kb);
        }
        double mb = kb / 1024.0;

        if (mb < 1024) {
            return String.format("%.2f MB", mb);
        }

        double gb = mb / 1024.0;

        return String.format("%.2f GB", gb);

    }

    //Descargar Documentos
    public Resource descargar(Long documentoId) {

        try {
            Documento documento = documentoRepository
                    .findById(documentoId)
                    .orElseThrow(() ->
                            new RuntimeException("Documento no encontrado"));

            Path ruta = Paths.get(documento.getRutaArchivo());
            Resource resource = new UrlResource(ruta.toUri());

            if (!resource.exists()) {
                throw new RuntimeException("El archivo no existe.");
            }

            return resource;

        } catch (Exception e) {

            throw new RuntimeException("No fue posible descargar el documento.");

        }

    }

    private DocumentoResponse mapToResponse(Documento documento) {

        DocumentoResponse dto = new DocumentoResponse();

        dto.setId(documento.getId());

        dto.setNombre(documento.getNombre());

        dto.setTipoDocumento(documento.getTipoDocumento().name());

        dto.setIngresoId(documento.getIngreso().getId());

        dto.setUsuario(documento.getUsuario());

        dto.setRolUsuario(documento.getRolUsuario());

        dto.setExtension(documento.getExtension());

        dto.setTamanoArchivo(documento.getTamanoArchivo());

        dto.setTamano(convertirTamano(documento.getTamanoArchivo()));

        dto.setFechaCreacion(documento.getFechaCreacion());

        return dto;
    }
}