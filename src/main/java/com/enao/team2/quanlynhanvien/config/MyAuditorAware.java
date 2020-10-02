package com.enao.team2.quanlynhanvien.config;

import com.enao.team2.quanlynhanvien.service.impl.UserDetailsImpl;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

public class MyAuditorAware implements AuditorAware<UUID> {

    @Override
    public Optional<UUID> getCurrentAuditor() {
        UserDetailsImpl userDetails = (UserDetailsImpl)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Optional.of(userDetails.getId());
    }
}
