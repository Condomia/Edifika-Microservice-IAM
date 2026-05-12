package com.edifika.iam.authentication.interfaces.rest;

import com.edifika.iam.authentication.domain.model.entities.Role;
import com.edifika.iam.authentication.infrastructure.persistence.jpa.repositories.RoleRepository;
import com.edifika.iam.authentication.interfaces.rest.resources.RoleResource;
import com.edifika.iam.authentication.interfaces.rest.transform.RoleResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controlador REST para consultar los roles disponibles en el sistema.
 */
@RestController
@RequestMapping(value = "/api/v1/roles", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Roles", description = "Endpoints de roles del sistema")
public class RolesController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RolesController.class);
    private final RoleRepository roleRepository;

    public RolesController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @GetMapping
    public ResponseEntity<List<RoleResource>> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        LOGGER.info("Consultando roles disponibles, total: {}", roles.size());
        var resources = roles.stream()
                .map(RoleResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }
}