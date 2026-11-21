package com.pagoflex.compromisos_service.dto;

public record CompromisoRequest(
        Long usuarioId,
        Long agenteId,
        Long empresaId,
        String empresaNombre,
        String clienteNombre,
        String concepto,
        Integer cuotaActual,
        Integer cuotasTotales,
        Long montoClp,
        Long vencimientoMillis,
        String estado,
        Long recargoClp,
        Long cuentaDemoId,
        Boolean esDemo
) {
}
