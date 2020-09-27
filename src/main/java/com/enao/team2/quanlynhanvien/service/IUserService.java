package com.enao.team2.quanlynhanvien.service;

import com.enao.team2.quanlynhanvien.model.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IUserService {
    void flushCache();

    List<UserEntity> findAll();

    Page<UserEntity> findAll(Pageable pageable);

    UserEntity save(UserEntity userEntity);

    Page<UserEntity> findUsersWithPredicate(String keyword, Pageable pageable);
}
