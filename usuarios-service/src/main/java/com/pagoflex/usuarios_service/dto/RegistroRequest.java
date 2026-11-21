package com.pagoflex.usuarios_service.dto;

public record RegistroRequest(
    String nombre,
    String rut,
    String cuenta,
    String ciudad,
    String contrasena,
    Long rolId
) {

}
