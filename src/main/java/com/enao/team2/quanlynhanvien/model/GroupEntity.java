package com.enao.team2.quanlynhanvien.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "groups")
@Where(clause = "is_active=true")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupEntity extends AuditableEntity<String> {
    @Column
    private String name;

    @Column
    private String description;

    //one to many user
    @OneToMany(
            mappedBy = "group",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<UserEntity> users = new HashSet<>();
}
