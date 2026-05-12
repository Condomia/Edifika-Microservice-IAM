package com.edifika.iam.authentication.application.internal.outboundservices.tokens;


import org.springframework.security.core.Authentication;
import java.util.List;

/**
 * Interfaz para el servicio de generación y validación de tokens JWT.
 */
public interface TokenService {
    String generateToken(Authentication authentication);

    String generateToken(String email, Long userId, List<String> roles);

    String getUserNameFromToken(String token);

    boolean validateToken(String token);
    
}