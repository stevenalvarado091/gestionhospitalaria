package com.stiveen.gestionhospitalaria.service;
import com.stiveen.gestionhospitalaria.dto.response.*;
import com.stiveen.gestionhospitalaria.entity.*;
import com.stiveen.gestionhospitalaria.enums.TipoDocumento;
import com.stiveen.gestionhospitalaria.exception.EpsNoEncontradaException;
import com.stiveen.gestionhospitalaria.exception.IngresoDuplicadoException;
import com.stiveen.gestionhospitalaria.exception.IngresoNoEncontradoException;
import com.stiveen.gestionhospitalaria.repository.*;
import com.stiveen.gestionhospitalaria.security.user.CustomUserDetails;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.stiveen.gestionhospitalaria.dto.request.IngresoRequest;
import com.stiveen.gestionhospitalaria.repository.EpsRepository;
import com.stiveen.gestionhospitalaria.repository.PacienteRepository;
import com.stiveen.gestionhospitalaria.dto.response.IngresoListadoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Service
public class IngresoService {

    private final PacienteRepository pacienteRepository;
    private final EpsRepository epsRepository;
    private final IngresoRepository ingresoRepository;
    private final ObservacionRepository observacionRepository;
    private final DocumentoRepository documentoRepository;
    private final AutorizacionRepository autorizacionRepository;
    private final CorreoEnviadoRepository correoEnviadoRepository;

    public IngresoService(
            IngresoRepository ingresoRepository,
            ObservacionRepository observacionRepository,
            DocumentoRepository documentoRepository,
            AutorizacionRepository autorizacionRepository,
            PacienteRepository pacienteRepository,
            EpsRepository epsRepository,
            CorreoEnviadoRepository correoEnviadoRepository
    ){
        this.ingresoRepository = ingresoRepository;
        this.observacionRepository = observacionRepository;
        this.documentoRepository = documentoRepository;
        this.pacienteRepository = pacienteRepository;
        this.epsRepository = epsRepository;
        this.autorizacionRepository = autorizacionRepository;
        this.correoEnviadoRepository = correoEnviadoRepository;
    }


    public Ingreso crearIngreso(IngresoRequest request,
                                CustomUserDetails usuarioAutenticado) {

        Paciente paciente = pacienteRepository
                .findByTipoDocumentoAndNumeroDocumento(request.getTipoDocumento(), request.getNumeroDocumento()).orElse(null);

        if (paciente == null) {

            paciente = new Paciente();
            paciente.setTipoDocumento(request.getTipoDocumento());
            paciente.setNumeroDocumento(request.getNumeroDocumento());
            paciente.setPrimerNombre(request.getPrimerNombre());
            paciente.setSegundoNombre(request.getSegundoNombre());
            paciente.setPrimerApellido(request.getPrimerApellido());
            paciente.setSegundoApellido(request.getSegundoApellido());
            paciente.setFechaNacimiento(request.getFechaNacimiento());
            paciente.setSexo(request.getSexo());

            paciente = pacienteRepository.save(paciente);
        }

        Eps eps = epsRepository.findById(request.getEpsId())
                .orElseThrow(() -> new EpsNoEncontradaException(
                                "La EPS es obligatoria"));


        if (ingresoRepository.findByNumeroIngreso(request.getNumeroIngreso()).isPresent()) {
            throw new IngresoDuplicadoException(
                    "Ya existe un ingreso con ese número");
        }

        Ingreso ingreso = new Ingreso();

        ingreso.setNumeroIngreso(request.getNumeroIngreso());
        Usuario usuario = usuarioAutenticado.getUsuario();

        ingreso.setUsuario(usuario.getNombreCompleto());
        ingreso.setRolUsuario(usuario.getRol().getNombre());
        ingreso.setPaciente(paciente);
        ingreso.setEps(eps);
        ingreso.setFechaIngreso(java.time.LocalDateTime.now());
        ingreso.setEstado(com.stiveen.gestionhospitalaria.enums.EstadoIngreso.ACTIVO);
        ingreso.setActivo(true);

        return ingresoRepository.save(ingreso);
    }

