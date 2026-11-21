package com.pagoflex.roles_service.config;

import com.pagoflex.roles_service.model.Rol;
import com.pagoflex.roles_service.repository.RolRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatosIniciales {

    @Bean
    CommandLineRunner cargarRoles(RolRepository repositorio) {
        return args -> {
            if (repositorio.count() == 0) {
                repositorio.save(Rol.builder().nombre("Agente").build());
                repositorio.save(Rol.builder().nombre("Ejecutivo").build());
                repositorio.save(Rol.builder().nombre("Usuario Final").build());
            }
        };
    }
}
