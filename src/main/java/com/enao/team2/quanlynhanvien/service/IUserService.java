package com.enao.team2.quanlynhanvien.service;

import com.enao.team2.quanlynhanvien.dto.ResponseMessage;
import com.enao.team2.quanlynhanvien.model.UserEntity;

import java.util.List;
import java.util.UUID;

public interface IUserService {
    List<UserEntity> findAll();
    UserEntity save(UserEntity userEntity);
}
