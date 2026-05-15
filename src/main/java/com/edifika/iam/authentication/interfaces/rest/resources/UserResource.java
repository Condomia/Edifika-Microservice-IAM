package com.edifika.iam.authentication.interfaces.rest.resources;

import com.edifika.iam.authentication.domain.model.valueobjects.DocumentType;

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
        DocumentType documentType,
        String documentNumber,
        List<String> roles
) {}