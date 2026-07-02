package com.tomadin.pacientes.dto.responses;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record PacienteResponse(
        @NotBlank Long idPaciente,
        @NotBlank @Size(max = 50) String nombre,
        @NotBlank @Size(max = 50) String apellido,
        @NotBlank @Pattern(regexp = "\\d{7,8}") String dni,
        @NotNull @Past LocalDate fechaNac,
        @Pattern(regexp = "\\+?\\d{6,15}") String telefono
) {}