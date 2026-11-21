package com.pagoflex.pagos_service.repository;

import com.pagoflex.pagos_service.model.ComprobantePago;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ComprobantePagoRepository extends JpaRepository<ComprobantePago, Long> {
    Optional<ComprobantePago> findByPagoId(Long pagoId);
    List<ComprobantePago> findByUsuarioIdOrderByFechaMillisDesc(Long usuarioId);
}
