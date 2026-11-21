package com.pagoflex.recomendaciones_service.dto;

public record RecomendacionRequest(
        Long agenteId,
        String organizacion,
        String contacto,
        Boolean autorizacionContacto,
        String estado,
        Long cuentaDemoId,
        Boolean esDemo
) {
}
