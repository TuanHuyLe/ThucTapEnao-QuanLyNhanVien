package com.enao.team2.quanlynhanvien.service;

import com.enao.team2.quanlynhanvien.model.GroupEntity;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface IGroupService {
    List<GroupEntity> findAll();
    Optional<GroupEntity> findByName(String name);
    GroupEntity save(GroupEntity groupEntity);
    List<GroupEntity> findByNameContaining(String name);
}
