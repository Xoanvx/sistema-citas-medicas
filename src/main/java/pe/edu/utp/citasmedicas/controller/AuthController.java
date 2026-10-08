package pe.edu.utp.citasmedicas.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final pe.edu.utp.citasmedicas.repository.UsuarioRepository repositorioUsuario;
    private final org.springframework.security.crypto.password.PasswordEncoder encriptador;
    private final pe.edu.utp.citasmedicas.security.JwtUtil utilJwt;

    public AuthController(
            pe.edu.utp.citasmedicas.repository.UsuarioRepository repositorioUsuario,
            org.springframework.security.crypto.password.PasswordEncoder encriptador,
            pe.edu.utp.citasmedicas.security.JwtUtil utilJwt) {
        this.repositorioUsuario = repositorioUsuario;
        this.encriptador = encriptador;
        this.utilJwt = utilJwt;
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@RequestBody pe.edu.utp.citasmedicas.model.Usuario nuevoUsuario) {
        if (this.repositorioUsuario.existsByUsername(nuevoUsuario.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        nuevoUsuario.setPassword(this.encriptador.encode(nuevoUsuario.getPassword()));
        pe.edu.utp.citasmedicas.model.Usuario guardado = this.repositorioUsuario.save(nuevoUsuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credenciales) {
        String usuario = credenciales.get("username");
        String clave = credenciales.get("password");

        Optional<pe.edu.utp.citasmedicas.model.Usuario> encontrado = this.repositorioUsuario.findByUsername(usuario);
        if (encontrado.isEmpty() || !this.encriptador.matches(clave, encontrado.get().getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String tokenGenerado = this.utilJwt.generarToken(usuario);
        return ResponseEntity.ok(Collections.singletonMap("token", tokenGenerado));
    }
}

