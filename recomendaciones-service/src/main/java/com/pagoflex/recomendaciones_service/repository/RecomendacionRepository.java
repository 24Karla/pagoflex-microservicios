package com.pagoflex.recomendaciones_service.repository;

import com.pagoflex.recomendaciones_service.model.RecomendacionOrganizacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecomendacionRepository extends JpaRepository<RecomendacionOrganizacion, Long> {
    List<RecomendacionOrganizacion> findByAgenteIdOrderByFechaCreacionMillisDesc(Long agenteId);
}
