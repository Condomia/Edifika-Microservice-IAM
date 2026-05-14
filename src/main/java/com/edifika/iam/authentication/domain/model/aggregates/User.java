package com.edifika.iam.authentication.domain.model.aggregates;

import com.edifika.iam.authentication.domain.model.entities.Role;
import com.edifika.iam.authentication.domain.model.valueobjects.Roles;
import com.edifika.iam.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Aggregate principal del módulo IAM.
 * Representa a un usuario registrado en Edifika con su rol y estado.
 */
@Getter
@Entity
public class User extends AuditableAbstractAggregateRoot<User> {

    private String fullName;
    private String email;
    private String passwordHash;
    private String phone;
    private String status;

    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    protected User() {
        super();
        this.roles = new HashSet<>();
    }

    public User(String fullName, String email, String passwordHash, String phone, List<Role> roles) {
        this.fullName = fullName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.phone = phone;
        this.status = "ACTIVE";
        this.roles = new HashSet<>();
        addRoles(roles);
    }


    public void changePassword(String newPassword) { this.passwordHash = newPassword; }

    public void addRoles(List<Role> roles) {
        this.roles.addAll(Role.validateRoleSet(roles));
    }

    public boolean hasRole(Roles role) {
        return this.roles.stream().anyMatch(r -> r.getName() == role);
    }

    public void updateFullName(String fullName) {
        this.fullName = fullName;
    }

    public void updateEmail(String email) {
        this.email = email;
    }

    public void updatePhone(String phone) {
        this.phone = phone;
    }

    // activar usuario - residential management
    public void activate() { this.status = "ACTIVE"; }
    // desactivar usuario moroso - residential management
    public void deactivate() { this.status = "INACTIVE"; }

}