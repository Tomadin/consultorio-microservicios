package com.tomadin.turnos.services;

import com.tomadin.turnos.client.PacienteClient;
import com.tomadin.turnos.client.PacienteDTO;
import com.tomadin.turnos.dto.requests.TurnoRequest;
import com.tomadin.turnos.dto.responses.TurnoResponse;
import com.tomadin.turnos.entities.Turno;
import com.tomadin.turnos.exceptions.NotFoundException;
import com.tomadin.turnos.mappers.TurnoMapper;
import com.tomadin.turnos.repositories.TurnoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TurnoServiceTest {

    @Mock
    private TurnoRepository repository;
    @Mock
    private TurnoMapper mapper;
    @Mock
    private PacienteClient pacienteClient;

    @InjectMocks
    private TurnoService service;

    @Test
    void save_armaNombreCompletoDesdeElClienteYPersiste() {
        TurnoRequest request = new TurnoRequest(LocalDate.now(), "Limpieza", "12345678");
        Turno entity = new Turno();
        entity.setDniPaciente("12345678");

        when(mapper.toEntity(request)).thenReturn(entity);
        when(pacienteClient.buscarPorDni("12345678"))
                .thenReturn(new PacienteDTO(1L, "Ana", "Pérez", "12345678", LocalDate.of(1990, 1, 1), "1122334455"));
        when(repository.save(any(Turno.class))).thenAnswer(inv -> inv.getArgument(0));
        when(mapper.toResponse(any(Turno.class)))
                .thenReturn(new TurnoResponse(1L, request.fecha(), "Limpieza", "12345678", "Ana Pérez"));

        TurnoResponse response = service.save(request);

        ArgumentCaptor<Turno> captor = ArgumentCaptor.forClass(Turno.class);
        org.mockito.Mockito.verify(repository).save(captor.capture());
        assertThat(captor.getValue().getNombreCompletoPaciente()).isEqualTo("Ana Pérez");
        assertThat(response.nombreCompletoPaciente()).isEqualTo("Ana Pérez");
    }

    @Test
    void findById_inexistente_lanzaNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("99");
    }
}
