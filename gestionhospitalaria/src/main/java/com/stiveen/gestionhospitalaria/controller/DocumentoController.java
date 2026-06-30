package com.stiveen.gestionhospitalaria.controller;

import com.stiveen.gestionhospitalaria.dto.response.DocumentoResponse;
import com.stiveen.gestionhospitalaria.enums.TipoDocumentoArchivo;
import com.stiveen.gestionhospitalaria.service.DocumentoService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/documentos")
public class DocumentoController {

    private final DocumentoService documentoService;

    public DocumentoController(DocumentoService documentoService) {
        this.documentoService = documentoService;
    }

    @PostMapping("/ingreso/{ingresoId}")
    public DocumentoResponse guardar(
            @PathVariable Long ingresoId,
            @RequestParam TipoDocumentoArchivo tipoDocumento,
            @RequestParam String usuario,
            @RequestParam String rolUsuario,
            @RequestParam MultipartFile archivo
    ) {
        return documentoService.guardar(
                ingresoId,
                tipoDocumento,
                usuario,
                rolUsuario,
                archivo
        );
    }

    @GetMapping("/ingreso/{ingresoId}")
    public List<DocumentoResponse> listar(@PathVariable Long ingresoId) {
        return documentoService.listarPorIngreso(ingresoId);
    }

    @GetMapping("/{id}/descargar")
    public ResponseEntity<Resource> descargar(
            @PathVariable Long id
    ) {

        Resource archivo = documentoService.descargar(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" +
                                archivo.getFilename() +
                                "\"").body(archivo);

    }
}
