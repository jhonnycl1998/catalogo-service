package cibertec.grupo7.catalogoservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class FiltroJwtAuth extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String token = jwtService.extraerToken(request);
            System.out.println("--> [CATALOGO] Petición a: " + request.getRequestURI());
            System.out.println("--> [CATALOGO] Token recibido: " + (token != null ? "SÍ" : "NO (Llegó null)"));

            if (token != null && jwtService.tokenValido(token)) {
                Claims claims = jwtService.obtenerClaims(token);
                System.out.println("--> [CATALOGO] Roles en el token: " + claims.get("authorities"));
                jwtService.generarAuthSecurity(claims);
            } else {
                SecurityContextHolder.clearContext();
            }
            filterChain.doFilter(request, response);
        } catch (JwtException ex) {
            System.out.println("--> [CATALOGO] Error de JWT: " + ex.getMessage());
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.sendError(HttpServletResponse.SC_FORBIDDEN, ex.getMessage());
        }
    }
}