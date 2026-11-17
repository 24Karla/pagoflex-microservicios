package com.pagoflex.usuarios_service.dto;

public record RegistroRequest(
    String nombre,
    Long rut,
    String correo,
    String ciudad,
    String contrasena
) {

}
