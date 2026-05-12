package com.edifika.iam.authentication.interfaces.rest.transform;

import com.edifika.iam.authentication.domain.model.aggregates.User;
import com.edifika.iam.authentication.interfaces.rest.resources.UserResource;
import java.util.List;

/**
 * Convierte un User en un UserResource.
 */
public class UserResourceFromEntityAssembler {
    public static UserResource toResourceFromEntity(User user) {
        List<String> roles = user.getRoles().stream()
                .map(role -> role.getName().name())
                .toList();
        return new UserResource(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getPhone(),
                user.getStatus(),
                roles
        );
    }
}