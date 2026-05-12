package com.edifika.iam.authentication.interfaces.rest;

import com.edifika.iam.authentication.domain.services.UserCommandService;
import com.edifika.iam.authentication.interfaces.rest.resources.AuthenticatedUserResource;
import com.edifika.iam.authentication.interfaces.rest.resources.SignInResource;
import com.edifika.iam.authentication.interfaces.rest.resources.SignUpResource;
import com.edifika.iam.authentication.interfaces.rest.resources.UserResource;
import com.edifika.iam.authentication.interfaces.rest.transform.AuthenticatedUserResourceFromEntityAssembler;
import com.edifika.iam.authentication.interfaces.rest.transform.SignInCommandFromResourceAssembler;
import com.edifika.iam.authentication.interfaces.rest.transform.SignUpCommandFromResourceAssembler;
import com.edifika.iam.authentication.interfaces.rest.transform.UserResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para autenticación de usuarios.
 * Maneja el registro e inicio de sesión en Edifika.
 */
@RestController
@RequestMapping("/api/v1/authentication")
@Tag(name = "Authentication", description = "Endpoints de autenticación")
public class AuthenticationController {

    private final UserCommandService userCommandService;

    public AuthenticationController(UserCommandService userCommandService) {
        this.userCommandService = userCommandService;
    }

    @PostMapping("/sign-in")
    public ResponseEntity<AuthenticatedUserResource> signIn(@RequestBody SignInResource resource) {
        var command = SignInCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = userCommandService.handle(command);
        if (result.isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        var user = result.get().getLeft();
        var token = result.get().getRight();
        return ResponseEntity.ok(AuthenticatedUserResourceFromEntityAssembler.toResourceFromEntity(user, token));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<UserResource> signUp(@RequestBody SignUpResource resource) {
        var command = SignUpCommandFromResourceAssembler.toCommandFromResource(resource);
        var user = userCommandService.handle(command);
        if (user.isEmpty()) return ResponseEntity.badRequest().build();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UserResourceFromEntityAssembler.toResourceFromEntity(user.get()));
    }
}