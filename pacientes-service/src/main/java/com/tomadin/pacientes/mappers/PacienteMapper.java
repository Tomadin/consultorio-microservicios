package com.tomadin.pacientes.mappers;

import com.tomadin.pacientes.dto.requests.PacienteRequest;
import com.tomadin.pacientes.dto.responses.PacienteResponse;
import com.tomadin.pacientes.entities.Paciente;
import org.springframework.stereotype.Component;

@Component
public class PacienteMapper {

    public Paciente toEntity(PacienteRequest request){
        Paciente paciente = new Paciente();
        paciente.setNombre(request.nombre());
        paciente.setDni(request.dni());
        paciente.setApellido(request.apellido());
        paciente.setTelefono(request.telefono());
        paciente.setFechaNac(request.fechaNac());
        return paciente;
    }

    public PacienteResponse toResponse(Paciente paciente){
        return new PacienteResponse(
                paciente.getIdPaciente(),
                paciente.getNombre(),
                paciente.getApellido(),
                paciente.getDni(),
                paciente.getFechaNac(),
                paciente.getTelefono()
        );
    }

    public Paciente updateEntityFromRequest(PacienteRequest request, Paciente paciente) {
        paciente.setNombre(request.nombre());
        paciente.setApellido(request.apellido());
        paciente.setDni(request.dni());
        paciente.setTelefono(request.telefono());
        paciente.setFechaNac(request.fechaNac());

        return paciente;
    }

}