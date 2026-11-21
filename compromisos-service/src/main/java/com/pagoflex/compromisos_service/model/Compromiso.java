package com.pagoflex.compromisos_service.model;

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
@Table(name = "compromisos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Compromiso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long usuarioId;
    private Long agenteId;

    @Column(nullable = false)
    private Long empresaId;

    @Column(length = 150)
    private String empresaNombre;

    @Column(nullable = false, length = 150)
    private String clienteNombre;

    @Column(nullable = false, length = 250)
    private String concepto;

    @Column(nullable = false)
    private Integer cuotaActual;

    @Column(nullable = false)
    private Integer cuotasTotales;

    @Column(nullable = false)
    private Long montoClp;

    @Column(nullable = false)
    private Long vencimientoMillis;

    @Column(nullable = false, length = 30)
    private String estado;

    @Column(nullable = false)
    private Long recargoClp;

    @Column(nullable = false)
    private Long fechaCreacionMillis;

    private Long cuentaDemoId;

    @Column(nullable = false)
    private boolean esDemo;
}
