package com.tomadin.pacientes.controllers;

import com.tomadin.pacientes.dto.requests.PacienteRequest;
import com.tomadin.pacientes.dto.responses.PacienteResponse;
import com.tomadin.pacientes.services.IPacienteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/pacientes")
public class PacienteController {

    private final IPacienteService pacienteService;

    public PacienteController(IPacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    // Crear un nuevo paciente
    @PostMapping
    public ResponseEntity<PacienteResponse> crear(@Valid @RequestBody PacienteRequest request) {
        PacienteResponse creado = pacienteService.save(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(creado.idPaciente())
                .toUri();

        return ResponseEntity.created(location).body(creado);
    }

    // Obtener todos los pacientes
    @GetMapping
    public ResponseEntity<List<PacienteResponse>> listar() {
        return ResponseEntity.ok(pacienteService.getAll());
    }

    // Obtener un paciente en particular
    @GetMapping("/{id}")
    public ResponseEntity<PacienteResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(pacienteService.findById(id));
    }

    // Obtener un paciente por su DNI
    @GetMapping("/dni/{dni}")
    public ResponseEntity<PacienteResponse> obtenerPorDni(@PathVariable String dni) {
        return ResponseEntity.ok(pacienteService.findByDni(dni));
    }

    // Editar un paciente existente
    @PutMapping("/{id}")
    public ResponseEntity<PacienteResponse> actualizar(@PathVariable Long id,
                                                       @Valid @RequestBody PacienteRequest request) {
        return ResponseEntity.ok(pacienteService.update(id, request));
    }

    // Eliminar un paciente
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        pacienteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
