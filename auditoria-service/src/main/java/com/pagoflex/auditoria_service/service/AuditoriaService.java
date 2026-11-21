package com.pagoflex.auditoria_service.service;

import com.pagoflex.auditoria_service.dto.EventoAuditoriaRequest;
import com.pagoflex.auditoria_service.model.EventoAuditoria;
import com.pagoflex.auditoria_service.repository.EventoAuditoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditoriaService {
    private final EventoAuditoriaRepository repositorio;

    public List<EventoAuditoria> listar(Long actorUsuarioId) {
        return actorUsuarioId == null
                ? repositorio.findAllByOrderByFechaMillisDesc()
                : repositorio.findByActorUsuarioIdOrderByFechaMillisDesc(actorUsuarioId);
    }

    public EventoAuditoria obtener(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento no encontrado"));
    }

    public EventoAuditoria registrar(EventoAuditoriaRequest datos) {
        if (datos == null || vacio(datos.entidad()) || vacio(datos.accion()) || vacio(datos.detalle())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Entidad, acción y detalle son obligatorios");
        }
        EventoAuditoria evento = EventoAuditoria.builder()
                .actorUsuarioId(datos.actorUsuarioId())
                .entidad(datos.entidad().trim())
                .entidadId(datos.entidadId())
                .accion(datos.accion().trim())
                .detalle(datos.detalle().trim())
                .fechaMillis(System.currentTimeMillis())
                .cuentaDemoId(datos.cuentaDemoId())
                .esDemo(Boolean.TRUE.equals(datos.esDemo()))
                .build();
        return repositorio.save(evento);
    }

    private boolean vacio(String texto) {
        return texto == null || texto.isBlank();
    }
}
