package com.pagoflex.reportes_service.dto;

public record ActualizarReporteRequest(
        Long agenteAsignadoId,
        String estado,
        String respuesta
) {
}
