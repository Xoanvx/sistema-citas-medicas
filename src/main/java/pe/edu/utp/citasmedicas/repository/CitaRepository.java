package pe.edu.utp.citasmedicas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.utp.citasmedicas.model.Cita;

public interface CitaRepository extends JpaRepository<Cita, Long> {
}