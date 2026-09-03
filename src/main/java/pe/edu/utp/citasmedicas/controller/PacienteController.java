package pe.edu.utp.citasmedicas.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.citasmedicas.model.Paciente;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    private final List<Paciente> pacientes = new ArrayList<>();

    public PacienteController() {
        pacientes.add(new Paciente(1L, "Juan Perez", "12345678", "juan@test.com"));
        pacientes.add(new Paciente(2L, "Maria Lopez", "87654321", "maria@test.com"));
    }

    @GetMapping
    public ResponseEntity<List<Paciente>> listarTodos() {
        return ResponseEntity.ok(pacientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Paciente> buscarPorId(@PathVariable Long id) {
        return pacientes.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<Paciente> crear(@RequestBody Paciente paciente) {
        if (paciente.getDni() == null || paciente.getDni().trim().isEmpty() ||
            paciente.getNombre() == null || paciente.getNombre().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        paciente.setId((long) (pacientes.size() + 1));
        pacientes.add(paciente);
        return ResponseEntity.status(HttpStatus.CREATED).body(paciente);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        boolean eliminado = pacientes.removeIf(p -> p.getId().equals(id));
        if (eliminado) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
