package com.edifika.iam.authentication.domain.services;

import com.edifika.iam.authentication.domain.model.aggregates.User;
import com.edifika.iam.authentication.domain.model.queries.*;
import java.util.List;
import java.util.Optional;

import com.edifika.iam.authentication.domain.model.aggregates.User;
import com.edifika.iam.authentication.domain.model.queries.*;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz que define los casos de uso de lectura para usuarios.
 * Permite buscar usuarios por distintos criterios.
 */
public interface UserQueryService {
    List<User> handle(GetAllUsersQuery getAllUsersQuery);

    Optional<User> handle(GetUserByIdQuery getUserByIdQuery);

    Optional<User> handle(GetUserByEmailQuery getUserByEmailQuery);

    boolean handle(ValidateUserRoleQuery validateUserRoleQuery);

}