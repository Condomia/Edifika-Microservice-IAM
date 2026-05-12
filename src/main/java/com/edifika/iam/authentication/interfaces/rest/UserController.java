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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controlador REST para la gestión de usuarios.
 * Permite listar, obtener, actualizar y eliminar usuarios.
 */
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Users", description = "Endpoints de gestión de usuarios")
public class UserController {

    private final UserQueryService userQueryService;
    private final UserCommandService userCommandService;

    public UserController(UserQueryService userQueryService, UserCommandService userCommandService) {
        this.userQueryService = userQueryService;
        this.userCommandService = userCommandService;
    }

    @GetMapping
    public ResponseEntity<List<UserResource>> getAllUsers() {
        var users = userQueryService.handle(new GetAllUsersQuery());
        var resources = users.stream()
                .map(UserResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResource> getUserById(@PathVariable Long id) {
        var user = userQueryService.handle(new GetUserByIdQuery(id));
        if (user.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(UserResourceFromEntityAssembler.toResourceFromEntity(user.get()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResource> updateUser(@PathVariable Long id,
                                                   @RequestBody UpdateUserResource resource) {
        var command = UpdateUserCommandFromResourceAssembler.toCommandFromResource(resource);
        var user = userCommandService.handle(command, id);
        if (user.isEmpty()) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(UserResourceFromEntityAssembler.toResourceFromEntity(user.get()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userCommandService.handle(new DeleteUserCommand(id));
        return ResponseEntity.noContent().build();
    }
}