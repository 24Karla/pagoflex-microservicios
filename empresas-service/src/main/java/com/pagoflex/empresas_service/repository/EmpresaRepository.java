package com.pagoflex.empresas_service.repository;

import com.pagoflex.empresas_service.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
    List<Empresa> findByEjecutivoId(Long ejecutivoId);
    List<Empresa> findByCuentaDemoId(Long cuentaDemoId);
}
