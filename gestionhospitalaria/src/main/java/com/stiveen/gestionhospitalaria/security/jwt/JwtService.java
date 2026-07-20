package com.stiveen.gestionhospitalaria.security.jwt;

import com.stiveen.gestionhospitalaria.entity.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    /**
     * Clave utilizada para firmar y validar los tokens JWT.
     * En producción deberá obtenerse desde variables de entorno
     * o archivos de configuración seguros.
     */
    private static final String CLAVE_SECRETA =
            "gestion-hospitalaria-clave-jwt-segura-2026-stiveen";

    /**
     * Tiempo de vida del token.
     * 8 horas.
     */
    private static final long TIEMPO_EXPIRACION_MILISEGUNDOS =
            1000L * 60 * 60 * 8;

    /**
     * Obtiene la llave criptográfica utilizada para firmar
     * y validar los JWT.
     */
    private SecretKey obtenerClaveFirma() {

        return Keys.hmacShaKeyFor(
                CLAVE_SECRETA.getBytes(StandardCharsets.UTF_8)
        );
    }

    /**
     * Genera un JWT para el usuario autenticado.
     */
    public String generarToken(Usuario usuario) {

        return Jwts.builder()
                .subject(usuario.getNumeroDocumento())
                .claim("rol", usuario.getRol().getNombre())
                .claim("nombreCompleto", usuario.getNombreCompleto())
                .issuedAt(new Date())
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + TIEMPO_EXPIRACION_MILISEGUNDOS
                        )
                )
                .signWith(obtenerClaveFirma())
                .compact();
    }

    /**
     * Obtiene el nombre del usuario almacenado en el JWT.
     */
    public String obtenerIdentificadorUsuario(String tokenJwt) {

        return obtenerClaim(
                tokenJwt,
                Claims::getSubject
        );
    }

    /**
     * Obtiene la fecha de expiración del token.
     */
    public Date obtenerFechaExpiracion(String tokenJwt) {

        return obtenerClaim(
                tokenJwt,
                Claims::getExpiration
        );
    }

    /**
     * Obtiene cualquier claim del JWT.
     */
    public <T> T obtenerClaim(
            String tokenJwt,
            Function<Claims, T> resolverClaim
    ) {

        Claims claims = obtenerTodosLosClaims(tokenJwt);

        return resolverClaim.apply(claims);
    }

    /**
     * Obtiene todos los claims contenidos en el JWT.
     */
    private Claims obtenerTodosLosClaims(String tokenJwt) {

        return Jwts.parser()
                .verifyWith(obtenerClaveFirma())
                .build()
                .parseSignedClaims(tokenJwt)
                .getPayload();
    }

    /**
     * Indica si el token ya expiró.
     */
    public boolean estaExpirado(String tokenJwt) {

        return obtenerFechaExpiracion(tokenJwt)
                .before(new Date());
    }

    /**
     * Valida que el token pertenezca al usuario indicado
     * y que además no esté expirado.
     */
    public boolean esTokenValido(
            String tokenJwt,
            Usuario usuario
    ) {

        String numeroDocumento =
                obtenerIdentificadorUsuario(tokenJwt);

        return numeroDocumento.equals(usuario.getNumeroDocumento())
                && !estaExpirado(tokenJwt);

    }

}