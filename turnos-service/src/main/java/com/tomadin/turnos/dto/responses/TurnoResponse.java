package com.tomadin.turnos.dto.responses;

import java.time.LocalDate;

public record TurnoResponse(
        Long idTurno,
        LocalDate fecha,
        String tratamiento,
        String dniPaciente,
        String nombreCompletoPaciente
) {}
