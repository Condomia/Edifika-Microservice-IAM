package com.edifika.iam.authentication.interfaces.rest.resources;

/**
 * Resource de salida tras autenticación exitosa.
 * Contiene el id, email y token JWT del usuario.
 */
public record AuthenticatedUserResource(Long id, String email, String token) {}