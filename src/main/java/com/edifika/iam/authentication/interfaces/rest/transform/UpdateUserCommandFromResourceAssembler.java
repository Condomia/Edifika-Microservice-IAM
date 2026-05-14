package com.edifika.iam.authentication.interfaces.rest.transform;

import com.edifika.iam.authentication.domain.model.aggregates.User;
import com.edifika.iam.authentication.domain.model.commands.UpdateUserCommand;
import com.edifika.iam.authentication.interfaces.rest.resources.UpdateUserResource;

/**
 * Convierte un UpdateUserResource en un UpdateUserCommand.
 * Incluye el email y password actuales para validar cambios.
 */
public class UpdateUserCommandFromResourceAssembler {
    public static UpdateUserCommand toCommandFromResource(UpdateUserResource resource, User currentUser) {
        return new UpdateUserCommand(
                resource.fullName(),
                resource.email(),
                resource.password(),
                resource.phone(),
                currentUser.getEmail(),
                currentUser.getPasswordHash()
        );
    }
}