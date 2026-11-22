package com.pagoflex.usuarios_service.dto;

public record UsuarioResponse(
        Long id,
        String nombre,
        String rut,
        String cuenta,
        String ciudad,
        Long rolId,
        String rolNombre,
        String avatarUrl
) {

}
