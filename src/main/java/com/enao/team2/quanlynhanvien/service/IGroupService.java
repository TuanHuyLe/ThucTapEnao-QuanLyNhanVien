package com.enao.team2.quanlynhanvien.service;

import com.enao.team2.quanlynhanvien.dto.GroupDTO;
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
    MessageResponse delete(UUID id);
    Page<GroupEntity> getByPage(int pageIndex, int pageSize);
    Optional<GroupEntity> findById(UUID id);
    GroupEntity getOne(UUID id);
    Page<GroupEntity> findGroupsWithPredicate(String keyword, Pageable pageable);
    Page<GroupEntity> findAll(Pageable pageable);
}