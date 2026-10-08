package pe.edu.utp.citasmedicas.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.citasmedicas.model.Cita;
import pe.edu.utp.citasmedicas.repository.CitaRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/citas")
public class CitaController {

    private final CitaRepository citaRepository;

    public CitaController(CitaRepository citaRepository) {
        this.citaRepository = citaRepository;
    }

    @GetMapping
    public ResponseEntity<List<Cita>> listarTodas() {
        return ResponseEntity.ok(citaRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cita> buscarPorId(@PathVariable Long id) {
        Optional<Cita> cita = citaRepository.findById(id);
        if (cita.isPresent()) {
            return ResponseEntity.ok(cita.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping
    public ResponseEntity<Cita> crear(@RequestBody Cita cita) {
        if (cita.getPacienteId() == null || cita.getMedicoId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Cita guardada = citaRepository.save(cita);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (citaRepository.existsById(id)) {
            citaRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}