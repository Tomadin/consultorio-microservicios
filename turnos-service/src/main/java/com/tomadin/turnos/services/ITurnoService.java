package com.tomadin.turnos.services;

import com.tomadin.turnos.dto.requests.TurnoRequest;
import com.tomadin.turnos.dto.responses.TurnoResponse;

import java.util.List;

public interface ITurnoService {
    public List<TurnoResponse> getAll();
    public TurnoResponse save(TurnoRequest request);
    public void delete(Long id);
    public TurnoResponse findById(Long id);
    public TurnoResponse update(Long id, TurnoRequest request);
}
