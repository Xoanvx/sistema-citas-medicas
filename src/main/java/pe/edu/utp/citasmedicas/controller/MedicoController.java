package pe.edu.utp.citasmedicas.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.citasmedicas.model.Medico;
import pe.edu.utp.citasmedicas.repository.MedicoRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping({"/medicos", "/api/medicos"})
public class MedicoController {

    private final MedicoRepository medicoRepository;

    public MedicoController(MedicoRepository medicoRepository) {
        this.medicoRepository = medicoRepository;
    }

    @GetMapping
    public ResponseEntity<List<Medico>> listarTodos() {
        return ResponseEntity.ok(medicoRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Medico> buscarPorId(@PathVariable Long id) {
        Optional<Medico> medico = medicoRepository.findById(id);
        if (medico.isPresent()) {
            return ResponseEntity.ok(medico.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping
    public ResponseEntity<Medico> crear(@RequestBody Medico medico) {
        if (medico.getNombre() == null || medico.getNombre().trim().isEmpty() ||
            medico.getColegiatura() == null || medico.getColegiatura().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Medico guardado = medicoRepository.save(medico);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (medicoRepository.existsById(id)) {
            medicoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}

