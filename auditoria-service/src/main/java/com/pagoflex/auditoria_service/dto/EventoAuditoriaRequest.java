package com.pagoflex.auditoria_service.dto;

public record EventoAuditoriaRequest(
        Long actorUsuarioId,
        String entidad,
        Long entidadId,
        String accion,
        String detalle,
        Long cuentaDemoId,
        Boolean esDemo
) {
}
