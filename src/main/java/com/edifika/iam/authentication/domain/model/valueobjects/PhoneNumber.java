package com.edifika.iam.authentication.domain.model.valueobjects;

/**
 * Value object que representa un número de teléfono peruano.
 * Agrega automáticamente el prefijo +51 si no lo tiene.
 */
public record PhoneNumber(String value) {

    public PhoneNumber {
        if (value == null || value.isBlank())
            throw new IllegalArgumentException("El número de teléfono no puede estar vacío");

        // Limpia espacios y guiones
        value = value.replaceAll("[\\s\\-]", "");

        // Agrega +51 automáticamente si no lo tiene
        if (value.startsWith("+51")) {
            value = value;
        } else if (value.startsWith("51")) {
            value = "+" + value;
        } else {
            value = "+51" + value;
        }

        // Valida que después del +51 haya exactamente 9 dígitos
        String digits = value.substring(3);
        if (!digits.matches("\\d{9}"))
            throw new IllegalArgumentException("El número de teléfono debe tener 9 dígitos");
    }
}
