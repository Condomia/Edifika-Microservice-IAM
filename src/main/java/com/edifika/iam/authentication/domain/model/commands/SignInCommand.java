package com.edifika.iam.authentication.domain.model.commands;

/**
 * Comando para iniciar sesión con email y contraseña.
 */
public record SignInCommand(String email, String password) {
    public SignInCommand {
        if (email == null || email.isBlank())
            throw new IllegalArgumentException("El email no puede estar vacío");
        if (!email.matches("^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$"))
            throw new IllegalArgumentException("El email debe tener un formato válido");
        if (password == null || password.isBlank())
            throw new IllegalArgumentException("La contraseña no puede estar vacía");
    }
}