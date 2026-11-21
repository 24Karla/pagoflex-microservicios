package com.pagoflex.compromisos_service.repository;

import com.pagoflex.compromisos_service.model.Compromiso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompromisoRepository extends JpaRepository<Compromiso, Long> {
    List<Compromiso> findByUsuarioId(Long usuarioId);
    List<Compromiso> findByAgenteId(Long agenteId);
    List<Compromiso> findByEmpresaId(Long empresaId);
}
