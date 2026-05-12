package com.edifika.iam.authentication.interfaces.rest.transform;

import com.edifika.iam.authentication.domain.model.commands.SignInCommand;
import com.edifika.iam.authentication.interfaces.rest.resources.SignInResource;

/**
 * Convierte un SignInResource en un SignInCommand.
 */
public class SignInCommandFromResourceAssembler {
    public static SignInCommand toCommandFromResource(SignInResource resource) {
        return new SignInCommand(resource.email(), resource.password());
    }
}