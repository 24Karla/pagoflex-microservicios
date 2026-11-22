package com.pagoflex.usuarios_service.dto;

public record ActualizarUsuarioRequest(
        String nombre,
        String cuenta,
        String ciudad,
        String rut,
        Long rolId
) {
}
