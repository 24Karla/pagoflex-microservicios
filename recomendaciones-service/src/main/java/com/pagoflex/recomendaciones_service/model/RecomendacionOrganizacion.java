package com.pagoflex.recomendaciones_service.model;

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
@Table(name = "recomendaciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecomendacionOrganizacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long agenteId;

    @Column(nullable = false, length = 150)
    private String organizacion;

    @Column(nullable = false, length = 150)
    private String contacto;

    @Column(nullable = false)
    private boolean autorizacionContacto;

    @Column(nullable = false, length = 30)
    private String estado;

    @Column(nullable = false)
    private Long fechaCreacionMillis;

    @Column(nullable = false)
    private Long fechaActualizacionMillis;

    private Long cuentaDemoId;

    @Column(nullable = false)
    private boolean esDemo;
}
