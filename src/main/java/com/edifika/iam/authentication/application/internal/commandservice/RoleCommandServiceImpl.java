package com.edifika.iam.authentication.application.internal.commandservice;

import com.edifika.iam.authentication.domain.model.commands.SeedRolesCommand;
import com.edifika.iam.authentication.domain.model.entities.Role;
import com.edifika.iam.authentication.domain.model.valueobjects.Roles;
import com.edifika.iam.authentication.domain.services.RoleCommandService;

import com.edifika.iam.authentication.infrastructure.persistence.jpa.repositories.RoleRepository;
import org.springframework.stereotype.Service;
import java.util.Arrays;

/**
 * Implementación del comando de inicialización de roles.
 * Crea los roles por defecto si no existen al arrancar la app.
 */
@Service
public class RoleCommandServiceImpl implements RoleCommandService {

    private final RoleRepository roleRepository;

    public RoleCommandServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void handle(SeedRolesCommand seedRolesCommand) {
        Arrays.stream(Roles.values()).forEach(role -> {
            if (!roleRepository.existsByName(role)) {
                roleRepository.save(new Role(role));
            }
        });
    }
}