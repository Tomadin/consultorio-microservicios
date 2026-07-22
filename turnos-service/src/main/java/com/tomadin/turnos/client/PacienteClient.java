package com.tomadin.turnos.client;

import com.tomadin.turnos.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/**
 * Cliente HTTP hacia el microservicio de pacientes.
 */
@Component
public class PacienteClient {

    private final RestClient restClient;

    public PacienteClient(@Value("${pacientes.service.url}") String baseUrl) {
        this.restClient = RestClient.create(baseUrl);
    }

    public PacienteDTO buscarPorDni(String dni) {
        return restClient.get()
                .uri("/api/v1/pacientes/dni/{dni}", dni)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    throw new NotFoundException("No existe un paciente con DNI: " + dni);
                })
                .body(PacienteDTO.class);
    }
}
