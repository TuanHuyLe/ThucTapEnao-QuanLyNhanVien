package com.enao.team2.quanlynhanvien.service;

import com.enao.team2.quanlynhanvien.model.PositionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface IPositionService {

    Page<PositionEntity> findGroupsWithPredicate(String keyword, Pageable pageable);

    Page<PositionEntity> findAll(Pageable pageable);

    Optional<PositionEntity> findByName(String name);

    PositionEntity save(PositionEntity positionEntity);

    Optional<PositionEntity> findById(UUID id);

    PositionEntity deleteSoftById(PositionEntity dataDelete);
}
