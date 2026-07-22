package com.tomadin.turnos.dto.requests;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record TurnoRequest(
        @NotNull @FutureOrPresent LocalDate fecha,
        @NotBlank @Size(max = 100) String tratamiento,
        @NotBlank @Pattern(regexp = "\\d{7,8}") String dniPaciente
) {}
