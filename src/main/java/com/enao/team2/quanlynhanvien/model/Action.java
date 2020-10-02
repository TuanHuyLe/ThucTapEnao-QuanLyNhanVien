package com.enao.team2.quanlynhanvien.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "action")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Action {
    @Id
    @Column(name = "code")
    private String code;
    @Column(name = "name")
    private String name;
    @OneToMany(
            mappedBy = "action",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<PermissionEntity> permissions = new HashSet<>();
}
