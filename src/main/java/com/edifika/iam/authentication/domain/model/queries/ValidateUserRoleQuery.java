package com.edifika.iam.authentication.domain.model.queries;

/** Query para validar si un usuario tiene un rol específico asignado. */
public record ValidateUserRoleQuery(Long userId, String roleName) {
}