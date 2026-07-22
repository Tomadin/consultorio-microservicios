package com.tomadin.pacientes.repositories;

import com.tomadin.pacientes.entities.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    boolean existsByDni(String dni);

    boolean existsByDniAndIdPacienteNot(String dni, Long idCliente);

    Optional<Paciente> findByDni(String dni);
}
