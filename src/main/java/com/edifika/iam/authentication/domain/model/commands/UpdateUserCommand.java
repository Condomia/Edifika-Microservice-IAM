package com.edifika.iam.authentication.domain.model.commands;

/**
 * Comando para actualizar los datos de un usuario existente.
 */
public record UpdateUserCommand(
        String fullName,
        String email,
        String password,
        String phone
) {
    public UpdateUserCommand {
        if (fullName == null || fullName.isBlank())
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        if (email == null || email.isBlank())
            throw new IllegalArgumentException("El email no puede estar vacío");
    }
}