package com.stiveen.gestionhospitalaria.service;

import com.stiveen.gestionhospitalaria.dto.response.AdjuntoResponse;
import com.stiveen.gestionhospitalaria.dto.response.CorreoEnviadoResponse;
import com.stiveen.gestionhospitalaria.dto.response.CorreoResponse;
import com.stiveen.gestionhospitalaria.dto.response.EnvioResponse;
import com.stiveen.gestionhospitalaria.entity.*;
import com.stiveen.gestionhospitalaria.enums.TipoCorreo;
import com.stiveen.gestionhospitalaria.enums.TipoEnvioCorreo;
import com.stiveen.gestionhospitalaria.exception.IngresoNoEncontradoException;
import com.stiveen.gestionhospitalaria.repository.*;
import com.stiveen.gestionhospitalaria.security.user.CustomUserDetails;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.stiveen.gestionhospitalaria.entity.CorreoDestinatario;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.nio.file.Path;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CorreoService {

    private final CorreoEnviadoRepository correoEnviadoRepository;
    private final CorreoAdjuntoRepository correoAdjuntoRepository;
    private final CorreoEnvioRepository correoEnvioRepository;
    private final CorreoEnvioAdjuntoRepository correoEnvioAdjuntoRepository;
    private final CorreoJobRepository correoJobRepository;
    private final IngresoRepository ingresoRepository;
    private final EpsCorreoRepository epsCorreoRepository;
    private final EpsRepository epsRepository;
    private final JavaMailSender mailSender;
    private final CorreoDestinatarioRepository correoDestinatarioRepository;

    public CorreoService(
            CorreoEnviadoRepository correoEnviadoRepository,
            CorreoAdjuntoRepository correoAdjuntoRepository,
            CorreoEnvioRepository correoEnvioRepository,
            CorreoEnvioAdjuntoRepository correoEnvioAdjuntoRepository,
            CorreoJobRepository correoJobRepository,
            IngresoRepository ingresoRepository,
            EpsCorreoRepository epsCorreoRepository,
            EpsRepository epsRepository,
            JavaMailSender mailSender,
            CorreoDestinatarioRepository correoDestinatarioRepository){

        this.correoEnviadoRepository = correoEnviadoRepository;
        this.correoAdjuntoRepository = correoAdjuntoRepository;
        this.correoEnvioRepository = correoEnvioRepository;
        this.correoEnvioAdjuntoRepository = correoEnvioAdjuntoRepository;
        this.correoJobRepository = correoJobRepository;
        this.ingresoRepository = ingresoRepository;
        this.epsCorreoRepository = epsCorreoRepository;
        this.epsRepository = epsRepository;
        this.mailSender = mailSender;
        this.correoDestinatarioRepository = correoDestinatarioRepository;

    }

    private void registrarEnvio(
            CorreoEnviado correo,
            int numero,
            TipoEnvioCorreo tipo,
            boolean automatico,
            List<String> destinatarios,
            boolean escalamiento) {

        CorreoEnvio envio = new CorreoEnvio();

        envio.setCorreoEnviado(correo);
        envio.setNumeroEnvio(numero);

        envio.setFechaEjecutada(LocalDateTime.now());

        envio.setTipoEnvio(tipo);
        envio.setAutomatico(automatico);

        // Snapshot
        envio.setAsunto(correo.getAsunto());
        envio.setMensaje(correo.getMensaje());
        envio.setUsuario(correo.getUsuario());
        envio.setRolUsuario(correo.getRolUsuario());

        envio.setDestinatarios(
                String.join("; ", destinatarios)
        );

        envio.setEscalamiento(escalamiento);

        CorreoEnvio envioGuardado = correoEnvioRepository.save(envio);


        copiarAdjuntos(correo, envioGuardado);
    }

    @Transactional
    public CorreoEnviadoResponse enviarCorreo(
            Long ingresoId,
            Long epsDestinoId,
            TipoCorreo tipoCorreo,
            MultipartFile[] archivos,
            CustomUserDetails usuarioAutenticado){

        Ingreso ingreso = ingresoRepository.findById(ingresoId)
                .orElseThrow(() -> new IngresoNoEncontradoException(
                        "No existe un ingreso con ese número."));

        Eps epsDestino = epsRepository.findById(epsDestinoId)
                .orElseThrow(() ->
                        new RuntimeException("EPS destino no encontrada"));

        Paciente paciente = ingreso.getPaciente();
        String asunto = construirAsunto(tipoCorreo, paciente);
        String mensaje = construirMensaje(tipoCorreo, ingreso);

        List<String> destinatarios = epsCorreoRepository.findEmailsByEpsId(epsDestinoId);

        if (destinatarios == null || destinatarios.isEmpty()) {
            throw new RuntimeException("No hay correos configurados para la EPS del ingreso");
        }

        CorreoEnviado correo = new CorreoEnviado();
        correo.setIngreso(ingreso);
        correo.setEpsDestino(epsDestino);
        correo.setAsunto(asunto);
        correo.setMensaje(mensaje);

        Usuario usuario = usuarioAutenticado.getUsuario();

        correo.setUsuario(usuario.getNombreCompleto());
        correo.setRolUsuario(usuario.getRol().getNombre());

        correo = correoEnviadoRepository.save(correo);

        for (String email : destinatarios) {

            CorreoDestinatario cd = new CorreoDestinatario();

            cd.setCorreo(email);
            cd.setCorreoEnviado(correo);

            correoDestinatarioRepository.save(cd);
        }

        List<String> adjuntos = new ArrayList<>();

        try {

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(destinatarios.toArray(new String[0]));
            helper.setSubject(asunto);
            helper.setText(mensaje, false);

            String carpetaAdjuntos = "uploads/correos/" + correo.getId();

            Files.createDirectories(Paths.get(carpetaAdjuntos));

            if (archivos != null) {

                for (MultipartFile file : archivos) {

                    if (file != null && !file.isEmpty()) {

                        String rutaArchivo = carpetaAdjuntos + "/" + file.getOriginalFilename();
                        Files.copy(file.getInputStream(), Paths.get(rutaArchivo), StandardCopyOption.REPLACE_EXISTING);
                        helper.addAttachment(file.getOriginalFilename(), new ByteArrayResource(file.getBytes()));

                        CorreoAdjunto ca = new CorreoAdjunto();

                        ca.setCorreo(correo);
                        ca.setNombreArchivo(file.getOriginalFilename());
                        ca.setRutaArchivo(rutaArchivo);
                        String nombre = file.getOriginalFilename();
                        String extension = "";

                        if (nombre != null && nombre.contains(".")) {
                            extension = nombre.substring(nombre.lastIndexOf('.') + 1).toUpperCase();
                        }

                        ca.setExtension(extension);
                        ca.setTamanoArchivo(file.getSize());

                        double kb = file.getSize() / 1024.0;

                        String tamano;

                        if (kb >= 1024) {
                            tamano = String.format("%.2f MB", kb / 1024);

                        } else {

                            tamano = String.format("%.2f KB", kb);}

                        ca.setTamano(tamano);
                        correoAdjuntoRepository.save(ca);
                        adjuntos.add(file.getOriginalFilename());
                    }
                }
            }

            mailSender.send(mimeMessage);

        } catch (Exception e) {
            throw new RuntimeException("Error enviando correo", e);
        }

        registrarEnvio(correo, 1, TipoEnvioCorreo.INICIAL,
                false, destinatarios, false);
        programarEnvios(correo);
        return mapToResponse(correo, adjuntos);
    }


    private void copiarAdjuntos(CorreoEnviado correo, CorreoEnvio envio) {

        for (CorreoAdjunto adjunto : correo.getAdjuntos()) {

            CorreoEnvioAdjunto copia = new CorreoEnvioAdjunto();

            copia.setCorreoEnvio(envio);

            copia.setNombreArchivo(adjunto.getNombreArchivo());
            copia.setRutaArchivo(adjunto.getRutaArchivo());
            copia.setExtension(adjunto.getExtension());
            copia.setTamanoArchivo(adjunto.getTamanoArchivo());
            copia.setTamano(adjunto.getTamano());

            correoEnvioAdjuntoRepository.save(copia);
        }
    }


    private void programarEnvios(CorreoEnviado correo) {

        LocalDateTime base = LocalDateTime.now();

        crearEnvio(correo, 2, base.plusMinutes(1)); //plusHours
        crearEnvio(correo, 3, base.plusMinutes(2));
        crearEnvio(correo, 4, base.plusMinutes(3));
    }

    private void crearEnvio(CorreoEnviado correo, int numero, LocalDateTime fecha) {

        // 1. Crear el envío primero
        CorreoEnvio envio = new CorreoEnvio();

        envio.setCorreoEnviado(correo);
        envio.setNumeroEnvio(numero);
        envio.setFechaProgramada(fecha);
        envio.setTipoEnvio(TipoEnvioCorreo.PROGRAMADO);
        envio.setAutomatico(true);

        envio.setAsunto(correo.getAsunto());
        envio.setMensaje(correo.getMensaje());
        envio.setUsuario(correo.getUsuario());
        envio.setRolUsuario(correo.getRolUsuario());

        String destinatarios;

        if (numero == 4) {

            destinatarios = correo.getEpsDestino().getCorreoEscalamiento();

        } else {

            destinatarios = correoDestinatarioRepository
                    .findByCorreoEnviadoId(correo.getId())
                    .stream()
                    .map(CorreoDestinatario::getCorreo)
                    .collect(Collectors.joining("; "));
        }

        envio.setDestinatarios(destinatarios);

        envio.setEscalamiento(numero == 4);
        envio.setActivo(true);

        CorreoEnvio envioGuardado = correoEnvioRepository.save(envio);
        copiarAdjuntos(correo, envioGuardado);

        // 2. Evitar duplicados de JOBS
        boolean existe = correoJobRepository.existsByCorreoEnvioIdAndNumeroEnvio(
                envioGuardado.getId(), numero);

        if (!existe) {

            CorreoJob job = new CorreoJob();

            job.setCorreoEnvio(envioGuardado);
            job.setProgramadoPara(fecha);
            job.setNumeroEnvio(numero);
            job.setEjecutado(false);
            job.setEnProceso(false);
            job.setIntentos(0);
            job.setMaxIntentos(3);

            correoJobRepository.save(job);
        }
    }

    private CorreoEnviadoResponse mapToResponse(CorreoEnviado correo,
            List<String> adjuntos) {

        CorreoEnviadoResponse response = new CorreoEnviadoResponse();

        response.setId(correo.getId());
        response.setAsunto(correo.getAsunto());
        response.setMensaje(correo.getMensaje());
        response.setUsuario(correo.getUsuario());
        response.setRolUsuario(correo.getRolUsuario());
        response.setNumeroIngreso(correo.getIngreso().getNumeroIngreso());
        response.setFechaCreacion(correo.getFechaCreacion());
        response.setDestinatarios(
                correo.getDestinatarios()
                        .stream()
                        .map(CorreoDestinatario::getCorreo)
                        .toList()
        );

        response.setAdjuntos(adjuntos);

        return response;

    }

    private String construirAsunto(TipoCorreo tipoCorreo, Paciente paciente) {

        return (tipoCorreo.name()
                + " "
                + paciente.getTipoDocumento()
                + " "
                + paciente.getNumeroDocumento()
                + " "
                + nombreCompleto(paciente)).toUpperCase();
    }

    private String construirMensaje(TipoCorreo tipoCorreo, Ingreso ingreso) {

        Paciente paciente = ingreso.getPaciente();

        return """
            Buen día,

            Se remite %s para el paciente:

            Tipo Documento: %s
            Número Documento: %s
            Nombre: %s
            Número Ingreso: %s

            Se adjuntan los soportes correspondientes.

            Cordialmente.
            """.formatted(
                tipoCorreo.name(),
                paciente.getTipoDocumento(),
                paciente.getNumeroDocumento(),
                nombreCompleto(paciente).trim().toUpperCase(),
                ingreso.getNumeroIngreso());
    }

    private String nombreCompleto(Paciente paciente) {

        return String.join(
                " ",
                paciente.getPrimerNombre(),
                paciente.getSegundoNombre() == null ? "" : paciente.getSegundoNombre(),
                paciente.getPrimerApellido(),
                paciente.getSegundoApellido() == null ? "" : paciente.getSegundoApellido()
        ).replaceAll("\\s+", " ").trim();
    }

    public List<CorreoResponse> listarPorIngreso(Long ingresoId) {

        return correoEnviadoRepository.findByIngresoIdConDetalle(ingresoId)
                .stream()
                .map(this::mapCorreoResponse)
                .toList();
    }

    private CorreoResponse mapCorreoResponse(CorreoEnviado correo) {

        CorreoResponse dto = new CorreoResponse();

        dto.setId(correo.getId());
        dto.setAsunto(correo.getAsunto());
        dto.setMensaje(correo.getMensaje());
        dto.setIngresoId(correo.getIngreso().getId());

        dto.setUsuario(correo.getUsuario());
        dto.setRolUsuario(correo.getRolUsuario());
        dto.setFechaCreacion(correo.getFechaCreacion());

        // =========================
        // DESTINATARIOS
        // =========================
        String destinatarios = correo.getDestinatarios()
                .stream()
                .map(CorreoDestinatario::getCorreo)
                .reduce((a, b) -> a + "; " + b)
                .orElse("");

        dto.setDestinatario(destinatarios);

        // =========================
        // ADJUNTOS
        // =========================
        dto.setAdjuntos(
                correo.getAdjuntos()
                        .stream()
                        .map(a -> {

                            AdjuntoResponse r = new AdjuntoResponse();

                            r.setId(a.getId());
                            r.setNombreArchivo(a.getNombreArchivo());
                            r.setExtension(a.getExtension());
                            r.setTamanoArchivo(a.getTamanoArchivo());
                            r.setTamano(a.getTamano());

                            return r;

                        })
                        .toList()
        );

        // =========================
        // TRAZABILIDAD (ENVÍOS)
        // =========================
        List<EnvioResponse> envios = new ArrayList<>();

        for (CorreoEnvio envio : correo.getEnvios()
                .stream()
                .sorted((a, b) -> Integer.compare(a.getNumeroEnvio(), b.getNumeroEnvio()))
                .toList()) {

            EnvioResponse response = new EnvioResponse();

            response.setId(envio.getId());
            response.setNumeroEnvio(envio.getNumeroEnvio());
            response.setTipoEnvio(envio.getTipoEnvio().name());
            response.setActivo(envio.getActivo());
            response.setFechaProgramada(envio.getFechaProgramada());
            response.setFechaEjecutada(envio.getFechaEjecutada());
            response.setAutomatico(envio.getAutomatico());

            // ===== Ahora salen del snapshot =====
            response.setUsuario(envio.getUsuario());
            response.setRolUsuario(envio.getRolUsuario());
            response.setAsunto(envio.getAsunto());
            response.setMensaje(envio.getMensaje());

            if (destinatarios == null || destinatarios.isBlank()) {

                response.setDestinatarios(List.of());

            } else {

                response.setDestinatarios(
                        Arrays.stream(destinatarios.split(";"))
                                .map(String::trim)
                                .filter(s -> !s.isEmpty())
                                .toList()
                );

            }

            response.setAdjuntos(
                    envio.getAdjuntos()
                            .stream()
                            .map(a -> {

                                AdjuntoResponse ar = new AdjuntoResponse();

                                ar.setId(a.getId());
                                ar.setNombreArchivo(a.getNombreArchivo());
                                ar.setExtension(a.getExtension());
                                ar.setTamanoArchivo(a.getTamanoArchivo());
                                ar.setTamano(a.getTamano());

                                return ar;

                            })
                            .toList()
            );


            if (envio.getTipoEnvio() == TipoEnvioCorreo.INICIAL) {

                response.setEstado("ENVIADO");
                response.setTitulo("Envío inicial");
                response.setDescripcion("Enviado");
                response.setEscalamiento(false);

            } else {

                if (envio.getFechaEjecutada() != null) {
                    response.setEstado("ENVIADO");
                } else {
                    response.setEstado("PROGRAMADO");
                }

                response.setTitulo(
                        envio.getNumeroEnvio() == 4
                                ? "Escalamiento automático"
                                : "Reenvío automático #" + envio.getNumeroEnvio()
                );

                response.setDescripcion(
                        envio.getNumeroEnvio() == 4
                                ? "Correo enviado al correo de escalamiento de la EPS."
                                : "Correo reenviado automáticamente."
                );

                response.setEscalamiento(envio.getNumeroEnvio() == 4);
            }

            envios.add(response);
        }

        dto.setEnvios(envios);

        return dto;
    }


    public ResponseEntity<Resource> descargarAdjunto(Long id) {

        try {

            CorreoAdjunto adjunto =
                    correoAdjuntoRepository.findById(id)
                            .orElseThrow(() ->
                                    new RuntimeException("Adjunto no encontrado"));

            Path path = Paths.get(adjunto.getRutaArchivo());

            Resource resource = new UrlResource(path.toUri());

            return ResponseEntity.ok()

                    .contentType(MediaType.APPLICATION_OCTET_STREAM)

                    .header(
                            HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" +
                                    adjunto.getNombreArchivo() + "\"")

                    .body(resource);

        } catch (Exception e) {

            e.printStackTrace();

            throw new RuntimeException(
                    "Error descargando adjunto", e);

        }

    }

}