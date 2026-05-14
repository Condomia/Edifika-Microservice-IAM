package com.edifika.iam.authentication.interfaces.rest;

import com.edifika.iam.authentication.domain.model.commands.DeleteUserCommand;
import com.edifika.iam.authentication.domain.model.queries.GetAllUsersQuery;
import com.edifika.iam.authentication.domain.model.queries.GetUserByIdQuery;
import com.edifika.iam.authentication.domain.services.UserCommandService;
import com.edifika.iam.authentication.domain.services.UserQueryService;
import com.edifika.iam.authentication.interfaces.rest.resources.UpdateUserResource;
import com.edifika.iam.authentication.interfaces.rest.resources.UserResource;
import com.edifika.iam.authentication.interfaces.rest.transform.UpdateUserCommandFromResourceAssembler;
import com.edifika.iam.authentication.interfaces.rest.transform.UserResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controlador REST para la gestión de usuarios.
 * Permite listar, obtener, actualizar y eliminar usuarios.
 */
@RestController
@RequestMapping(value = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Users", description = "Endpoints de gestión de usuarios")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final UserQueryService userQueryService;
    private final UserCommandService userCommandService;

    public UserController(UserQueryService userQueryService, UserCommandService userCommandService) {
        this.userQueryService = userQueryService;
        this.userCommandService = userCommandService;
    }

    @GetMapping
    public ResponseEntity<List<UserResource>> getAllUsers() {
        var users = userQueryService.handle(new GetAllUsersQuery());
        LOGGER.info("Consultando todos los usuarios, total: {}", users.size());
        var resources = users.stream()
                .map(UserResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResource> getUserById(@PathVariable Long id) {
        var user = userQueryService.handle(new GetUserByIdQuery(id));
        if (user.isEmpty()) {
            LOGGER.warn("Usuario no encontrado con ID: {}", id);
            return ResponseEntity.notFound().build();
        }
        LOGGER.info("Usuario encontrado con ID: {}", id);
        return ResponseEntity.ok(UserResourceFromEntityAssembler.toResourceFromEntity(user.get()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id,
                                        @RequestBody UpdateUserResource resource) {
        try {
            var currentUser = userQueryService.handle(new GetUserByIdQuery(id));
            if (currentUser.isEmpty()) {
                LOGGER.warn("Usuario no encontrado con ID: {}", id);
                return ResponseEntity.notFound().build();
            }
            var command = UpdateUserCommandFromResourceAssembler
                    .toCommandFromResource(resource, currentUser.get());
            var user = userCommandService.handle(command, id);
            if (user.isEmpty()) {
                LOGGER.warn("No se pudo actualizar el usuario con ID: {}", id);
                return ResponseEntity.badRequest().build();
            }
            LOGGER.info("Usuario actualizado exitosamente con ID: {}", id);
            return ResponseEntity.ok(UserResourceFromEntityAssembler.toResourceFromEntity(user.get()));
        } catch (IllegalArgumentException e) {
            LOGGER.error("Validacion fallida al actualizar usuario {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Error al actualizar usuario con ID {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().body("Error al actualizar el usuario");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            userCommandService.handle(new DeleteUserCommand(id));
            LOGGER.info("Usuario eliminado exitosamente con ID: {}", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            LOGGER.error("Error al eliminar usuario con ID {}: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}