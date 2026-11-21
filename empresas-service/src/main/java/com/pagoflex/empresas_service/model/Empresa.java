package com.pagoflex.empresas_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "empresas", uniqueConstraints = @UniqueConstraint(columnNames = {"cuenta_demo_id", "nombre"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Empresa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(nullable = false)
    private Long recargoFijoClp;

    private Long ejecutivoId;
    private Long cuentaDemoId;

    @Column(nullable = false)
    private boolean esDemo;

    @Column(nullable = false, length = 40)
    private String telefonoContacto;

    @Column(nullable = false, length = 150)
    private String correoContacto;
}
