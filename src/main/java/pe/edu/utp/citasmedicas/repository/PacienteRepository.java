package pe.edu.utp.citasmedicas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.utp.citasmedicas.model.Paciente;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
}