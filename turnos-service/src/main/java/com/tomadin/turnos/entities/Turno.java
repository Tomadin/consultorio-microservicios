package com.tomadin.turnos.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter @Setter
public class Turno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_turno")
    private Long idTurno;
    private LocalDate fecha;
    private String tratamiento;
    @Column(name = "dni_paciente", nullable = false)
    private String dniPaciente;
    @Column(name = "nombre_completo_paciente")
    private String nombreCompletoPaciente;

    public Turno() {
    }

    public Turno(LocalDate fecha, String tratamiento, String dniPaciente, String nombreCompletoPaciente) {
        this.fecha = fecha;
        this.tratamiento = tratamiento;
        this.dniPaciente = dniPaciente;
        this.nombreCompletoPaciente = nombreCompletoPaciente;
    }
}
