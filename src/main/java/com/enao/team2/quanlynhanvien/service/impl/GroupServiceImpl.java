package com.enao.team2.quanlynhanvien.service.impl;

import com.enao.team2.quanlynhanvien.constants.Constants;
import com.enao.team2.quanlynhanvien.constants.ESearchKey;
import com.enao.team2.quanlynhanvien.constants.ESearchOperation;
import com.enao.team2.quanlynhanvien.dto.SearchCriteria;
import com.enao.team2.quanlynhanvien.generic.GenericSpecification;
import com.enao.team2.quanlynhanvien.generic.GroupSpecification;
import com.enao.team2.quanlynhanvien.model.GroupEntity;
import com.enao.team2.quanlynhanvien.repository.IGroupRepository;
import com.enao.team2.quanlynhanvien.service.IGroupService;
import com.enao.team2.quanlynhanvien.utils.SlugUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class GroupServiceImpl implements IGroupService {
    @Autowired
    private IGroupRepository groupRepository;

    @Autowired
    private SlugUtils slugUtils;

    @Autowired
    private GroupSpecification groupSpecification;

    @Override
    public List<GroupEntity> findAll() {
        return groupRepository.findAll();
    }

    @Override
    public Optional<GroupEntity> findByName(String name) {
        return groupRepository.findByName(name);
    }

    @CacheEvict(cacheNames = "groups", allEntries = true)
    public void flushCache() {
    }

    @Override
    public GroupEntity save(GroupEntity groupEntity) {
        return groupRepository.save(groupEntity);
    }

    @Override
    public Optional<GroupEntity> findById(UUID id) {
        return this.groupRepository.findById(id);
    }

    @Override
    public Boolean checkId(UUID id) {
        Optional<GroupEntity> groupEntity = this.groupRepository.findById(id);
        if (!groupEntity.isPresent()) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public Page<GroupEntity> findGroupsWithPredicate(String keyword, String[] type, Pageable pageable) {
        GenericSpecification<GroupEntity> genericSpecification = new GenericSpecification<>();
        for (String t : type) {
//            if (ESearchKey.name.name().equals(t)) {
//                genericSpecification.add(new SearchCriteria(ESearchKey.name.name(), keyword, ESearchOperation.MATCH, null));
//            } else if (ESearchKey.description.name().equals(t)) {
                if (Constants.VALID_FULL_NAME_REGEX.matcher(keyword).matches()) {
                    String slugKeyword = slugUtils.slug(keyword);
                    if (ESearchKey.name.name().equals(t)){
                        genericSpecification.add(new SearchCriteria(ESearchKey.slugname.name(), slugKeyword, ESearchOperation.MATCH, null));
                    } if (ESearchKey.description.name().equals(t)){
                        genericSpecification.add(new SearchCriteria(ESearchKey.slugdescription.name(), slugKeyword, ESearchOperation.MATCH, null));
                    }
                } else {
                    if (ESearchKey.name.name().equals(t)){
                        genericSpecification.add(new SearchCriteria(ESearchKey.name.name(), keyword, ESearchOperation.MATCH, null));
                    } if (ESearchKey.description.name().equals(t)){
                        genericSpecification.add(new SearchCriteria(ESearchKey.description.name(), keyword, ESearchOperation.MATCH, null));
                    }
                }
//            } else {
//                genericSpecification.add(new SearchCriteria(t, keyword, ESearchOperation.MATCH, null));
//            }
        }
        return groupRepository.findAll(genericSpecification, pageable);
    }

    @Override
    public Page<GroupEntity> findGroupsWithPredicate(String keyword, Pageable pageable) {
        Specification<GroupEntity> specification = Specification
                .where(groupSpecification.hasName(keyword))
                .or(groupSpecification.hasDescription(keyword));
        if (Constants.VALID_FULL_NAME_REGEX.matcher(keyword).matches()) {
            String slugKeyword = slugUtils.slug(keyword);
            specification = specification.or(groupSpecification.hasSlugName(slugKeyword));
            specification = specification.or(groupSpecification.hasSlugDescription(slugKeyword));
        } else {
            specification = specification.or(groupSpecification.hasName(keyword));
            specification = specification.or(groupSpecification.hasDescription(keyword));
        }

        return groupRepository.findAll(specification, pageable);
    }

    @Override
    public Page<GroupEntity> findAll(Pageable pageable) {
        return groupRepository.findAll(pageable);
    }

}