package com.enao.team2.quanlynhanvien.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "positions")
@Where(clause = "is_active=true")
@Data
public class PositionEntity extends AuditableEntity<UUID>{
    private String name;
    private String description;

    @OneToMany(
            mappedBy = "positions",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<UserEntity> users = new HashSet<>();
}
