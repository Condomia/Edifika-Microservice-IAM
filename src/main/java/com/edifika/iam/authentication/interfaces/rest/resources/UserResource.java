package com.edifika.iam.authentication.interfaces.rest.resources;

import java.util.List;

/**
 * Resource de salida que representa los datos de un usuario.
 */
public record UserResource(
        Long id,
        String fullName,
        String email,
        String phone,
        String status,
        List<String> roles
) {}