    public IngresoDetalleResponse getDetalleIngreso(Long id) {

        Ingreso ingreso = ingresoRepository.findById(id).orElseThrow(() -> new RuntimeException("Ingreso no encontrado"));

        IngresoDetalleResponse dto = new IngresoDetalleResponse();

        dto.setId(ingreso.getId());
        dto.setNumeroIngreso(ingreso.getNumeroIngreso());
        dto.setFechaCreacion(ingreso.getFechaCreacion());
        dto.setFechaActualizacion(ingreso.getFechaActualizacion());


        // PACIENTE
        dto.setPacienteId(ingreso.getPaciente().getId());
        dto.setNombrePaciente(ingreso.getPaciente().getPrimerNombre() + " " +
                        ingreso.getPaciente().getPrimerApellido());
        dto.setTipoDocumento(ingreso.getPaciente().getTipoDocumento().name());
        dto.setNumeroDocumento(ingreso.getPaciente().getNumeroDocumento());
        dto.setSexo(ingreso.getPaciente().getSexo());
        dto.setFechaNacimiento(ingreso.getPaciente().getFechaNacimiento());
        dto.setUsuario(ingreso.getUsuario());
        dto.setRolUsuario(ingreso.getRolUsuario());
        dto.setEpsId(ingreso.getEps() != null ? ingreso.getEps().getId() : null);
        dto.setNombreEps(ingreso.getEps() != null ? ingreso.getEps().getNombre() : null);


        // ESTADO
        dto.setEstado(ingreso.getEstado().name());

        // OBSERVACIONES
        List<ObservacionResponse> observaciones =
                observacionRepository.findByIngresoIdOrderByFechaCreacionDesc(ingreso.getId())
                        .stream()
                        .map(this::mapObservacion)
                        .toList();

        dto.setObservaciones(observaciones);

        // DOCUMENTOS
        // DOCUMENTOS
        List<DocumentoResponse> documentos =
                documentoRepository.findByIngresoIdOrderByFechaCreacionDesc(ingreso.getId())
                        .stream()
                        .map(d -> {

                            DocumentoResponse doc = new DocumentoResponse();

                            doc.setId(d.getId());
                            doc.setNombre(d.getNombre());
                            doc.setTipoDocumento(d.getTipoDocumento().name());
                            doc.setIngresoId(d.getIngreso().getId());

                            doc.setUsuario(d.getUsuario());
                            doc.setRolUsuario(d.getRolUsuario());
                            doc.setExtension(d.getExtension());
                            doc.setTamanoArchivo(d.getTamanoArchivo());
                            doc.setFechaCreacion(d.getFechaCreacion());

                            return doc;
                        })
                        .toList();

        dto.setDocumentos(documentos);

        List<AutorizacionResponse> autorizaciones =
                autorizacionRepository.findByIngresoOrderByFechaCreacionDesc(ingreso)
                        .stream()
                        .map(this::mapAutorizacion)
                        .toList();

        dto.setAutorizaciones(autorizaciones);

        // CORREOS (vacío por ahora)
        List<CorreoResponse> correos =
                correoEnviadoRepository.findByIngresoIdConDetalle(ingreso.getId())
                        .stream()
                        .map(this::mapCorreo)
                        .toList();

        dto.setCorreos(correos);

        return dto;
    }

    private ObservacionResponse mapObservacion(Observacion observacion) {

        ObservacionResponse dto = new ObservacionResponse();

        dto.setId(observacion.getId());
        dto.setDescripcion(observacion.getDescripcion());
        dto.setUsuario(observacion.getUsuario());
        dto.setRolUsuario(observacion.getRolUsuario());
        dto.setTipoObservacion(observacion.getTipoObservacion().name());
        dto.setIngresoId(observacion.getIngreso().getId());
        dto.setFechaCreacion(observacion.getFechaCreacion());

        return dto;
    }

    private AutorizacionResponse mapAutorizacion(
            Autorizacion autorizacion) {

        AutorizacionResponse dto = new AutorizacionResponse();

        // Información de la autorización
        dto.setId(autorizacion.getId());
        dto.setNumeroAutorizacion(autorizacion.getNumeroAutorizacion());
        dto.setObservacion(autorizacion.getObservacion());

        // Información del documento
        dto.setNombreArchivo(autorizacion.getNombreOriginalArchivo());
        dto.setExtension(autorizacion.getExtension());
        dto.setTamanoArchivo(autorizacion.getTamanoArchivo());

        // Información del ingreso
        dto.setIngresoId(autorizacion.getIngreso().getId());
        dto.setNumeroIngreso(autorizacion.getIngreso().getNumeroIngreso());

        // Trazabilidad
        dto.setUsuario(autorizacion.getUsuario());
        dto.setRolUsuario(autorizacion.getRolUsuario());
        dto.setFechaCreacion(autorizacion.getFechaCreacion());

        return dto;
    }

