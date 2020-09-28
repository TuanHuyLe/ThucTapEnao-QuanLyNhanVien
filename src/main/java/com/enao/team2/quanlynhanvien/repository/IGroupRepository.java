package com.enao.team2.quanlynhanvien.repository;

import com.enao.team2.quanlynhanvien.model.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IGroupRepository extends JpaRepository<GroupEntity, UUID >, JpaSpecificationExecutor<GroupEntity>{
    Optional<GroupEntity> findByName(String name);
}
