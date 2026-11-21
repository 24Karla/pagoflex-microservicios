package com.pagoflex.reportes_service.repository;

import com.pagoflex.reportes_service.model.ReporteIncidencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReporteRepository extends JpaRepository<ReporteIncidencia, Long> {
    List<ReporteIncidencia> findByUsuarioIdOrderByFechaCreacionMillisDesc(Long usuarioId);
    List<ReporteIncidencia> findByAgenteAsignadoIdOrderByFechaCreacionMillisDesc(Long agenteAsignadoId);
}
