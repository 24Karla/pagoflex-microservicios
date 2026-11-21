package com.pagoflex.auditoria_service.controller;

import com.pagoflex.auditoria_service.dto.EventoAuditoriaRequest;
import com.pagoflex.auditoria_service.model.EventoAuditoria;
import com.pagoflex.auditoria_service.service.AuditoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/auditoria")
@RequiredArgsConstructor
public class AuditoriaController {
    private final AuditoriaService servicio;

    @GetMapping
    public List<EventoAuditoria> listar(@RequestParam(required = false) Long actorUsuarioId) {
        return servicio.listar(actorUsuarioId);
    }

    @GetMapping("/{id}")
    public EventoAuditoria obtener(@PathVariable Long id) {
        return servicio.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventoAuditoria registrar(@RequestBody EventoAuditoriaRequest datos) {
        return servicio.registrar(datos);
    }
}
