package com.enao.team2.quanlynhanvien.service;

import com.enao.team2.quanlynhanvien.model.RoleEntity;

import java.util.List;
import java.util.Optional;

public interface IRoleService {
    List<RoleEntity> findAll();
    Optional<RoleEntity> findByName(String name);
}
