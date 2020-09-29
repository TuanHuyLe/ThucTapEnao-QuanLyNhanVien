package com.enao.team2.quanlynhanvien.model;

import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Where(clause = "is_active=true")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity extends AuditableEntity<String> {
    @Column(unique = true)
    private String username;

    @Column
    private String password;

    @Column
    private String fullName;

    @Column
    private Boolean gender;

    @Column
    private String email;

    @Column
    private String picture;

    @Column
    private String slug;

    //many to one group
    @ManyToOne(fetch = FetchType.LAZY)
    private GroupEntity group;

    //many to many role
    @ManyToMany(mappedBy = "users")
    private Set<RoleEntity> roles = new HashSet<>();

    //override
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserEntity)) {
            return false;
        }
        return null != getId() && getId().equals(((UserEntity) o).getId());
    }
}
