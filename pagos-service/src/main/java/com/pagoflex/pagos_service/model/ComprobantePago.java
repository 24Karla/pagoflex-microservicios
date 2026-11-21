package com.pagoflex.pagos_service.model;

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
@Table(name = "comprobantes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComprobantePago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long pagoId;

    @Column(nullable = false)
    private Long usuarioId;

    @Column(nullable = false, unique = true, length = 40)
    private String folio;

    @Column(nullable = false)
    private Long montoClp;

    @Column(nullable = false, length = 250)
    private String concepto;

    @Column(nullable = false, length = 40)
    private String canal;

    @Column(nullable = false)
    private Long fechaMillis;

    @Column(nullable = false)
    private boolean esDemo;
}
