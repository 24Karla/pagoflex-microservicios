package com.pagoflex.pagos_service.service;

import com.pagoflex.pagos_service.dto.RegistrarPagoRequest;
import com.pagoflex.pagos_service.model.ComprobantePago;
import com.pagoflex.pagos_service.model.Pago;
import com.pagoflex.pagos_service.repository.ComprobantePagoRepository;
import com.pagoflex.pagos_service.repository.PagoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PagoService {
    private final PagoRepository pagoRepository;
    private final ComprobantePagoRepository comprobanteRepository;

    public List<Pago> listar(Long usuarioId, Long compromisoId) {
        if (usuarioId != null) return pagoRepository.findByUsuarioIdOrderByFechaMillisDesc(usuarioId);
        if (compromisoId != null) return pagoRepository.findByCompromisoIdOrderByFechaMillisDesc(compromisoId);
        return pagoRepository.findAll();
    }

    public Pago obtener(Long id) {
        return pagoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pago no encontrado"));
    }

    @Transactional
    public Pago registrar(RegistrarPagoRequest datos) {
        if (datos == null || datos.usuarioId() == null || datos.usuarioId() <= 0
                || datos.montoClp() == null || datos.montoClp() <= 0
                || vacio(datos.concepto()) || vacio(datos.canal())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Usuario, monto positivo, concepto y canal son obligatorios");
        }
        long ahora = System.currentTimeMillis();
        Pago pago = pagoRepository.save(Pago.builder()
                .usuarioId(datos.usuarioId()).compromisoId(datos.compromisoId())
                .montoClp(datos.montoClp()).concepto(datos.concepto().trim())
                .fechaMillis(ahora).estado("Registrado").build());

        String sufijo = UUID.randomUUID().toString().replace("-", "").substring(0, 12)
                .toUpperCase(Locale.ROOT);
        comprobanteRepository.save(ComprobantePago.builder()
                .pagoId(pago.getId()).usuarioId(pago.getUsuarioId()).folio("PF-" + sufijo)
                .montoClp(pago.getMontoClp()).concepto(pago.getConcepto()).canal(datos.canal().trim())
                .fechaMillis(ahora).esDemo(Boolean.TRUE.equals(datos.esDemo())).build());
        return pago;
    }

    public ComprobantePago obtenerComprobante(Long pagoId) {
        obtener(pagoId);
        return comprobanteRepository.findByPagoId(pagoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comprobante no encontrado"));
    }

    public List<ComprobantePago> listarComprobantes(Long usuarioId) {
        return comprobanteRepository.findByUsuarioIdOrderByFechaMillisDesc(usuarioId);
    }

    private boolean vacio(String texto) { return texto == null || texto.isBlank(); }
}
