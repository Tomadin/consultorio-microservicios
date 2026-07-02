package com.tomadin.pacientes.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter @Setter
public class Paciente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_paciente")
    private Long idPaciente;
    private String nombre;
    private String apellido;
    @Column(unique = true, nullable = false)
    private String dni;
    @Column(name = "fecha_nac")
    private LocalDate fechaNac;
    private String telefono;

    public Paciente() {
    }

    public Paciente(String nombre, String apellido, String dni, LocalDate fechaNac, String telefono) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.fechaNac = fechaNac;
        this.telefono = telefono;
    }
}
