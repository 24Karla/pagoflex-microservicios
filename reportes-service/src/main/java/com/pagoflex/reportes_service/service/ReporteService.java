package com.pagoflex.reportes_service.service;

import com.pagoflex.reportes_service.dto.ActualizarReporteRequest;
import com.pagoflex.reportes_service.dto.CrearReporteRequest;
import com.pagoflex.reportes_service.model.ReporteIncidencia;
import com.pagoflex.reportes_service.repository.ReporteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ReporteService {
    private static final Set<String> ESTADOS = Set.of("Abierto", "En gestión", "Resuelto");
    private final ReporteRepository repositorio;

    public List<ReporteIncidencia> listar(Long usuarioId, Long agenteId) {
        if (usuarioId != null) return repositorio.findByUsuarioIdOrderByFechaCreacionMillisDesc(usuarioId);
        if (agenteId != null) return repositorio.findByAgenteAsignadoIdOrderByFechaCreacionMillisDesc(agenteId);
        return repositorio.findAll();
    }

    public ReporteIncidencia obtener(Long id) { return buscar(id); }

    public ReporteIncidencia crear(CrearReporteRequest datos) {
        if (datos == null || vacio(datos.tipo()) || vacio(datos.descripcion())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo y descripción son obligatorios");
        }
        long ahora = System.currentTimeMillis();
        return repositorio.save(ReporteIncidencia.builder().usuarioId(datos.usuarioId())
                .compromisoId(datos.compromisoId()).empresaId(datos.empresaId()).tipo(datos.tipo().trim())
                .descripcion(datos.descripcion().trim()).estado("Abierto").respuesta(null)
                .fechaCreacionMillis(ahora).fechaActualizacionMillis(ahora)
                .cuentaDemoId(datos.cuentaDemoId()).esDemo(Boolean.TRUE.equals(datos.esDemo())).build());
    }

    public ReporteIncidencia actualizar(Long id, ActualizarReporteRequest datos) {
        ReporteIncidencia reporte = buscar(id);
        if (datos == null || vacio(datos.estado()) || !ESTADOS.contains(datos.estado())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El estado del reporte no es válido");
        }
        reporte.setAgenteAsignadoId(datos.agenteAsignadoId());
        reporte.setEstado(datos.estado());
        reporte.setRespuesta(datos.respuesta());
        reporte.setFechaActualizacionMillis(System.currentTimeMillis());
        return repositorio.save(reporte);
    }

    private ReporteIncidencia buscar(Long id) {
        return repositorio.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Reporte no encontrado"));
    }

    private boolean vacio(String texto) { return texto == null || texto.isBlank(); }
}
