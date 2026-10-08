package pe.edu.utp.citasmedicas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.utp.citasmedicas.model.Medico;

public interface MedicoRepository extends JpaRepository<Medico, Long> {
}