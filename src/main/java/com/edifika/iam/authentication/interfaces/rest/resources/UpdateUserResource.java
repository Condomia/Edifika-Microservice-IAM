package com.edifika.iam.authentication.interfaces.rest.resources;

/**
 * Resource de entrada para actualizar los datos de un usuario.
 */
public record UpdateUserResource(
        String fullName,
        String email,
        String password,
        String phone
) {}