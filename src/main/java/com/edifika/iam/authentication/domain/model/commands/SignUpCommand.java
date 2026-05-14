package com.edifika.iam.authentication.domain.model.commands;

import com.edifika.iam.authentication.domain.model.entities.Role;
import com.edifika.iam.authentication.domain.model.valueobjects.Roles;

import java.util.List;

/**
 * Comando para registrar un nuevo usuario en Edifika.
 * Incluye validaciones de formato y seguridad para todos los campos.
 */
public record SignUpCommand(
        String fullName,
        String email,
        String password,
        String phone,
        List<Role> roles
) {
    public SignUpCommand {
        // Validar fullName
        if (fullName == null || fullName.isBlank())
            throw new IllegalArgumentException("El nombre completo no puede estar vacío");
        if (fullName.trim().length() < 3)
            throw new IllegalArgumentException("El nombre completo debe tener al menos 3 caracteres");
        if (!fullName.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$"))
            throw new IllegalArgumentException("El nombre solo puede contener letras y espacios");

        // Validar email
        if (email == null || email.isBlank())
            throw new IllegalArgumentException("El email no puede estar vacío");
        if (email.contains(" "))
            throw new IllegalArgumentException("El email no puede contener espacios");
        if (!email.matches("^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$"))
            throw new IllegalArgumentException("El email debe tener un formato válido, por ejemplo: usuario@gmail.com");

        // Validar password
        if (password == null || password.isBlank())
            throw new IllegalArgumentException("La contraseña no puede estar vacía");
        if (password.length() < 8)
            throw new IllegalArgumentException("La contraseña debe tener mínimo 8 caracteres");
        if (!password.matches(".*[A-Z].*"))
            throw new IllegalArgumentException("La contraseña debe contener al menos una letra mayúscula");
        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*"))
            throw new IllegalArgumentException("La contraseña debe contener al menos un símbolo especial");
        if (password.toLowerCase().contains(fullName.toLowerCase().split(" ")[0].toLowerCase()))
            throw new IllegalArgumentException("La contraseña no puede contener tu nombre");
        if (password.toLowerCase().contains(email.split("@")[0].toLowerCase()))
            throw new IllegalArgumentException("La contraseña no puede contener tu email");

        // Validar phone
        if (phone == null || phone.isBlank())
            throw new IllegalArgumentException("El número de teléfono no puede estar vacío");
        phone = phone.replaceAll("[\\s\\-]", "");
        if (!phone.startsWith("+51")) {
            if (phone.startsWith("51")) {
                phone = "+" + phone;
            } else {
                phone = "+51" + phone;
            }
        }
        String digits = phone.substring(3);
        if (!digits.matches("\\d{9}"))
            throw new IllegalArgumentException("El número de teléfono debe tener 9 dígitos");
        if (!digits.startsWith("9"))
            throw new IllegalArgumentException("El número de teléfono peruano debe empezar con 9");

        // Validar roles
        if (roles == null || roles.isEmpty())
            throw new IllegalArgumentException("Debe asignarse al menos un rol al usuario");
        boolean hasValidRole = roles.stream()
                .anyMatch(r -> r.getName() == Roles.ADMIN ||
                        r.getName() == Roles.OWNER ||
                        r.getName() == Roles.TENANT);
        if (!hasValidRole)
            throw new IllegalArgumentException("El rol asignado no es válido");
    }
}