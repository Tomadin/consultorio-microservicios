package com.tomadin.turnos.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tomadin.turnos.dto.requests.TurnoRequest;
import com.tomadin.turnos.dto.responses.TurnoResponse;
import com.tomadin.turnos.services.ITurnoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TurnoController.class)
class TurnoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ITurnoService turnoService;

    @Test
    void crear_conBodyValido_devuelve201() throws Exception {
        TurnoRequest request = new TurnoRequest(LocalDate.now().plusDays(1), "Limpieza", "12345678");
        when(turnoService.save(any(TurnoRequest.class)))
                .thenReturn(new TurnoResponse(1L, request.fecha(), "Limpieza", "12345678", "Ana Pérez"));

        mockMvc.perform(post("/api/v1/turnos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idTurno").value(1))
                .andExpect(jsonPath("$.nombreCompletoPaciente").value("Ana Pérez"));
    }

    @Test
    void crear_conBodyInvalido_devuelve400() throws Exception {
        // dni con formato inválido y tratamiento en blanco
        TurnoRequest request = new TurnoRequest(LocalDate.now().plusDays(1), "", "abc");

        mockMvc.perform(post("/api/v1/turnos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
