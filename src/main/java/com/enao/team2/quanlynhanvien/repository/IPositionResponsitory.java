package com.enao.team2.quanlynhanvien.repository;

import com.enao.team2.quanlynhanvien.model.PositionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface IPositionResponsitory extends JpaRepository<PositionEntity, UUID>, JpaSpecificationExecutor<PositionEntity> {
    Optional<PositionEntity> findByName(String name);
}
