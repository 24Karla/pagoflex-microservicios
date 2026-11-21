package com.pagoflex.empresas_service.dto;

public record EmpresaRequest(
        String nombre,
        Long recargoFijoClp,
        Long ejecutivoId,
        Long cuentaDemoId,
        Boolean esDemo,
        String telefonoContacto,
        String correoContacto
) {
}
