package com.edifika.iam.authentication.domain.model.queries;

/** Query para buscar un usuario por su correo electrónico. */
public record GetUserByEmailQuery(String email) {
}
