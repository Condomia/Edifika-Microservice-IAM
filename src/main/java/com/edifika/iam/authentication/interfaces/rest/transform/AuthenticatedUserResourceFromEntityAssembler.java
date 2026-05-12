package com.edifika.iam.authentication.interfaces.rest.transform;

import com.edifika.iam.authentication.domain.model.aggregates.User;
import com.edifika.iam.authentication.interfaces.rest.resources.AuthenticatedUserResource;

/**
 * Convierte un User y token en un AuthenticatedUserResource.
 */
public class AuthenticatedUserResourceFromEntityAssembler {
    public static AuthenticatedUserResource toResourceFromEntity(User user, String token) {
        return new AuthenticatedUserResource(user.getId(), user.getEmail(), token);
    }
}