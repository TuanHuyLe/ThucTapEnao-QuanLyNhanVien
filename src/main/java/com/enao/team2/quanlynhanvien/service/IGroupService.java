package com.enao.team2.quanlynhanvien.service;

import com.enao.team2.quanlynhanvien.messages.MessageResponse;
import com.enao.team2.quanlynhanvien.model.GroupEntity;
import com.enao.team2.quanlynhanvien.model.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IGroupService {
    List<GroupEntity> findAll();
    Optional<GroupEntity> findByName(String name);
    GroupEntity save(GroupEntity groupEntity);
    Optional<GroupEntity> findById(UUID id);
    Boolean checkId(UUID id);
    Page<GroupEntity> findGroupsWithPredicate(String keyword, String type[], Pageable pageable);
    Page<GroupEntity> findGroupsWithPredicate(String keyword, Pageable pageable);
    Page<GroupEntity> findAll(Pageable pageable);
}