    private CorreoResponse mapCorreo(CorreoEnviado correo) {

        CorreoResponse dto = new CorreoResponse();

        dto.setId(correo.getId());
        dto.setAsunto(correo.getAsunto());
        dto.setMensaje(correo.getMensaje());
        dto.setIngresoId(correo.getIngreso().getId());
        dto.setUsuario(correo.getUsuario());
        dto.setRolUsuario(correo.getRolUsuario());
        dto.setFechaCreacion(correo.getFechaCreacion());

        // DESTINATARIOS
        if (correo.getDestinatarios() != null) {
            dto.setDestinatario(
                    correo.getDestinatarios()
                            .stream()
                            .map(CorreoDestinatario::getCorreo)
                            .reduce((a, b) -> a + ", " + b)
                            .orElse("")
            );
        }

        // =========================
        // ADJUNTOS (FIX REAL)
        // =========================
        if (correo.getAdjuntos() != null) {

            dto.setAdjuntos(

                    correo.getAdjuntos()
                            .stream()
                            .map(a -> {

                                AdjuntoResponse adjunto = new AdjuntoResponse();

                                adjunto.setId(a.getId());
                                adjunto.setNombreArchivo(a.getNombreArchivo());
                                adjunto.setExtension(a.getExtension());
                                adjunto.setTamanoArchivo(a.getTamanoArchivo());
                                adjunto.setTamano(a.getTamano());

                                return adjunto;

                            })
                            .toList()

            );

        }

        // =========================
        // TRAZABILIDAD ENVÍOS
        // =========================
        if (correo.getEnvios() != null) {
            dto.setEnvios(
                    correo.getEnvios()
                            .stream()
                            .map(e -> {
                                EnvioResponse r = new EnvioResponse();

                                r.setId(e.getId());

                                r.setNumeroEnvio(e.getNumeroEnvio());
                                r.setIntento(e.getNumeroEnvio());

                                r.setTipoEnvio(e.getTipoEnvio().name());                                r.setEstado(e.getTipoEnvio().name());

                                r.setFechaProgramada(e.getFechaProgramada());
                                r.setFechaEjecutada(e.getFechaEjecutada());

                                r.setAsunto(e.getAsunto());
                                r.setMensaje(e.getMensaje());
                                String destinatarios = e.getDestinatarios();

                                if (destinatarios == null || destinatarios.isBlank()) {

                                    r.setDestinatarios(List.of());

                                } else {

                                    r.setDestinatarios(
                                            Arrays.stream(destinatarios.split(";"))
                                                    .map(String::trim)
                                                    .filter(s -> !s.isEmpty())
                                                    .toList()
                                    );

                                }

                                r.setAdjuntos(
                                        e.getAdjuntos()
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
                                r.setUsuario(e.getUsuario());
                                r.setRolUsuario(e.getRolUsuario());

                                r.setAutomatico(e.getAutomatico());
                                r.setActivo(e.getActivo());
                                r.setEscalamiento(e.getEscalamiento());

                                return r;
                            })
                            .toList()
            );
        }

        return dto;
    }

    public List<IngresoListadoResponse> listarTodos() {

        return ingresoRepository
                .findAll(
                        Sort.by(
                                Sort.Direction.DESC,
                                "fechaActualizacion"
                        )
                )
                .stream()
                .map(this::mapListado)
                .toList();
    }

    public IngresoListadoResponse buscarPorNumeroIngreso(String numeroIngreso) {

        Ingreso ingreso = ingresoRepository.findByNumeroIngreso(numeroIngreso)
                .orElseThrow(() ->
                        new IngresoNoEncontradoException("Ingreso no encontrado"));

        return mapListado(ingreso);
    }

    private IngresoListadoResponse mapListado(
            Ingreso ingreso) {

        IngresoListadoResponse dto = new IngresoListadoResponse();

        dto.setId(ingreso.getId());
        dto.setPacienteId(ingreso.getPaciente().getId());
        dto.setNumeroIngreso(ingreso.getNumeroIngreso());
        dto.setNombrePaciente(nombreCompleto(ingreso.getPaciente()));
        dto.setTipoDocumento(ingreso.getPaciente()
                        .getTipoDocumento()
                        .name());
        dto.setNumeroDocumento(ingreso.getPaciente().getNumeroDocumento());
        dto.setEpsId(ingreso.getEps() != null ? ingreso.getEps().getId() : null);
        dto.setNombreEps(ingreso.getEps() != null ? ingreso.getEps().getNombre() : null);
        dto.setEstado(ingreso.getEstado().name());
        dto.setFechaIngreso(ingreso.getFechaIngreso());
        dto.setUsuario(ingreso.getUsuario());
        dto.setRolUsuario(ingreso.getRolUsuario());

        return dto;
    }

    private String nombreCompleto(Paciente paciente) {

        return String.join(" ",
                        paciente.getPrimerNombre(),
                        paciente.getSegundoNombre() == null ? "" : paciente.getSegundoNombre(),
                        paciente.getPrimerApellido(),
                        paciente.getSegundoApellido() == null ? "" : paciente.getSegundoApellido())
                .replaceAll("\\s+", " ")
                .trim();
    }

    public IngresoDetalleResponse getDetalleIngresoPorNumeroIngreso(
            String numeroIngreso) {

        Ingreso ingreso = ingresoRepository
                .findByNumeroIngreso(numeroIngreso)
                .orElseThrow(() ->
                        new IngresoNoEncontradoException("Ingreso no encontrado"));

        return getDetalleIngreso(ingreso.getId());
    }

    public List<IngresoListadoResponse> buscarPorDocumento(
            TipoDocumento tipoDocumento,
            String numeroDocumento) {

        return ingresoRepository
                .buscarPorDocumento(tipoDocumento, numeroDocumento)
                .stream()
                .map(this::mapListado)
                .toList();
    }


    public List<TimelineResponse> obtenerTimeline(Long ingresoId) {

        Ingreso ingreso = ingresoRepository.findById(ingresoId)
                .orElseThrow(() ->
                        new IngresoNoEncontradoException("Ingreso no encontrado"));

        List<TimelineResponse> timeline = new ArrayList<>();

        // EVENTO CREACIÓN INGRESO

        TimelineResponse ingresoEvento = new TimelineResponse();

        ingresoEvento.setFecha(ingreso.getFechaCreacion());
        ingresoEvento.setTipo("INGRESO");
        ingresoEvento.setUsuario(ingreso.getUsuario());
        ingresoEvento.setDescripcion("Ingreso " + ingreso.getNumeroIngreso() + " creado");

        timeline.add(ingresoEvento);

        // OBSERVACIONES

        observacionRepository.findByIngresoIdOrderByFechaCreacionDesc(ingresoId)
                .forEach(obs -> {

                    TimelineResponse evento = new TimelineResponse();

                    evento.setFecha(obs.getFechaCreacion());
                    evento.setTipo("OBSERVACION");
                    evento.setUsuario(obs.getUsuario());
                    evento.setDescripcion(obs.getDescripcion());

                    timeline.add(evento);
                });

        // DOCUMENTOS

        documentoRepository.findByIngresoIdOrderByFechaCreacionDesc(ingresoId)
                .forEach(doc -> {

                    TimelineResponse evento = new TimelineResponse();

                    evento.setFecha(doc.getFechaCreacion());
                    evento.setTipo("DOCUMENTO");
                    evento.setUsuario(doc.getUsuario());
                    evento.setDescripcion("Documento cargado: " + doc.getNombre());

                    timeline.add(evento);
                });

        // CORREOS

        correoEnviadoRepository.findByIngresoIdConDetalle(ingresoId)
                .forEach(correo -> {

                    TimelineResponse evento = new TimelineResponse();

                    evento.setFecha(correo.getFechaCreacion());
                    evento.setTipo("CORREO");
                    evento.setUsuario(correo.getUsuario());
                    evento.setDescripcion("Correo enviado: " + correo.getAsunto());

                    timeline.add(evento);
                });

        // AUTORIZACIONES

        autorizacionRepository
                .findByIngresoOrderByFechaCreacionDesc(ingreso)
                .forEach(auto -> {

                    TimelineResponse evento = new TimelineResponse();

                    evento.setFecha(auto.getFechaCreacion());
                    evento.setTipo("AUTORIZACION");
                    evento.setUsuario(auto.getUsuario());

                    StringBuilder descripcion = new StringBuilder();

                    if (auto.getNumeroAutorizacion() != null) {
                        descripcion.append("Número: ")
                                    .append(auto.getNumeroAutorizacion());
                    }

                    if (auto.getObservacion() != null) {

                        if (descripcion.length() > 0) {
                            descripcion.append(" | ");
                        }

                        descripcion.append("Observación: ")
                                .append(auto.getObservacion());
                    }

                    if (auto.getNombreOriginalArchivo() != null) {

                        if (descripcion.length() > 0) {
                            descripcion.append(" | ");
                        }

                        descripcion.append("Documento: ")
                                .append(auto.getNombreOriginalArchivo());
                    }

                    evento.setDescripcion(descripcion.toString());

                    timeline.add(evento);

                });

        timeline.sort(
                Comparator.comparing(
                        TimelineResponse::getFecha,
                        Comparator.reverseOrder()
                )
        );

        return timeline;
    }


    public Page<IngresoListadoResponse> listarPaginado(int pagina, int tamanio) {

        Pageable pageable = PageRequest.of(pagina, tamanio,
                Sort.by(Sort.Direction.DESC, "fechaActualizacion"));

        return ingresoRepository.findAll(pageable).map(this::mapListado);
    }

    public List<IngresoListadoResponse> buscarPorNombre(String nombre){

        return ingresoRepository
                .buscarPorNombre(nombre)
                .stream()
                .map(this::mapListado)
                .toList();
    }
}