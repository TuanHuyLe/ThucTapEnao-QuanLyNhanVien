package com.enao.team2.quanlynhanvien.config;

import com.enao.team2.quanlynhanvien.model.UserEntity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.UUID;

@Configuration
@EnableJpaAuditing
public class AuditorConfig {
    @Bean
    public AuditorAware<UserEntity> auditorAware() {
        return new MyAuditorAware();
    }
}
