package com.enao.team2.quanlynhanvien.model;

import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "permissions")
@Where(clause = "is_active=true")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PermissionEntity extends AuditableEntity<String> {
    @Column
    private String name;

    @Column
    private String description;

    @Column
    private UUID parent_id;

    //many to many role
    @ManyToMany(mappedBy = "permissions")
    private Set<RoleEntity> roles = new HashSet<>();
}
