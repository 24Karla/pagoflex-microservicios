package com.pagoflex.reportes_service.controller;

import com.pagoflex.reportes_service.dto.ActualizarReporteRequest;
import com.pagoflex.reportes_service.dto.CrearReporteRequest;
import com.pagoflex.reportes_service.model.ReporteIncidencia;
import com.pagoflex.reportes_service.service.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
public class ReporteController {
    private final ReporteService servicio;

    @GetMapping
    public List<ReporteIncidencia> listar(@RequestParam(required = false) Long usuarioId,
                                          @RequestParam(required = false) Long agenteId) {
        return servicio.listar(usuarioId, agenteId);
    }

    @GetMapping("/{id}")
    public ReporteIncidencia obtener(@PathVariable Long id) { return servicio.obtener(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReporteIncidencia crear(@RequestBody CrearReporteRequest datos) { return servicio.crear(datos); }

    @PutMapping("/{id}")
    public ReporteIncidencia actualizar(@PathVariable Long id, @RequestBody ActualizarReporteRequest datos) {
        return servicio.actualizar(id, datos);
    }
}
