package com.edifika.iam.authentication.domain.services;

import com.edifika.iam.authentication.domain.model.entities.Role;
import com.edifika.iam.authentication.domain.model.queries.GetAllUsersQuery;
import java.util.List;

/**
 * Interfaz para consultar los roles disponibles en el sistema.
 */
public interface RoleQueryService {
    List<Role> handle(GetAllUsersQuery getAllUsersQuery);
}