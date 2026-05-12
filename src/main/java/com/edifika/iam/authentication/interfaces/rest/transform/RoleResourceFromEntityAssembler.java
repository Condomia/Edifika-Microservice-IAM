package com.edifika.iam.authentication.interfaces.rest.transform;

import com.edifika.iam.authentication.domain.model.entities.Role;
import com.edifika.iam.authentication.interfaces.rest.resources.RoleResource;

/**
 * Convierte un Role en un RoleResource.
 */
public class RoleResourceFromEntityAssembler {
    public static RoleResource toResourceFromEntity(Role role) {
        return new RoleResource(role.getId(), role.getStringName());
    }
}