package com.tomadin.turnos.services;

import com.tomadin.turnos.client.PacienteClient;
import com.tomadin.turnos.client.PacienteDTO;
import com.tomadin.turnos.dto.requests.TurnoRequest;
import com.tomadin.turnos.dto.responses.TurnoResponse;
import com.tomadin.turnos.entities.Turno;
import com.tomadin.turnos.exceptions.NotFoundException;
import com.tomadin.turnos.mappers.TurnoMapper;
import com.tomadin.turnos.repositories.TurnoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TurnoService implements ITurnoService {

    private final TurnoRepository repository;
    private final TurnoMapper mapper;
    private final PacienteClient pacienteClient;

    public TurnoService(TurnoRepository repository, TurnoMapper mapper, PacienteClient pacienteClient) {
        this.repository = repository;
        this.mapper = mapper;
        this.pacienteClient = pacienteClient;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TurnoResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public TurnoResponse save(TurnoRequest request) {
        Turno turno = mapper.toEntity(request);
        turno.setNombreCompletoPaciente(resolverNombrePaciente(request.dniPaciente()));
        Turno guardado = repository.save(turno);
        return mapper.toResponse(guardado);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Turno turno = buscarPorId(id);
        repository.delete(turno);
    }

    @Override
    @Transactional(readOnly = true)
    public TurnoResponse findById(Long id) {
        return mapper.toResponse(buscarPorId(id));
    }

    @Override
    @Transactional
    public TurnoResponse update(Long id, TurnoRequest request) {
        Turno turno = buscarPorId(id);
        turno = mapper.updateEntityFromRequest(request, turno);
        turno.setNombreCompletoPaciente(resolverNombrePaciente(request.dniPaciente()));
        turno = repository.save(turno);
        return mapper.toResponse(turno);
    }

    private Turno buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("No se ha encontrado el turno con ID: " + id));
    }

    private String resolverNombrePaciente(String dni) {
        PacienteDTO paciente = pacienteClient.buscarPorDni(dni);
        return paciente.nombre() + " " + paciente.apellido();
    }
}
