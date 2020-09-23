package com.enao.team2.quanlynhanvien.service.impl;

import com.enao.team2.quanlynhanvien.repository.IPermissionRepository;
import com.enao.team2.quanlynhanvien.service.IPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PermissionServiceImpl implements IPermissionService {
    @Autowired
    private IPermissionRepository permissionRepository;
}
