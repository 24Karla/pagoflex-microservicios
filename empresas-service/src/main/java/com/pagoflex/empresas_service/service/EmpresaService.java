package com.pagoflex.empresas_service.service;

import com.pagoflex.empresas_service.dto.EmpresaRequest;
import com.pagoflex.empresas_service.model.Empresa;
import com.pagoflex.empresas_service.repository.EmpresaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmpresaService {
    private final EmpresaRepository repositorio;

    public List<Empresa> listar(Long ejecutivoId, Long cuentaDemoId) {
        if (ejecutivoId != null) return repositorio.findByEjecutivoId(ejecutivoId);
        if (cuentaDemoId != null) return repositorio.findByCuentaDemoId(cuentaDemoId);
        return repositorio.findAll();
    }

    public Empresa obtener(Long id) { return buscar(id); }

    public Empresa crear(EmpresaRequest datos) {
        validar(datos);
        return repositorio.save(construir(datos, null));
    }

    public Empresa actualizar(Long id, EmpresaRequest datos) {
        Empresa existente = buscar(id);
        validar(datos);
        Empresa actualizada = construir(datos, existente.getId());
        return repositorio.save(actualizada);
    }

    public void eliminar(Long id) { repositorio.delete(buscar(id)); }

    private Empresa construir(EmpresaRequest datos, Long id) {
        return Empresa.builder().id(id).nombre(datos.nombre().trim())
                .recargoFijoClp(datos.recargoFijoClp() == null ? 0L : datos.recargoFijoClp())
                .ejecutivoId(datos.ejecutivoId()).cuentaDemoId(datos.cuentaDemoId())
                .esDemo(Boolean.TRUE.equals(datos.esDemo()))
                .telefonoContacto(datos.telefonoContacto() == null ? "" : datos.telefonoContacto().trim())
                .correoContacto(datos.correoContacto() == null ? "" : datos.correoContacto().trim())
                .build();
    }

    private void validar(EmpresaRequest datos) {
        if (datos == null || vacio(datos.nombre())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre de la empresa es obligatorio");
        }
        if (datos.recargoFijoClp() != null && datos.recargoFijoClp() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El recargo no puede ser negativo");
        }
    }

    private Empresa buscar(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa no encontrada"));
    }

    private boolean vacio(String texto) { return texto == null || texto.isBlank(); }
}
