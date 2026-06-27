package com.stiveen.gestionhospitalaria.service;

import com.stiveen.gestionhospitalaria.entity.CorreoDestinatario;
import com.stiveen.gestionhospitalaria.entity.CorreoEnviado;
import com.stiveen.gestionhospitalaria.entity.CorreoJob;
import com.stiveen.gestionhospitalaria.repository.CorreoJobRepository;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.stiveen.gestionhospitalaria.entity.CorreoAdjunto;
import org.springframework.core.io.FileSystemResource;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CorreoJobQueueService {

    private final CorreoJobRepository correoJobRepository;
    private final JavaMailSender mailSender;

    public CorreoJobQueueService(
            CorreoJobRepository correoJobRepository,
            JavaMailSender mailSender) {
        this.correoJobRepository = correoJobRepository;
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

        CorreoEnviado correo = job.getCorreoEnvio().getCorreoEnviado();

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setSubject(correo.getAsunto());
        helper.setText(correo.getMensaje(), false);

        // =========================
        // DESTINATARIOS (FIX REAL)
        // =========================
        if (job.getNumeroEnvio() == 4) {

            String correoEscalamiento =
                    correo.getEpsDestino().getCorreoEscalamiento();

            if (correoEscalamiento == null || correoEscalamiento.isBlank()) {
                throw new IllegalStateException(
                        "La EPS "
                                + correo.getEpsDestino().getNombre()
                                + " no tiene correo de escalamiento configurado");
            }

            helper.setTo(correoEscalamiento);

        } else {

            List<String> destinatarios = correo.getDestinatarios()
                    .stream()
                    .map(CorreoDestinatario::getCorreo)
                    .filter(c -> c != null && !c.isBlank())
                    .toList();

            if (destinatarios.isEmpty()) {
                throw new IllegalStateException(
                        "No hay destinatarios configurados para el correo ID: "
                                + correo.getId());
            }

            helper.setTo(destinatarios.toArray(new String[0]));
        }
        for (CorreoAdjunto adjunto : correo.getAdjuntos()) {

            FileSystemResource archivo =
                    new FileSystemResource(
                            adjunto.getRutaArchivo());

            if (archivo.exists()) {

                helper.addAttachment(
                        adjunto.getNombreArchivo(),
                        archivo);
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