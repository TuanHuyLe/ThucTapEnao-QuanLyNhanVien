package com.enao.team2.quanlynhanvien.model;

import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "groups")
@Where(clause = "is_active=true")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GroupEntity extends AuditableEntity<UUID> {
    @Column
    private String name;

    @Column
    private String description;

    @Column
    private String slugname;

    @Column
    private String slugdescription;

    //one to many user
    @OneToMany(
            mappedBy = "group",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<UserEntity> users = new HashSet<>();
}
