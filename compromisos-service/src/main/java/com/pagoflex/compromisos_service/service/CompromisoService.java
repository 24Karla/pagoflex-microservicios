package com.pagoflex.compromisos_service.service;

import com.pagoflex.compromisos_service.dto.CompromisoRequest;
import com.pagoflex.compromisos_service.model.Compromiso;
import com.pagoflex.compromisos_service.repository.CompromisoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CompromisoService {
    private static final Set<String> ESTADOS = Set.of("Pendiente", "Vencido", "Pagado", "En revisión", "Anulado");
    private final CompromisoRepository repositorio;

    public List<Compromiso> listar(Long usuarioId, Long agenteId, Long empresaId) {
        if (usuarioId != null) return repositorio.findByUsuarioId(usuarioId);
        if (agenteId != null) return repositorio.findByAgenteId(agenteId);
        if (empresaId != null) return repositorio.findByEmpresaId(empresaId);
        return repositorio.findAll();
    }

    public Compromiso obtener(Long id) {
        return buscar(id);
    }

    public Compromiso crear(CompromisoRequest datos) {
        validar(datos);
        return repositorio.save(construir(datos, null));
    }

    public Compromiso actualizar(Long id, CompromisoRequest datos) {
        Compromiso existente = buscar(id);
        validar(datos);
        Compromiso actualizado = construir(datos, existente.getFechaCreacionMillis());
        actualizado.setId(existente.getId());
        return repositorio.save(actualizado);
    }

    public void eliminar(Long id) {
        repositorio.delete(buscar(id));
    }

    private Compromiso construir(CompromisoRequest datos, Long fechaCreacion) {
        return Compromiso.builder()
                .usuarioId(datos.usuarioId()).agenteId(datos.agenteId()).empresaId(datos.empresaId())
                .empresaNombre(datos.empresaNombre()).clienteNombre(datos.clienteNombre().trim())
                .concepto(datos.concepto().trim()).cuotaActual(datos.cuotaActual()).cuotasTotales(datos.cuotasTotales())
                .montoClp(datos.montoClp()).vencimientoMillis(datos.vencimientoMillis())
                .estado(datos.estado() == null || datos.estado().isBlank() ? "Pendiente" : datos.estado())
                .recargoClp(datos.recargoClp() == null ? 0L : datos.recargoClp())
                .fechaCreacionMillis(fechaCreacion == null ? System.currentTimeMillis() : fechaCreacion)
                .cuentaDemoId(datos.cuentaDemoId()).esDemo(Boolean.TRUE.equals(datos.esDemo())).build();
    }

    private void validar(CompromisoRequest datos) {
        if (datos == null || datos.empresaId() == null || datos.empresaId() <= 0
                || vacio(datos.clienteNombre()) || vacio(datos.concepto()) || datos.cuotaActual() == null
                || datos.cuotasTotales() == null || datos.montoClp() == null || datos.vencimientoMillis() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Faltan datos obligatorios del compromiso");
        }
        if (datos.cuotaActual() < 1 || datos.cuotasTotales() < datos.cuotaActual()
                || datos.montoClp() < 0 || (datos.recargoClp() != null && datos.recargoClp() < 0)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cuotas y montos no tienen valores válidos");
        }
        if (datos.estado() != null && !datos.estado().isBlank() && !ESTADOS.contains(datos.estado())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El estado del compromiso no es válido");
        }
    }

    private Compromiso buscar(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Compromiso no encontrado"));
    }

    private boolean vacio(String texto) { return texto == null || texto.isBlank(); }
}
