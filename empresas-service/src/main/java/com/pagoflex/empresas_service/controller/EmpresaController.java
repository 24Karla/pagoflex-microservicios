package com.pagoflex.empresas_service.controller;

import com.pagoflex.empresas_service.dto.EmpresaRequest;
import com.pagoflex.empresas_service.model.Empresa;
import com.pagoflex.empresas_service.service.EmpresaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/empresas")
@RequiredArgsConstructor
public class EmpresaController {
    private final EmpresaService servicio;

    @GetMapping
    public List<Empresa> listar(@RequestParam(required = false) Long ejecutivoId,
                                @RequestParam(required = false) Long cuentaDemoId) {
        return servicio.listar(ejecutivoId, cuentaDemoId);
    }

    @GetMapping("/{id}")
    public Empresa obtener(@PathVariable Long id) { return servicio.obtener(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Empresa crear(@RequestBody EmpresaRequest datos) { return servicio.crear(datos); }

    @PutMapping("/{id}")
    public Empresa actualizar(@PathVariable Long id, @RequestBody EmpresaRequest datos) {
        return servicio.actualizar(id, datos);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) { servicio.eliminar(id); }
}
