package com.tomadin.turnos.client;

import java.time.LocalDate;

/**
 * Representación de la respuesta del microservicio de pacientes.
 * Se usa únicamente para deserializar el cuerpo devuelto por su API.
 */
public record PacienteDTO(
        Long idPaciente,
        String nombre,
        String apellido,
        String dni,
        LocalDate fechaNac,
        String telefono
) {}
