package com.edifika.iam.authentication.interfaces.rest;

import com.edifika.iam.authentication.domain.model.entities.Role;
import com.edifika.iam.authentication.domain.model.valueobjects.Roles;
import com.edifika.iam.authentication.infrastructure.persistence.jpa.repositories.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RolesControllerTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RolesController rolesController;

    @Test
    void getAllRoles() {

        var adminRole = new Role(Roles.ADMIN);
        var ownerRole = new Role(Roles.OWNER);

        when(roleRepository.findAll())
                .thenReturn(List.of(adminRole, ownerRole));

        ResponseEntity<?> response =
                rolesController.getAllRoles();

        assertEquals(200, response.getStatusCode().value());

        verify(roleRepository, times(1))
                .findAll();
    }
}