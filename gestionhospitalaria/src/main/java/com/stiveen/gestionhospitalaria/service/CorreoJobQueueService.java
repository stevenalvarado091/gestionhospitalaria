package com.stiveen.gestionhospitalaria.service;

import com.stiveen.gestionhospitalaria.entity.*;
import com.stiveen.gestionhospitalaria.repository.CorreoEnvioRepository;
import com.stiveen.gestionhospitalaria.repository.CorreoJobRepository;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.core.io.FileSystemResource;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CorreoJobQueueService {

    private final CorreoJobRepository correoJobRepository;
    private final CorreoEnvioRepository correoEnvioRepository;
    private final JavaMailSender mailSender;


    public CorreoJobQueueService(
            CorreoJobRepository correoJobRepository,
            CorreoEnvioRepository correoEnvioRepository,
            JavaMailSender mailSender

    ) {
        this.correoJobRepository = correoJobRepository;
        this.correoEnvioRepository = correoEnvioRepository;
        this.mailSender = mailSender;
    }

    // =========================
    // SCHEDULER
    // =========================
    @Scheduled(fixedRate = 30000)
    @Transactional
    public void procesarColaDeCorreos() {

        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime limiteBloqueo = ahora.minusMinutes(10);

        List<CorreoJob> jobs = correoJobRepository.findPendientes(ahora, limiteBloqueo);

        for (CorreoJob job : jobs) {
            try {

                bloquearJob(job, ahora);
                ejecutarJob(job);
                finalizarJob(job);

            } catch (Exception e) {
                manejarFallo(job, ahora, e);
            }
        }
    }

    // =========================
    // BLOQUEO
    // =========================
    private void bloquearJob(CorreoJob job, LocalDateTime ahora) {

        job.setEnProceso(true);
        job.setBloqueadoEn(ahora);

        correoJobRepository.save(job);
    }

    // =========================
    // EJECUCIÓN REAL
    // =========================
    private void ejecutarJob(CorreoJob job) throws Exception {

        CorreoEnvio envio = job.getCorreoEnvio();

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setSubject(envio.getAsunto());
        helper.setText(envio.getMensaje(), false);


        String[] destinatarios = envio.getDestinatarios()
                .split(";");

        helper.setTo(destinatarios);

        // =========================
        // DESTINATARIOS (FIX REAL)
        // =========================
//        if (job.getNumeroEnvio() == 4) {
//
//            String correoEscalamiento =
//                    envio.getCorreoEnviado()
//                            .getEpsDestino()
//                            .getCorreoEscalamiento();
//
//            if (correoEscalamiento == null || correoEscalamiento.isBlank()) {
//                throw new IllegalStateException(
//                        "La EPS "
//                                + envio.getCorreoEnviado()
//                                .getEpsDestino()
//                                .getNombre()
//                                + " no tiene correo de escalamiento configurado");
//            }
//
//            helper.setTo(correoEscalamiento);
//
//        } else {
//
//            List<String> destinatarios =
//                    envio.getCorreoEnviado()
//                            .getDestinatarios()
//                            .stream()
//                            .map(CorreoDestinatario::getCorreo)
//                            .toList();
//
//            if (destinatarios.isEmpty()) {
//                throw new IllegalStateException(
//                        "No hay destinatarios configurados para el envío ID: "
//                                + envio.getId());
//            }
//
//            helper.setTo(destinatarios.toArray(new String[0]));
//        }
        for (CorreoEnvioAdjunto adjunto : envio.getAdjuntos()) {

            FileSystemResource archivo =
                    new FileSystemResource(adjunto.getRutaArchivo());

            if (archivo.exists()) {

                helper.addAttachment(
                        adjunto.getNombreArchivo(),
                        archivo
                );
            }
        }

        mailSender.send(message);
    }

    // =========================
    // FINALIZAR OK
    // =========================
    private void finalizarJob(CorreoJob job) {

        job.setEjecutado(true);
        job.setEnProceso(false);
        job.setBloqueadoEn(null);
        job.setSiguienteIntento(null);
        job.getCorreoEnvio().setFechaEjecutada(LocalDateTime.now());
        correoEnvioRepository.save(job.getCorreoEnvio());

        correoJobRepository.save(job);
    }

    // =========================
    // FALLAS + RETRY
    // =========================
    private void manejarFallo(CorreoJob job, LocalDateTime ahora, Exception e) {

        int intentos = job.getIntentos() == null ? 0 : job.getIntentos();
        int max = job.getMaxIntentos() == null ? 3 : job.getMaxIntentos();

        intentos++;
        job.setIntentos(intentos);
        job.setUltimoError(e.getMessage());

        if (intentos >= max) {

            job.setEjecutado(true);
            job.setEnProceso(false);
            job.setBloqueadoEn(null);

        } else {

            int minutos = (int) Math.pow(2, intentos);

            job.setSiguienteIntento(ahora.plusMinutes(minutos));

            job.setEnProceso(false);
            job.setBloqueadoEn(null);
        }

        correoJobRepository.save(job);

        System.err.println("Fallo job ID " + job.getId() + ": " + e.getMessage());
    }
}