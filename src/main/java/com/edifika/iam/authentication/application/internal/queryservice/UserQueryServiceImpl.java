package com.edifika.iam.authentication.application.internal.queryservice;

import com.edifika.iam.authentication.domain.model.aggregates.User;
import com.edifika.iam.authentication.domain.model.queries.*;
import com.edifika.iam.authentication.domain.services.UserQueryService;
import com.edifika.iam.authentication.domain.model.valueobjects.Roles;
import com.edifika.iam.authentication.infrastructure.persistence.jpa.repositories.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Implementación de las consultas del módulo IAM.
 * Permite buscar usuarios por distintos criterios.
 */
@Service
public class UserQueryServiceImpl implements UserQueryService {

    private final UserRepository userRepository;

    public UserQueryServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> handle(GetAllUsersQuery getAllUsersQuery) {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> handle(GetUserByIdQuery getUserByIdQuery) {
        return userRepository.findById(getUserByIdQuery.id());
    }

    @Override
    public Optional<User> handle(GetUserByEmailQuery getUserByEmailQuery) {
        return userRepository.findByEmail(getUserByEmailQuery.email());
    }

    @Override
    public boolean handle(ValidateUserRoleQuery validateUserRoleQuery) {
        return userRepository.findById(validateUserRoleQuery.userId())
                .map(user -> user.hasRole(Roles.valueOf(validateUserRoleQuery.roleName())))
                .orElse(false);
    }
}