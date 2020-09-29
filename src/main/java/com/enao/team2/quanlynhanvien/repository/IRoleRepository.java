package com.enao.team2.quanlynhanvien.repository;

import com.enao.team2.quanlynhanvien.constants.ERoles;
import com.enao.team2.quanlynhanvien.model.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IRoleRepository extends JpaRepository<RoleEntity, UUID> {
    Optional<RoleEntity> findByName(String name);
}
