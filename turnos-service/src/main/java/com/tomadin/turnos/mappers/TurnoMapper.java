package com.tomadin.turnos.mappers;

import com.tomadin.turnos.dto.requests.TurnoRequest;
import com.tomadin.turnos.dto.responses.TurnoResponse;
import com.tomadin.turnos.entities.Turno;
import org.springframework.stereotype.Component;

@Component
public class TurnoMapper {

    public Turno toEntity(TurnoRequest request) {
        Turno turno = new Turno();
        turno.setFecha(request.fecha());
        turno.setTratamiento(request.tratamiento());
        turno.setDniPaciente(request.dniPaciente());
        return turno;
    }

    public TurnoResponse toResponse(Turno turno) {
        return new TurnoResponse(
                turno.getIdTurno(),
                turno.getFecha(),
                turno.getTratamiento(),
                turno.getDniPaciente(),
                turno.getNombreCompletoPaciente()
        );
    }

    public Turno updateEntityFromRequest(TurnoRequest request, Turno turno) {
        turno.setFecha(request.fecha());
        turno.setTratamiento(request.tratamiento());
        turno.setDniPaciente(request.dniPaciente());
        return turno;
    }
}
