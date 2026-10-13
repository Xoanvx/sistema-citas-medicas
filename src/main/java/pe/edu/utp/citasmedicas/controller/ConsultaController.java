package pe.edu.utp.citasmedicas.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.citasmedicas.model.Consulta;
import pe.edu.utp.citasmedicas.repository.ConsultaRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping({"/consultas", "/api/consultas"})
public class ConsultaController {

    private final ConsultaRepository consultaRepository;

    public ConsultaController(ConsultaRepository consultaRepository) {
        this.consultaRepository = consultaRepository;
    }

    @GetMapping
    public ResponseEntity<List<Consulta>> listarTodas() {
        return ResponseEntity.ok(consultaRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Consulta> buscarPorId(@PathVariable Long id) {
        Optional<Consulta> consulta = consultaRepository.findById(id);
        if (consulta.isPresent()) {
            return ResponseEntity.ok(consulta.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping
    public ResponseEntity<Consulta> crear(@RequestBody Consulta consulta) {
        if (consulta.getCitaId() == null ||
            consulta.getDiagnostico() == null || consulta.getDiagnostico().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Consulta guardada = consultaRepository.save(consulta);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (consultaRepository.existsById(id)) {
            consultaRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}

