package com.enao.team2.quanlynhanvien.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "permission")
@Getter
@Setter
@NoArgsConstructor
<<<<<<< HEAD
public class PermissionEntity extends AuditableEntity<UUID> {
    @Column
=======
@AllArgsConstructor
public class PermissionEntity {
    @Id
    @Column(name = "code")
    private String code;
    @Column(name = "name")
>>>>>>> d6be2a2eef559f52c9c323b4146ff78b19952a68
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    private Action action;
    @ManyToOne(fetch = FetchType.LAZY)
    private Module module;
    @ManyToMany(mappedBy = "permissions")
    private Set<RoleEntity> roles = new HashSet<>();
}
