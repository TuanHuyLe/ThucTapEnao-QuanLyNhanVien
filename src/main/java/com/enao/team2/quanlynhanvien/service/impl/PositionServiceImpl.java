package com.enao.team2.quanlynhanvien.service.impl;

import com.enao.team2.quanlynhanvien.constants.ESearchKey;
import com.enao.team2.quanlynhanvien.constants.ESearchOperation;
import com.enao.team2.quanlynhanvien.dto.SearchCriteria;
import com.enao.team2.quanlynhanvien.generic.GenericSpecification;
import com.enao.team2.quanlynhanvien.model.PositionEntity;
import com.enao.team2.quanlynhanvien.repository.IPositionResponsitory;
import com.enao.team2.quanlynhanvien.service.IPositionService;
import com.enao.team2.quanlynhanvien.utils.SlugUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PositionServiceImpl implements IPositionService {
    @Autowired
    IPositionResponsitory positionResponsitory;

    @Autowired
    private SlugUtils slugUtils;

    @Override
    public Page<PositionEntity> findGroupsWithPredicate(String keyword, Pageable pageable) {
        String slugKeyword = slugUtils.slug(keyword);
        GenericSpecification<PositionEntity> genericSpecification = new GenericSpecification<>();
        genericSpecification.add(new SearchCriteria(ESearchKey.name.name(), keyword, ESearchOperation.MATCH, null));
        genericSpecification.add(new SearchCriteria(ESearchKey.slug.name(), slugKeyword, ESearchOperation.MATCH, null));
        return positionResponsitory.findAll(genericSpecification, pageable);
    }

    @Override
    public Page<PositionEntity> findAll(Pageable pageable) {
        return positionResponsitory.findAll(pageable);
    }

    @Override
    public Optional<PositionEntity> findByName(String name) {
        return positionResponsitory.findByName(name);
    }

    @Override
    public PositionEntity save(PositionEntity positionEntity) {
        return positionResponsitory.save(positionEntity);
    }

    @Override
    public Optional<PositionEntity> findById(UUID id) {
        return positionResponsitory.findById(id);
    }

    @Override
    public PositionEntity deleteSoftById(PositionEntity dataDelete) {
        dataDelete.setActive(false);
        positionResponsitory.save(dataDelete);
        return dataDelete;
    }
}
