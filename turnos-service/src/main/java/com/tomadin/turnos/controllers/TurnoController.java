package com.tomadin.turnos.controllers;

import com.tomadin.turnos.dto.requests.TurnoRequest;
import com.tomadin.turnos.dto.responses.TurnoResponse;
import com.tomadin.turnos.services.ITurnoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/turnos")
public class TurnoController {

    private final ITurnoService turnoService;

    public TurnoController(ITurnoService turnoService) {
        this.turnoService = turnoService;
    }

    // Crear un nuevo turno
    @PostMapping
    public ResponseEntity<TurnoResponse> crear(@Valid @RequestBody TurnoRequest request) {
        TurnoResponse creado = turnoService.save(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(creado.idTurno())
                .toUri();

        return ResponseEntity.created(location).body(creado);
    }

    // Obtener todos los turnos
    @GetMapping
    public ResponseEntity<List<TurnoResponse>> listar() {
        return ResponseEntity.ok(turnoService.getAll());
    }

    // Obtener un turno en particular
    @GetMapping("/{id}")
    public ResponseEntity<TurnoResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(turnoService.findById(id));
    }

    // Editar un turno existente
    @PutMapping("/{id}")
    public ResponseEntity<TurnoResponse> actualizar(@PathVariable Long id,
                                                    @Valid @RequestBody TurnoRequest request) {
        return ResponseEntity.ok(turnoService.update(id, request));
    }

    // Eliminar un turno
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        turnoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
