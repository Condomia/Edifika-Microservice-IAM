package com.edifika.iam.authentication.interfaces.rest;

import com.edifika.iam.authentication.domain.model.aggregates.User;
import com.edifika.iam.authentication.domain.model.commands.DeleteUserCommand;
import com.edifika.iam.authentication.domain.model.commands.UpdateUserCommand;
import com.edifika.iam.authentication.domain.model.queries.GetAllUsersQuery;
import com.edifika.iam.authentication.domain.model.queries.GetUserByEmailQuery;
import com.edifika.iam.authentication.domain.model.queries.GetUserByIdQuery;
import com.edifika.iam.authentication.domain.model.valueobjects.DocumentType;
import com.edifika.iam.authentication.domain.services.UserCommandService;
import com.edifika.iam.authentication.domain.services.UserQueryService;
import com.edifika.iam.authentication.interfaces.rest.resources.UpdateUserResource;
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
class UserControllerTest {

    @Mock
    private UserQueryService userQueryService;

    @Mock
    private UserCommandService userCommandService;

    @InjectMocks
    private UserController userController;

    @Test
    void getAllUsers() {

        var user1 = mock(User.class);
        var user2 = mock(User.class);

        when(userQueryService.handle((GetAllUsersQuery) any()))
                .thenReturn(List.of(user1, user2));

        ResponseEntity<?> response =
                userController.getAllUsers();

        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void getUserById() {

        var user = mock(User.class);

        when(userQueryService.handle((GetUserByIdQuery) any()))
                .thenReturn(Optional.of(user));

        ResponseEntity<?> response =
                userController.getUserById(1L);

        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void updateUser() {

        var currentUser = mock(User.class);

        var resource = new UpdateUserResource(
                "Nuevo Nombre",
                "nuevo@test.com",
                "Hola_1234",
                "999999999",
                DocumentType.DNI,
                "87654321"
        );

        when(userQueryService.handle((GetUserByIdQuery) any()))
                .thenReturn(Optional.of(currentUser));

        when(userCommandService.handle((UpdateUserCommand) any(), eq(1L)))
                .thenReturn(Optional.of(currentUser));

        ResponseEntity<?> response =
                userController.updateUser(1L, resource);

        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void deleteUser() {

        doNothing().when(userCommandService)
                .handle((DeleteUserCommand) any());

        ResponseEntity<?> response =
                userController.deleteUser(1L);

        assertEquals(204, response.getStatusCode().value());


    }
}