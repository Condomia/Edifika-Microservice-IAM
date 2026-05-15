package com.edifika.iam.authentication.interfaces.rest.resources;

import com.edifika.iam.authentication.domain.model.valueobjects.DocumentType;

import java.util.List;

/**
 * Resource de entrada para el registro de un nuevo usuario.
 */
public record SignUpResource(
        String fullName,
        String email,
        String password,
        String phone,
        DocumentType documentType,
        String documentNumber,
        List<String> roles
) {}