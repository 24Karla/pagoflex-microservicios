package com.pagoflex.pagos_service.controller;

import com.pagoflex.pagos_service.dto.RegistrarPagoRequest;
import com.pagoflex.pagos_service.model.ComprobantePago;
import com.pagoflex.pagos_service.model.Pago;
import com.pagoflex.pagos_service.service.PagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
public class PagoController {
    private final PagoService servicio;

    @GetMapping
    public List<Pago> listar(@RequestParam(required = false) Long usuarioId,
                             @RequestParam(required = false) Long compromisoId) {
        return servicio.listar(usuarioId, compromisoId);
    }

    @GetMapping("/{id}")
    public Pago obtener(@PathVariable Long id) { return servicio.obtener(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Pago registrar(@RequestBody RegistrarPagoRequest datos) { return servicio.registrar(datos); }

    @GetMapping("/{id}/comprobante")
    public ComprobantePago comprobante(@PathVariable Long id) { return servicio.obtenerComprobante(id); }

    @GetMapping("/comprobantes")
    public List<ComprobantePago> comprobantesDeUsuario(@RequestParam Long usuarioId) {
        return servicio.listarComprobantes(usuarioId);
    }
}
