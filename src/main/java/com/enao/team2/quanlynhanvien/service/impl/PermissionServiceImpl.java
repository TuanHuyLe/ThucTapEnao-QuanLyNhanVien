package com.enao.team2.quanlynhanvien.service.impl;

import com.enao.team2.quanlynhanvien.model.PermissionEntity;
import com.enao.team2.quanlynhanvien.repository.IPermissionRepository;
import com.enao.team2.quanlynhanvien.service.IPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PermissionServiceImpl implements IPermissionService {
    @Autowired
    private IPermissionRepository permissionRepository;

    @Override
    public List<PermissionEntity> findAll() {
        return permissionRepository.findAll();
    }

    @Override
    public Optional<PermissionEntity> findByName(String name) {
        return permissionRepository.findByName(name);
    }
}
