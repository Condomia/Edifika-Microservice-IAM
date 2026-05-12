package com.edifika.iam.authentication.domain.services;


import com.edifika.iam.authentication.domain.model.aggregates.User;
import com.edifika.iam.authentication.domain.model.commands.*;
import org.apache.commons.lang3.tuple.ImmutablePair;
import java.util.Optional;

/**
 * Interfaz que define los casos de uso de escritura para el módulo IAM.
 * Maneja registro, login, actualización y eliminación de usuarios.
 */
public interface UserCommandService {
    Optional<ImmutablePair<User, String>> handle(SignInCommand signInCommand);

    Optional<User> handle(SignUpCommand signUpCommand);

    Optional<User> handle(UpdateUserCommand updateUserCommand, Long userId);

    void handle(DeleteUserCommand deleteUserCommand);

}
