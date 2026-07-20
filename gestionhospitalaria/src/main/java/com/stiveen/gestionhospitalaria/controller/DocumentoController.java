package com.stiveen.gestionhospitalaria.controller;

import com.stiveen.gestionhospitalaria.dto.response.DocumentoResponse;
import com.stiveen.gestionhospitalaria.entity.Documento;
import com.stiveen.gestionhospitalaria.enums.TipoDocumentoArchivo;
import com.stiveen.gestionhospitalaria.security.user.CustomUserDetails;
import com.stiveen.gestionhospitalaria.service.DocumentoService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

            @RequestParam MultipartFile archivo,

            @AuthenticationPrincipal CustomUserDetails usuarioAutenticado

    ) {

        return documentoService.guardar(

                ingresoId,
                tipoDocumento,
                archivo,
                usuarioAutenticado

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

        Documento documento =
                documentoService.obtenerDocumento(id);

        Resource archivo =
                documentoService.descargar(id);


        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\""
                                + documento.getNombre()
                                + "\""
                )
                .body(archivo);

    }


}
