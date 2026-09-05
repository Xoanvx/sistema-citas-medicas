package pe.edu.utp.citasmedicas.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.citasmedicas.model.Cita;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/citas")
public class CitaController {

    private final List<Cita> citas = new ArrayList<>();

    public CitaController() {
        citas.add(new Cita(1L, 1L, 1L, "PENDIENTE"));
        citas.add(new Cita(2L, 2L, 2L, "CONFIRMADA"));
    }

    // GET /citas
    @GetMapping
    public ResponseEntity<List<Cita>> listarTodas() {
        return ResponseEntity.ok(citas);
    }

    // GET /citas/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Cita> buscarPorId(@PathVariable Long id) {
        return citas.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // POST /citas
    @PostMapping
    public ResponseEntity<Cita> crear(@RequestBody Cita cita) {

        if (cita.getPacienteId() == null ||
            cita.getMedicoId() == null ||
            cita.getEstado() == null ||
            cita.getEstado().trim().isEmpty()) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        cita.setId((long) (citas.size() + 1));
        citas.add(cita);

        return ResponseEntity.status(HttpStatus.CREATED).body(cita);
    }

    // PUT /citas/{id}/estado
    @PutMapping("/{id}/estado")
    public ResponseEntity<Cita> actualizarEstado(
            @PathVariable Long id,
            @RequestBody String nuevoEstado) {

        return citas.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .map(cita -> {
                    cita.setEstado(nuevoEstado);
                    return ResponseEntity.ok(cita);
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}