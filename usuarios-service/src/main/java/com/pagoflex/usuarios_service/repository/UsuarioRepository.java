package com.pagoflex.usuarios_service.repository;

import com.pagoflex.usuarios_service.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByCuentaIgnoreCase(String cuenta);
    boolean existsByCuentaIgnoreCase(String cuenta);
    boolean existsByRut(String rut);
    boolean existsByCuentaIgnoreCaseAndIdNot(String cuenta, Long id);
    boolean existsByRutAndIdNot(String rut, Long id);
    java.util.List<Usuario> findByNombreContainingIgnoreCaseOrCuentaContainingIgnoreCaseOrRutContainingIgnoreCase(
            String nombre, String cuenta, String rut);
}
