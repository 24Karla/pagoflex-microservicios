package com.pagoflex.roles_service.repository;

import com.pagoflex.roles_service.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolRepository extends JpaRepository<Rol, Long> {
}
