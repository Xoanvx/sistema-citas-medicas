package pe.edu.utp.citasmedicas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.utp.citasmedicas.model.Consulta;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
}