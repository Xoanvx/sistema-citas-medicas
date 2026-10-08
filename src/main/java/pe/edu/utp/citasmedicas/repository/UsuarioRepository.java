package pe.edu.utp.citasmedicas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.utp.citasmedicas.model.Usuario;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);
    boolean existsByUsername(String username);
}