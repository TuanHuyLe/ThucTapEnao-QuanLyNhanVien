package com.enao.team2.quanlynhanvien.model;

import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@Where(clause = "is_active=true")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleEntity extends AuditableEntity<String> {
    @Column
    private String name;

    @Column
    private String description;

    //many to many users
    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "UserRole",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @Getter(AccessLevel.NONE)
    private Set<UserEntity> users = new HashSet<>();

    //many to many permission
    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "RolePermission",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<PermissionEntity> permissions = new HashSet<>();
}
