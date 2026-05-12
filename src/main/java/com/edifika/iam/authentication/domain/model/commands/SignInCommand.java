package com.edifika.iam.authentication.domain.model.commands;

/** Comando para iniciar sesión con email y contraseña. */
public record SignInCommand(String email, String password) {}
