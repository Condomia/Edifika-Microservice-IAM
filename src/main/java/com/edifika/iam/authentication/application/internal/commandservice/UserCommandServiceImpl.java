package com.edifika.iam.authentication.application.internal.commandservice;


import com.edifika.iam.authentication.application.internal.outboundservices.hashing.HashingService;
import com.edifika.iam.authentication.application.internal.outboundservices.tokens.TokenService;
import com.edifika.iam.authentication.domain.model.aggregates.User;
import com.edifika.iam.authentication.domain.model.commands.*;


import com.edifika.iam.authentication.domain.model.valueobjects.Roles;
import com.edifika.iam.authentication.domain.services.UserCommandService;
import com.edifika.iam.authentication.infrastructure.persistence.jpa.repositories.RoleRepository;
import com.edifika.iam.authentication.infrastructure.persistence.jpa.repositories.UserRepository;
import org.apache.commons.lang3.tuple.ImmutablePair;

import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementación de los comandos del módulo IAM.
 * Maneja registro, login, actualización y eliminación de usuarios.
 */
@Service
public class UserCommandServiceImpl implements UserCommandService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final HashingService hashingService;
    private final TokenService tokenService;

    public UserCommandServiceImpl(UserRepository userRepository,
                                  RoleRepository roleRepository,
                                  HashingService hashingService,
                                  TokenService tokenService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.hashingService = hashingService;
        this.tokenService = tokenService;
    }

    @Override
    public Optional<ImmutablePair<User, String>> handle(SignInCommand signInCommand) {
        var user = userRepository.findByEmail(signInCommand.email());
        if (user.isEmpty())
            throw new IllegalArgumentException("Usuario con email " + signInCommand.email() + " no encontrado");
        if (!hashingService.matches(signInCommand.password(), user.get().getPasswordHash()))
            throw new IllegalArgumentException("Contraseña incorrecta");

        var authenticatedUser = user.get();
        var roles = authenticatedUser.getRoles().stream()
                .map(role -> role.getStringName())
                .collect(Collectors.toList());
        var token = tokenService.generateToken(authenticatedUser.getEmail(),
                authenticatedUser.getId(), roles);
        return Optional.of(ImmutablePair.of(authenticatedUser, token));
    }

    @Override
    public Optional<User> handle(SignUpCommand signUpCommand) {
        if (userRepository.existsByEmail(signUpCommand.email()))
            throw new IllegalArgumentException("El email " + signUpCommand.email() + " ya está registrado");

        // IMPORTANTE: solo se puede registrar con rol ADMIN en este microservicio
        boolean hasNonAdminRole = signUpCommand.roles().stream()
                .anyMatch(r -> r.getName() != Roles.ADMIN);
        if (hasNonAdminRole)
            throw new IllegalArgumentException("En este sistema solo se pueden registrar administradores");

        var roles = signUpCommand.roles().stream().map(
                role -> roleRepository.findByName(role.getName())
                        .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado: " + role.getName()))
        ).toList();

        var user = new User(
                signUpCommand.fullName(),
                signUpCommand.email(),
                hashingService.encode(signUpCommand.password()),
                signUpCommand.phone(),
                roles
        );
        userRepository.save(user);
        return userRepository.findByEmail(signUpCommand.email());
    }

    @Override
    public Optional<User> handle(UpdateUserCommand updateUserCommand, Long userId) {
        var userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty())
            throw new IllegalArgumentException("Usuario con ID " + userId + " no encontrado");

        var userToUpdate = userOptional.get();

        // Actualizar fullName
        userToUpdate.updateFullName(updateUserCommand.fullName());

        // Actualizar email
        userToUpdate.updateEmail(updateUserCommand.email());

        // Actualizar phone
        if (updateUserCommand.phone() != null && !updateUserCommand.phone().isBlank())
            userToUpdate.updatePhone(updateUserCommand.phone());

        // Actualizar password solo si viene
        if (updateUserCommand.password() != null && !updateUserCommand.password().isBlank()) {
            String encodedPassword = hashingService.encode(updateUserCommand.password());
            userToUpdate.changePassword(encodedPassword);
        }

        try {
            return Optional.of(userRepository.save(userToUpdate));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public void handle(DeleteUserCommand deleteUserCommand) {
        if (!userRepository.existsById(deleteUserCommand.id()))
            throw new IllegalArgumentException("Usuario con ID " + deleteUserCommand.id() + " no encontrado");
        try {
            userRepository.deleteById(deleteUserCommand.id());
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar usuario: " + e.getMessage(), e);
        }
    }
}