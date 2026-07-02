package com.tomadin.pacientes.services;

import com.tomadin.pacientes.dto.requests.PacienteRequest;
import com.tomadin.pacientes.dto.responses.PacienteResponse;
import com.tomadin.pacientes.entities.Paciente;
import com.tomadin.pacientes.exceptions.ConflictException;
import com.tomadin.pacientes.exceptions.NotFoundException;
import com.tomadin.pacientes.mappers.PacienteMapper;
import com.tomadin.pacientes.repositories.PacienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PacienteService implements IPacienteService{
    private final PacienteRepository repository;
    private final PacienteMapper mapper;

    public PacienteService(PacienteRepository repository, PacienteMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PacienteResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public PacienteResponse save(PacienteRequest request) {
        if (repository.existsByDni(request.dni())) {
            throw new ConflictException("Ya existe un paciente con el DNI: " + request.dni());
        }
        Paciente guardado = repository.save(mapper.toEntity(request));
        return mapper.toResponse(guardado);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Paciente paciente = buscarPorId(id);
        repository.delete(paciente);
    }

    @Override
    @Transactional(readOnly = true)
    public PacienteResponse findById(Long id) {
        return mapper.toResponse(buscarPorId(id));
    }

    @Override
    @Transactional
    public PacienteResponse update(Long id, PacienteRequest request) {
        Paciente paciente = buscarPorId(id);

        if (repository.existsByDniAndIdPacienteNot(request.dni(), id)) {
            throw new ConflictException("Ya existe otro paciente con el DNI: " + request.dni());
        }

        paciente = mapper.updateEntityFromRequest(request, paciente);
        paciente = repository.save(paciente);

        return mapper.toResponse(paciente);
    }

    private Paciente buscarPorId(Long id){
        return repository.findById(id)
                .orElseThrow(()-> new NotFoundException("No se ha encontrado el paciente con ID: "+id));
    }
}
