package com.enao.team2.quanlynhanvien.service;

import com.enao.team2.quanlynhanvien.messages.MessageResponse;
import com.enao.team2.quanlynhanvien.model.RoleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IRoleService {
    List<RoleEntity> findAll();
    Optional<RoleEntity> findByName(String name);
    RoleEntity save(RoleEntity newRoleEntity);
    MessageResponse delete(UUID id);
    //Page<RoleEntity> getByPage(int pageIndex, int pageSize);
    Optional<RoleEntity> findById(UUID id);
    boolean checkId(UUID id);
    RoleEntity getOne(UUID id);
    Page<RoleEntity> findRolesWithPredicate(String keyword, String type[], Pageable pageable);
    Page<RoleEntity> findRolesWithPredicate(String keyword, Pageable pageable);
    Page<RoleEntity> findAll(Pageable pageable);
}
