package com.enao.team2.quanlynhanvien.service;

import com.enao.team2.quanlynhanvien.model.PermissionEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IPermissionService {
    List<PermissionEntity> findAll();
    Optional<PermissionEntity> findByName(String name);
    Optional<PermissionEntity> findById(UUID id);
}
