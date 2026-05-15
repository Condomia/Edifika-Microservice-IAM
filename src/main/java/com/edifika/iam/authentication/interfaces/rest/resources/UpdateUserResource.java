package com.edifika.iam.authentication.interfaces.rest.resources;

import com.edifika.iam.authentication.domain.model.valueobjects.DocumentType;

/**
 * Resource de entrada para actualizar los datos de un usuario.
 */
public record UpdateUserResource(
        String fullName,
        String email,
        String password,
        String phone,
        DocumentType documentType,
        String documentNumber
) {}