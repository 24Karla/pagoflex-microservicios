package com.pagoflex.auditoria_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "eventos_auditoria")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventoAuditoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long actorUsuarioId;

    @Column(nullable = false, length = 80)
    private String entidad;

    private Long entidadId;

    @Column(nullable = false, length = 60)
    private String accion;

    @Column(nullable = false, length = 2000)
    private String detalle;

    @Column(nullable = false)
    private Long fechaMillis;

    private Long cuentaDemoId;

    @Column(nullable = false)
    private boolean esDemo;
}
