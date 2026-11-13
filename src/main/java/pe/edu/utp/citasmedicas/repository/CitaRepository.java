package pe.edu.utp.citasmedicas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.utp.citasmedicas.model.Cita;

import java.time.LocalDate;
import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Long> {
    List<Cita> findByPacienteId(Long pacienteId);
    List<Cita> findByFechaBetween(LocalDate fechaInicio, LocalDate fechaFin);
}
