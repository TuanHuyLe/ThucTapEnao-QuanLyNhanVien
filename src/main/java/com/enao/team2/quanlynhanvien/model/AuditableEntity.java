package com.enao.team2.quanlynhanvien.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Data
public abstract class AuditableEntity<T> implements Serializable {
    @Id
    @Column(unique = true)
    private UUID id;

    @CreatedBy
    @Column(updatable = false)
    private T createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    @Column(updatable = false)
    private Date createdDate;

    @LastModifiedBy
    @Column(insertable = false)
    private T modifiedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    @Column(insertable = false)
    private Date modifiedDate;

    @Column(name = "is_active", columnDefinition = "boolean default true")
    private Boolean active;
}

