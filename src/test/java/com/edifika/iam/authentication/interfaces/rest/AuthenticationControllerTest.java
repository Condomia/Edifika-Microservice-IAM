package com.edifika.iam.authentication.interfaces.rest;

import com.edifika.iam.authentication.domain.model.aggregates.User;
import com.edifika.iam.authentication.domain.model.commands.SignInCommand;
import com.edifika.iam.authentication.domain.model.commands.SignUpCommand;
import com.edifika.iam.authentication.domain.model.entities.Role;
import com.edifika.iam.authentication.domain.model.valueobjects.DocumentType;
import com.edifika.iam.authentication.domain.model.valueobjects.Roles;
import com.edifika.iam.authentication.domain.services.UserCommandService;
import com.edifika.iam.authentication.infrastructure.persistence.jpa.repositories.RoleRepository;
import com.edifika.iam.authentication.interfaces.rest.resources.SignInResource;
import com.edifika.iam.authentication.interfaces.rest.resources.SignUpResource;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @Mock
    private UserCommandService userCommandService;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private AuthenticationController authenticationController;

    @Test
    void signIn() {

        var resource = new SignInResource(
                "admin@test.com",
                "Hola_1243"
        );

        var user = mock(User.class);

        when(userCommandService.handle((SignInCommand) any()))
                .thenReturn(Optional.of(
                        ImmutablePair.of(user, "fake-token")
                ));

        ResponseEntity<?> response =
                authenticationController.signIn(resource);

        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void signUp() {

        var resource = new SignUpResource(
                "Admin",
                "admin@test.com",
                "Hola_1243",
                "999999999",
                DocumentType.DNI,
                "12345678",
                List.of("ADMIN")
        );

        var user = mock(User.class);
        var role = new Role(Roles.ADMIN);

        when(roleRepository.findByName(any()))
                .thenReturn(Optional.of(role));
        when(userCommandService.handle((SignUpCommand) any()))
                .thenReturn(Optional.of(user));

        ResponseEntity<?> response =
                authenticationController.signUp(resource);

        assertEquals(201, response.getStatusCode().value());
    }
}