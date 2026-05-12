package com.edifika.iam.authentication.interfaces.rest;

import com.edifika.iam.authentication.domain.model.entities.Role;
import com.edifika.iam.authentication.infrastructure.persistence.jpa.repositories.RoleRepository;
import com.edifika.iam.authentication.interfaces.rest.resources.RoleResource;
import com.edifika.iam.authentication.interfaces.rest.transform.RoleResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controlador REST para consultar los roles disponibles en el sistema.
 */
@RestController
@RequestMapping("/api/v1/roles")
@Tag(name = "Roles", description = "Endpoints de roles del sistema")
public class RolesController {

    private final RoleRepository roleRepository;

    public RolesController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @GetMapping
    public ResponseEntity<List<RoleResource>> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        var resources = roles.stream()
                .map(RoleResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }
}