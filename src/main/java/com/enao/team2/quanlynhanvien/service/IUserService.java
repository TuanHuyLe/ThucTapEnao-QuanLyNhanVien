package com.enao.team2.quanlynhanvien.service;

import com.enao.team2.quanlynhanvien.model.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    List<UserEntity> findAll();

    Page<UserEntity> findAll(Pageable pageable);

    UserEntity save(UserEntity userEntity);

    Page<UserEntity> findUsersWithPredicate(String keyword, String type, Pageable pageable);

    Optional<UserEntity> findByUsername(String username);
}
