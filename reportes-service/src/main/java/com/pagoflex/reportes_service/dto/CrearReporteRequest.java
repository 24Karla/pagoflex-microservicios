package com.pagoflex.reportes_service.dto;

public record CrearReporteRequest(
        Long usuarioId,
        Long compromisoId,
        Long empresaId,
        String tipo,
        String descripcion,
        Long cuentaDemoId,
        Boolean esDemo
) {
}
