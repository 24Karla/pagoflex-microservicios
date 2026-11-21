package com.pagoflex.auditoria_service.repository;

import com.pagoflex.auditoria_service.model.EventoAuditoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventoAuditoriaRepository extends JpaRepository<EventoAuditoria, Long> {
    List<EventoAuditoria> findAllByOrderByFechaMillisDesc();
    List<EventoAuditoria> findByActorUsuarioIdOrderByFechaMillisDesc(Long actorUsuarioId);
}
