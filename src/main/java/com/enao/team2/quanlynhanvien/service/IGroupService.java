package com.enao.team2.quanlynhanvien.service;

import com.enao.team2.quanlynhanvien.dto.ResponseMessage;
import com.enao.team2.quanlynhanvien.model.GroupEntity;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IGroupService {
    List<GroupEntity> findAll();
    Optional<GroupEntity> findByName(String name);
    GroupEntity save(GroupEntity groupEntity);
    ResponseMessage delete(UUID id);
    Page<GroupEntity> getByPage(int pageIndex, int pageSize);
    GroupEntity findById(UUID id);
}
