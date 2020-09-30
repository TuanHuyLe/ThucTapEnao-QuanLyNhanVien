package com.enao.team2.quanlynhanvien.config;

import com.enao.team2.quanlynhanvien.model.UserEntity;
import com.enao.team2.quanlynhanvien.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

public class MyAuditorAware implements AuditorAware<UserEntity> {
    @Autowired
    private IUserService userService;

    @Override
    public Optional<UserEntity> getCurrentAuditor() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.findByUsername(username);
    }
}
