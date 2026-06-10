package com.edifika.iam.authentication.application.internal.commandservice;

import com.edifika.iam.authentication.domain.model.commands.SeedRolesCommand;
import com.edifika.iam.authentication.domain.model.entities.Role;
import com.edifika.iam.authentication.domain.model.valueobjects.Roles;
import com.edifika.iam.authentication.infrastructure.persistence.jpa.repositories.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleCommandServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleCommandServiceImpl roleCommandService;

    private SeedRolesCommand seedRolesCommand;

    @BeforeEach
    void setUp() {
        seedRolesCommand = new SeedRolesCommand();
    }

    @Test
    void shouldSaveRoleWhenRoleDoesNotExist() {

        when(roleRepository.existsByName(ArgumentMatchers.any(Roles.class)))
                .thenReturn(false);

        roleCommandService.handle(seedRolesCommand);

        verify(roleRepository, times(Roles.values().length))
                .save(any(Role.class));
    }

    @Test
    void shouldNotSaveRoleWhenRoleAlreadyExists() {

        when(roleRepository.existsByName(ArgumentMatchers.any(Roles.class)))
                .thenReturn(true);

        roleCommandService.handle(seedRolesCommand);

        verify(roleRepository, never())
                .save(any(Role.class));
    }
}