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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para autenticación de usuarios.
 * Maneja el registro e inicio de sesión en Edifika.
 */
@RestController
@RequestMapping(value = "/api/v1/authentication", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Authentication", description = "Endpoints de autenticación")
public class AuthenticationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class);
    private final UserCommandService userCommandService;

    public AuthenticationController(UserCommandService userCommandService) {
        this.userCommandService = userCommandService;
    }

    @PostMapping("/sign-in")
    public ResponseEntity<AuthenticatedUserResource> signIn(@RequestBody SignInResource resource) {
        try {
            var command = SignInCommandFromResourceAssembler.toCommandFromResource(resource);
            var result = userCommandService.handle(command);
            if (result.isEmpty()) {
                LOGGER.warn("Sign in fallido para el email: {}", resource.email());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            var user = result.get().getLeft();
            var token = result.get().getRight();
            LOGGER.info("Sign in exitoso para el email: {}", resource.email());
            return ResponseEntity.ok(
                    AuthenticatedUserResourceFromEntityAssembler.toResourceFromEntity(user, token));
        } catch (Exception e) {
            LOGGER.error("Error en sign in para el email {}: {}", resource.email(), e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/sign-up")
    public ResponseEntity<UserResource> signUp(@RequestBody SignUpResource resource) {
        try {
            var command = SignUpCommandFromResourceAssembler.toCommandFromResource(resource);
            var user = userCommandService.handle(command);
            if (user.isEmpty()) {
                LOGGER.warn("Sign up fallido para el email: {}", resource.email());
                return ResponseEntity.badRequest().build();
            }
            LOGGER.info("Usuario registrado exitosamente: {}", resource.email());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(UserResourceFromEntityAssembler.toResourceFromEntity(user.get()));
        } catch (Exception e) {
            LOGGER.error("Error en sign up para el email {}: {}", resource.email(), e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}