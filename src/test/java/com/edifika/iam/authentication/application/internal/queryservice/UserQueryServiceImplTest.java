package com.edifika.iam.authentication.application.internal.queryservice;

import com.edifika.iam.authentication.domain.model.aggregates.User;
import com.edifika.iam.authentication.domain.model.queries.GetAllUsersQuery;
import com.edifika.iam.authentication.domain.model.queries.GetUserByEmailQuery;
import com.edifika.iam.authentication.domain.model.queries.GetUserByIdQuery;
import com.edifika.iam.authentication.domain.model.queries.ValidateUserRoleQuery;
import com.edifika.iam.authentication.domain.model.valueobjects.Roles;
import com.edifika.iam.authentication.infrastructure.persistence.jpa.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserQueryServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserQueryServiceImpl userQueryService;

    @Test
    void handleGetAllUsersSuccessfully() {

        var user1 = mock(User.class);
        var user2 = mock(User.class);

        when(userRepository.findAll())
                .thenReturn(List.of(user1, user2));

        var query = new GetAllUsersQuery();

        List<User> result = userQueryService.handle(query);

        assertEquals(2, result.size());

        verify(userRepository, times(1))
                .findAll();
    }

    @Test
    void handleGetUserByIdSuccessfully() {

        var user = mock(User.class);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        var query = new GetUserByIdQuery(1L);

        Optional<User> result = userQueryService.handle(query);

        assertTrue(result.isPresent());

        verify(userRepository, times(1))
                .findById(1L);
    }

    @Test
    void handleGetUserByEmailSuccessfully() {

        var user = mock(User.class);

        when(userRepository.findByEmail("admin@test.com"))
                .thenReturn(Optional.of(user));

        var query = new GetUserByEmailQuery("admin@test.com");

        Optional<User> result = userQueryService.handle(query);

        assertTrue(result.isPresent());

        verify(userRepository, times(1))
                .findByEmail("admin@test.com");
    }

    @Test
    void handleValidateUserRoleSuccessfully() {

        var user = mock(User.class);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(user.hasRole(Roles.ADMIN))
                .thenReturn(true);

        var query = new ValidateUserRoleQuery(
                1L,
                "ADMIN"
        );

        boolean result = userQueryService.handle(query);

        assertTrue(result);

        verify(userRepository, times(1))
                .findById(1L);
    }
}