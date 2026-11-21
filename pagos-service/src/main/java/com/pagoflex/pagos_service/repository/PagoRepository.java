package com.pagoflex.pagos_service.repository;

import com.pagoflex.pagos_service.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PagoRepository extends JpaRepository<Pago, Long> {
    List<Pago> findByUsuarioIdOrderByFechaMillisDesc(Long usuarioId);
    List<Pago> findByCompromisoIdOrderByFechaMillisDesc(Long compromisoId);
}
