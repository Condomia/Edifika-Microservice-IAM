package com.edifika.iam.authentication.domain.services;

import com.edifika.iam.authentication.domain.model.commands.SeedRolesCommand;

/**
 * Interfaz para el comando de inicialización de roles.
 * Se ejecuta al arrancar la aplicación.
 */
public interface RoleCommandService {
    void handle(SeedRolesCommand seedRolesCommand);
}