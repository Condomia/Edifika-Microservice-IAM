package com.edifika.iam.authentication.domain.model.commands;

import com.edifika.iam.authentication.domain.model.entities.Role;
import java.util.List;

/** Comando para registrar un nuevo usuario en Edifika. */
public record SignUpCommand(
        String fullName,
        String email,
        String password,
        String phone,
        List<Role> roles
) { }
