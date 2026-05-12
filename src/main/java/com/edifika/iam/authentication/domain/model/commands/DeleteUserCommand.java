package com.edifika.iam.authentication.domain.model.commands;

/** Comando para eliminar un usuario del sistema por su ID. */
public record DeleteUserCommand(Long id) {}