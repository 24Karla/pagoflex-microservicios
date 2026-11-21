package com.pagoflex.recomendaciones_service.controller;

import com.pagoflex.recomendaciones_service.dto.RecomendacionRequest;
import com.pagoflex.recomendaciones_service.model.RecomendacionOrganizacion;
import com.pagoflex.recomendaciones_service.service.RecomendacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping("/api/recomendaciones")
@RequiredArgsConstructor
public class RecomendacionController {
    private final RecomendacionService servicio;

    @GetMapping
    public List<RecomendacionOrganizacion> listar(@RequestParam(required = false) Long agenteId) {
        return servicio.listar(agenteId);
    }

    @GetMapping("/{id}")
    public RecomendacionOrganizacion obtener(@PathVariable Long id) { return servicio.obtener(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RecomendacionOrganizacion crear(@RequestBody RecomendacionRequest datos) { return servicio.crear(datos); }

    @PutMapping("/{id}")
    public RecomendacionOrganizacion actualizar(@PathVariable Long id, @RequestBody RecomendacionRequest datos) {
        return servicio.actualizar(id, datos);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) { servicio.eliminar(id); }
}
