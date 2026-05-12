package com.edifika.iam.authentication.infrastructure.persistence.jpa.repositories;

import com.edifika.iam.authentication.domain.model.aggregates.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad User.
 * Permite buscar usuarios por email y verificar su existencia.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
