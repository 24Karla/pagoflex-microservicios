package com.pagoflex.recomendaciones_service.service;

import com.pagoflex.recomendaciones_service.dto.RecomendacionRequest;
import com.pagoflex.recomendaciones_service.model.RecomendacionOrganizacion;
import com.pagoflex.recomendaciones_service.repository.RecomendacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RecomendacionService {
    private static final Set<String> ESTADOS = Set.of("Recibida", "Contactada", "En evaluación", "Se unió", "Descartada");
    private final RecomendacionRepository repositorio;

    public List<RecomendacionOrganizacion> listar(Long agenteId) {
        return agenteId == null ? repositorio.findAll()
                : repositorio.findByAgenteIdOrderByFechaCreacionMillisDesc(agenteId);
    }

    public RecomendacionOrganizacion obtener(Long id) { return buscar(id); }

    public RecomendacionOrganizacion crear(RecomendacionRequest datos) {
        validar(datos);
        long ahora = System.currentTimeMillis();
        return repositorio.save(construir(datos, null, ahora, ahora));
    }

    public RecomendacionOrganizacion actualizar(Long id, RecomendacionRequest datos) {
        RecomendacionOrganizacion existente = buscar(id);
        validar(datos);
        return repositorio.save(construir(datos, existente.getId(), existente.getFechaCreacionMillis(),
                System.currentTimeMillis()));
    }

    public void eliminar(Long id) { repositorio.delete(buscar(id)); }

    private RecomendacionOrganizacion construir(RecomendacionRequest datos, Long id, Long creada, Long actualizada) {
        return RecomendacionOrganizacion.builder().id(id).agenteId(datos.agenteId())
                .organizacion(datos.organizacion().trim()).contacto(datos.contacto().trim())
                .autorizacionContacto(Boolean.TRUE.equals(datos.autorizacionContacto()))
                .estado(datos.estado() == null || datos.estado().isBlank() ? "Recibida" : datos.estado())
                .fechaCreacionMillis(creada).fechaActualizacionMillis(actualizada)
                .cuentaDemoId(datos.cuentaDemoId()).esDemo(Boolean.TRUE.equals(datos.esDemo())).build();
    }

    private void validar(RecomendacionRequest datos) {
        if (datos == null || datos.agenteId() == null || datos.agenteId() <= 0
                || vacio(datos.organizacion()) || vacio(datos.contacto())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Agente, organización y contacto son obligatorios");
        }
        if (!Boolean.TRUE.equals(datos.autorizacionContacto())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Se necesita autorización para contactar a la organización");
        }
        if (datos.estado() != null && !datos.estado().isBlank() && !ESTADOS.contains(datos.estado())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El estado de la recomendación no es válido");
        }
    }

    private RecomendacionOrganizacion buscar(Long id) {
        return repositorio.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Recomendación no encontrada"));
    }

    private boolean vacio(String texto) { return texto == null || texto.isBlank(); }
}
