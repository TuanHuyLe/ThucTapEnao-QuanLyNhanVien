package com.enao.team2.quanlynhanvien.service;

import com.enao.team2.quanlynhanvien.model.GroupEntity;

import java.util.List;
import java.util.Optional;

public interface IGroupService {
    List<GroupEntity> findAll();
    Optional<GroupEntity> findByName(String name);
    GroupEntity save(GroupEntity groupEntity);
}
