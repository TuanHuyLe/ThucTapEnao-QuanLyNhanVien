package com.enao.team2.quanlynhanvien.config;

import com.enao.team2.quanlynhanvien.model.UserEntity;
import com.enao.team2.quanlynhanvien.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

public class MyAuditorAware implements AuditorAware<UUID> {
    @Autowired
    private IUserService userService;

    @Override
    public Optional<UUID> getCurrentAuditor() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<UserEntity> userEntity = userService.findByUsername(username);
        UserEntity user = userEntity.get();
        return Optional.of(user.getId());
    }
}
