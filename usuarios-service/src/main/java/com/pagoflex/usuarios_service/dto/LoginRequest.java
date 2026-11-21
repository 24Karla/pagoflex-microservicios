package com.pagoflex.usuarios_service.dto;

public record LoginRequest(String cuenta, String contrasena, Long rolId) {

}
