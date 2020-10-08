package com.enao.team2.quanlynhanvien.service;

import com.enao.team2.quanlynhanvien.model.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IUserService {
    /**
     * get all user trong DB
     * @return list user entity
     */
    List<UserEntity> findAll();

    /**
     * get tat ca user su dung phan trang
     * @param pageable phan trang
     * @return page user entity
     */
    Page<UserEntity> findAll(Pageable pageable);

    /**
     * luu hoac cap nhat thay doi user
     * @param userEntity doi tuong duoc luu hoac cap nhat
     * @return doi tuong vua duoc luu hoac cap nhat
     */
    UserEntity save(UserEntity userEntity);

    /**
     * tim kiem co type
     * @param keyword gia tri can tim
     * @param type loai gia tri can tim
     * @param pageable phan trang
     * @return page user entity
     */
    Page<UserEntity> findUsersWithPredicate(String keyword, String[] type, Pageable pageable);

    /**
     * tim kiem khong co type - tim kiem tat ca
     * @param keyword gia tri can tim
     * @param pageable loai gia tri can tim
     * @return page user entity
     */
    Page<UserEntity> findUsersWithPredicate(String keyword, Pageable pageable);

    /**
     * tim kiem user theo username
     * @param username chuoi username can tim
     * @return doi tuong user kieu optional
     */
    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findById(UUID id);

    UserEntity deleteSoftById(UserEntity dataDelete);
}
