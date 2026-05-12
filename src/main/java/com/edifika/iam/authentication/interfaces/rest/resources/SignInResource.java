package com.edifika.iam.authentication.interfaces.rest.resources;

/**
 * Resource de entrada para el inicio de sesión.
 */
public record SignInResource(String email, String password) {}