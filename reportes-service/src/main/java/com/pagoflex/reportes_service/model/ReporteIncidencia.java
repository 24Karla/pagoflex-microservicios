package com.pagoflex.reportes_service.model;

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
@Table(name = "reportes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReporteIncidencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long usuarioId;
    private Long compromisoId;
    private Long empresaId;
    private Long agenteAsignadoId;

    @Column(nullable = false, length = 80)
    private String tipo;

    @Column(nullable = false, length = 2000)
    private String descripcion;

    @Column(nullable = false, length = 30)
    private String estado;

    @Column(length = 2000)
    private String respuesta;

    @Column(nullable = false)
    private Long fechaCreacionMillis;

    @Column(nullable = false)
    private Long fechaActualizacionMillis;

    private Long cuentaDemoId;

    @Column(nullable = false)
    private boolean esDemo;
}
