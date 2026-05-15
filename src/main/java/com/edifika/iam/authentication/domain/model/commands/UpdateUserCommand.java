package com.edifika.iam.authentication.domain.model.commands;

import com.edifika.iam.authentication.domain.model.valueobjects.DocumentType;

/**
 * Comando para actualizar los datos de un usuario existente.
 */
public record UpdateUserCommand(
        String fullName,
        String email,
        String password,
        String phone,
        DocumentType documentType,
        String documentNumber,
        String currentEmail,
        String currentPasswordHash
) {
    public UpdateUserCommand {
        if (fullName == null || fullName.isBlank())
            throw new IllegalArgumentException("El nombre completo no puede estar vacío");
        if (!fullName.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$"))
            throw new IllegalArgumentException("El nombre solo puede contener letras y espacios");

        if (email == null || email.isBlank())
            throw new IllegalArgumentException("El email no puede estar vacío");
        if (email.contains(" "))
            throw new IllegalArgumentException("El email no puede contener espacios");
        if (!email.matches("^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$"))
            throw new IllegalArgumentException("El email debe tener un formato válido");
        if (currentEmail != null && email.equalsIgnoreCase(currentEmail))
            throw new IllegalArgumentException("El nuevo email debe ser diferente al actual");

        if (password != null && !password.isBlank()) {
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
        }

        if (phone != null && !phone.isBlank()) {
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
        }

        if (documentType != null && (documentNumber == null || documentNumber.isBlank()))
            throw new IllegalArgumentException("El número de documento no puede estar vacío");
        if (documentType == DocumentType.DNI && documentNumber != null && !documentNumber.matches("\\d{8}"))
            throw new IllegalArgumentException("El DNI debe tener exactamente 8 dígitos");
        if (documentType == DocumentType.CE && documentNumber != null && !documentNumber.matches("[a-zA-Z0-9]{9,12}"))
            throw new IllegalArgumentException("El CE debe tener entre 9 y 12 caracteres alfanuméricos");
    }
}