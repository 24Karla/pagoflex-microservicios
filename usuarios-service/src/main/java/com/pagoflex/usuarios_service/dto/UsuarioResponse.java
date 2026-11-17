package com.pagoflex.usuarios_service.dto;

public record UsuarioResponse(
        Long id,
        String nombre,
        String correo,
        String ciudad,
        Long rolId,
        String rolNombre,
        String avatarUrl
) {

}
