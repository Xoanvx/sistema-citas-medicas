package pe.edu.utp.citasmedicas.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String claveSecreta;

    @Value("${jwt.expiration}")
    private Long tiempoExpiracion;

    private Key obtenerClaveFirma() {
        byte[] arregloBytes = this.claveSecreta.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(arregloBytes);
    }

    public String generarToken(String username) {
        long tiempoActual = System.currentTimeMillis();
        Date fechaInicio = new Date(tiempoActual);
        Date fechaFin = new Date(tiempoActual + this.tiempoExpiracion);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(fechaInicio)
                .setExpiration(fechaFin)
                .signWith(obtenerClaveFirma(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String obtenerUsername(String token) {
        Claims cuerpo = Jwts.parserBuilder()
                .setSigningKey(obtenerClaveFirma())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return cuerpo.getSubject();
    }

    public boolean validarToken(String token, String username) {
        try {
            String usuarioToken = obtenerUsername(token);
            Claims cuerpo = Jwts.parserBuilder()
                    .setSigningKey(obtenerClaveFirma())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            boolean noHaExpirado = cuerpo.getExpiration().after(new Date());
            return usuarioToken.equals(username) && noHaExpirado;
        } catch (Exception e) {
            return false;
        }
    }
}

