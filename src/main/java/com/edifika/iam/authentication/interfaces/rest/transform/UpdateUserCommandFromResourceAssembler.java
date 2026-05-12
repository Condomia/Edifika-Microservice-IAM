package com.edifika.iam.authentication.interfaces.rest.transform;

import com.edifika.iam.authentication.domain.model.commands.UpdateUserCommand;
import com.edifika.iam.authentication.interfaces.rest.resources.UpdateUserResource;

/**
 * Convierte un UpdateUserResource en un UpdateUserCommand.
 */
public class UpdateUserCommandFromResourceAssembler {
    public static UpdateUserCommand toCommandFromResource(UpdateUserResource resource) {
        return new UpdateUserCommand(
                resource.fullName(),
                resource.email(),
                resource.password(),
                resource.phone()
        );
    }
}