package com.tomadin.pacientes.services;

import com.tomadin.pacientes.dto.requests.PacienteRequest;
import com.tomadin.pacientes.dto.responses.PacienteResponse;
import com.tomadin.pacientes.entities.Paciente;

import java.util.List;

public interface IPacienteService {
    public List<PacienteResponse> getAll();
    public PacienteResponse save(PacienteRequest request);
    public void delete(Long id);
    public PacienteResponse findById(Long id);
    public PacienteResponse findByDni(String dni);
    public PacienteResponse update(Long id, PacienteRequest request);
}
