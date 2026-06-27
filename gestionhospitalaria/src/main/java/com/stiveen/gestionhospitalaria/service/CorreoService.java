package com.stiveen.gestionhospitalaria.service;

import com.stiveen.gestionhospitalaria.dto.response.AdjuntoResponse;
import com.stiveen.gestionhospitalaria.dto.response.CorreoEnviadoResponse;
import com.stiveen.gestionhospitalaria.dto.response.CorreoResponse;
import com.stiveen.gestionhospitalaria.dto.response.EnvioResponse;
import com.stiveen.gestionhospitalaria.entity.*;
import com.stiveen.gestionhospitalaria.enums.TipoCorreo;
import com.stiveen.gestionhospitalaria.enums.TipoEnvioCorreo;
import com.stiveen.gestionhospitalaria.repository.*;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CorreoService {

    private final CorreoEnviadoRepository correoEnviadoRepository;
    private final CorreoAdjuntoRepository correoAdjuntoRepository;
    private final CorreoEnvioRepository correoEnvioRepository;
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
            CorreoJobRepository correoJobRepository,
            IngresoRepository ingresoRepository,
            EpsCorreoRepository epsCorreoRepository,
            EpsRepository epsRepository,
            JavaMailSender mailSender,
            CorreoDestinatarioRepository correoDestinatarioRepository){

        this.correoEnviadoRepository = correoEnviadoRepository;
        this.correoAdjuntoRepository = correoAdjuntoRepository;
        this.correoEnvioRepository = correoEnvioRepository;
        this.correoJobRepository = correoJobRepository;
        this.ingresoRepository = ingresoRepository;
        this.epsCorreoRepository = epsCorreoRepository;
        this.epsRepository = epsRepository;
        this.mailSender = mailSender;
        this.correoDestinatarioRepository = correoDestinatarioRepository;

    }

    @Transactional
    public CorreoEnviadoResponse enviarCorreo(
            Long ingresoId,
            Long epsDestinoId,
            TipoCorreo tipoCorreo,
            String usuario,
            String rolUsuario,
            MultipartFile[] archivos){

        Ingreso ingreso = ingresoRepository.findById(ingresoId)
                .orElseThrow(() -> new RuntimeException("Ingreso no encontrado"));

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
        correo.setUsuario(usuario);
        correo.setRolUsuario(rolUsuario);

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

                        correoAdjuntoRepository.save(ca);

                        adjuntos.add(file.getOriginalFilename());
                    }
                }
            }

            mailSender.send(mimeMessage);

        } catch (Exception e) {
            throw new RuntimeException("Error enviando correo", e);
        }

        registrarEnvio(correo, 1, TipoEnvioCorreo.INICIAL, false);
        programarEnvios(correo);
        return mapToResponse(correo, adjuntos);
    }

    private void registrarEnvio(CorreoEnviado correo, int numero, TipoEnvioCorreo tipo, boolean automatico) {

        CorreoEnvio envio = new CorreoEnvio();

        envio.setCorreoEnviado(correo);
        envio.setNumeroEnvio(numero);
        envio.setFechaEnvio(LocalDateTime.now());
        envio.setTipoEnvio(tipo);
        envio.setAutomatico(automatico);

        correoEnvioRepository.save(envio);
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
        envio.setFechaEnvio(fecha);
        envio.setTipoEnvio(TipoEnvioCorreo.PROGRAMADO);
        envio.setAutomatico(true);

        CorreoEnvio envioGuardado = correoEnvioRepository.save(envio);

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
        List<String> destinatarios = correo.getDestinatarios()
                        .stream()
                        .map(CorreoDestinatario::getCorreo)
                        .toList();
        response.setDestinatarios(destinatarios);
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
        // ADJUNTOS (FIX REAL)
        // =========================
        dto.setAdjuntos(
                correo.getAdjuntos()
                        .stream()
                        .map(a -> {
                            AdjuntoResponse r = new AdjuntoResponse();
                            r.setNombreArchivo(a.getNombreArchivo());
                            return r;
                        })
                        .toList()
        );
        // =========================
        // TRAZABILIDAD (ENVÍOS)
        // =========================
        dto.setEnvios(
                correo.getEnvios()
                        .stream()
                        .map(e -> {
                            EnvioResponse r = new EnvioResponse();
                            r.setIntento(e.getNumeroEnvio());
                            r.setFechaEnvio(e.getFechaEnvio());
                            r.setEstado(e.getTipoEnvio().name());
                            return r;
                        })
                        .toList()
        );

        return dto;
    }

}