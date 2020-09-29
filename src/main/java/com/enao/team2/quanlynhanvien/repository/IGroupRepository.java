package com.enao.team2.quanlynhanvien.repository;

import com.enao.team2.quanlynhanvien.model.GroupEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IGroupRepository extends JpaRepository<GroupEntity, UUID> {
    Optional<GroupEntity> findByName(String name);

    List<GroupEntity> findByNameContaining(String name);
}
