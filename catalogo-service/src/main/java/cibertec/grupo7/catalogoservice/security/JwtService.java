package cibertec.grupo7.catalogoservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class JwtService {
    private static final String CLAVE_SECRETA_UNIFICADA = "MiClaveSecretaSuperSeguraParaProyectoDAW2Grupo7Cibertec2026";
    private final SecretKey key;

    public JwtService() {
        System.out.println("--> [CATALOGO] CLAVE FORZADA: [" + CLAVE_SECRETA_UNIFICADA + "]");
        this.key = Keys.hmacShaKeyFor(CLAVE_SECRETA_UNIFICADA.getBytes(StandardCharsets.UTF_8));
    }
    // ... el resto de métodos queda igual

    public String extraerToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

    public Claims obtenerClaims(String token) {
        return Jwts.parser().verifyWith(this.key).build().parseSignedClaims(token).getPayload();
    }

    public boolean tokenValido(String token) {
        try {
            obtenerClaims(token);
            return true;
        } catch (Exception e) {
            System.out.println("--> [CATALOGO] Falló validación del token: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            return false;
        }
    }

    public void generarAuthSecurity(Claims claims) {
        List<String> authorities = claims.get("authorities", List.class);
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                claims.getSubject(),
                claims.getId(),
                authorities.stream().map(SimpleGrantedAuthority::new).toList()
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}