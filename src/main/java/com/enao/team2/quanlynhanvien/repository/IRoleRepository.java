package com.enao.team2.quanlynhanvien.repository;

import com.enao.team2.quanlynhanvien.model.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IRoleRepository extends JpaRepository<RoleEntity, UUID> {
}
