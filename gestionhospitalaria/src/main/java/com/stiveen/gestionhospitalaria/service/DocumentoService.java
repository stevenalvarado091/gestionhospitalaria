package com.stiveen.gestionhospitalaria.service;

import com.stiveen.gestionhospitalaria.dto.response.DocumentoResponse;
import com.stiveen.gestionhospitalaria.entity.Documento;
import com.stiveen.gestionhospitalaria.entity.Ingreso;
import com.stiveen.gestionhospitalaria.enums.TipoDocumentoArchivo;
import com.stiveen.gestionhospitalaria.repository.DocumentoRepository;
import com.stiveen.gestionhospitalaria.repository.IngresoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;

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
                    .orElseThrow(() -> new RuntimeException("Ingreso no encontrado"));

            String carpeta = "uploads/documentos/" + ingresoId;

            Files.createDirectories(Paths.get(carpeta));

            String rutaArchivo = carpeta + "/" + archivo.getOriginalFilename();

            Files.copy(archivo.getInputStream(),
                    Paths.get(rutaArchivo),
                    StandardCopyOption.REPLACE_EXISTING);

            Documento documento = new Documento();

            documento.setIngreso(ingreso);
            documento.setNombre(archivo.getOriginalFilename());
            documento.setTipoDocumento(tipoDocumento);
            documento.setRutaArchivo(rutaArchivo);
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

    private DocumentoResponse mapToResponse(Documento documento) {

        DocumentoResponse dto = new DocumentoResponse();

        dto.setId(documento.getId());
        dto.setNombre(documento.getNombre());
        dto.setTipoDocumento(documento.getTipoDocumento().name());
        dto.setRutaArchivo(documento.getRutaArchivo());
        dto.setIngresoId(documento.getIngreso().getId());
        dto.setUsuario(documento.getUsuario());
        dto.setRolUsuario(documento.getRolUsuario());

        return dto;
    }
}