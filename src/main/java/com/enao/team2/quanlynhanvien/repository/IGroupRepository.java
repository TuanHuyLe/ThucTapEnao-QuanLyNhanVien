package com.enao.team2.quanlynhanvien.repository;


import com.enao.team2.quanlynhanvien.model.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IGroupRepository extends JpaRepository<GroupEntity, UUID> {
    Optional<GroupEntity> findByName(String name);

    @Query("select g from GroupEntity g where g.name like ?1")
    List<GroupEntity> searchByName(String keyword);
}
