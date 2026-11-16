package com.pagoflex.usuarios_service.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class RolCliente {

    private final WebClient webClient;

    public RolCLiente(@Value("${roles.service.url}")String rolesUrl){
        this.webClient = WebClient.builder().baseUrl(rolesUrl).build();
    }

    public List<RolDto> listar(){
        try {
            return webClient.get()
                    .uri("/api/roles")
                    .retrieve()
                    .bodyToFlux(RolDto.class)
                    .collectList()
                    .block();
        } catch (WebClientRequestException e) {
            throw servicioNoDisponible();
        }
    }

}
