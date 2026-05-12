package com.edifika.iam.authentication.application.internal.outboundservices.hashing;

/**
 * Interfaz para el servicio de cifrado de contraseñas.
 */
public interface HashingService {
    String encode(CharSequence rawPassword);
    boolean matches(CharSequence rawPassword, String encodedPassword);
}