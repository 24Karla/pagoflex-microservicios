package com.pagoflex.pagos_service.dto;

public record RegistrarPagoRequest(
        Long usuarioId,
        Long compromisoId,
        Long montoClp,
        String concepto,
        String canal,
        Boolean esDemo
) {
}
