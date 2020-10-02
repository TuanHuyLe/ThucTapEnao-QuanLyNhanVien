package com.enao.team2.quanlynhanvien.repository;

import com.enao.team2.quanlynhanvien.model.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IPermissionRepository extends JpaRepository<PermissionEntity, UUID> {
    Optional<PermissionEntity> findByName(String name);
}
