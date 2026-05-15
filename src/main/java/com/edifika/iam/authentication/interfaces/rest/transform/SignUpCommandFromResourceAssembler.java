package com.edifika.iam.authentication.interfaces.rest.transform;

import com.edifika.iam.authentication.domain.model.commands.SignUpCommand;
import com.edifika.iam.authentication.domain.model.entities.Role;
import com.edifika.iam.authentication.domain.model.valueobjects.Roles;
import com.edifika.iam.authentication.infrastructure.persistence.jpa.repositories.RoleRepository;
import com.edifika.iam.authentication.interfaces.rest.resources.SignUpResource;
import java.util.List;

/**
 * Convierte un SignUpResource en un SignUpCommand.
 */
public class SignUpCommandFromResourceAssembler {
    public static SignUpCommand toCommandFromResource(SignUpResource resource,
                                                      RoleRepository roleRepository) {
        var roles = resource.roles().stream()
                .map(r -> roleRepository.findByName(Roles.valueOf(r))
                        .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado: " + r)))
                .toList();
        return new SignUpCommand(
                resource.fullName(),
                resource.email(),
                resource.password(),
                resource.phone(),
                resource.documentType(),
                resource.documentNumber(),
                roles
        );
    }
}