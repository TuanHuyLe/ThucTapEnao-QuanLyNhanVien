package com.enao.team2.quanlynhanvien.service.impl;

import com.enao.team2.quanlynhanvien.model.GroupEntity;
import com.enao.team2.quanlynhanvien.repository.IGroupRepository;
import com.enao.team2.quanlynhanvien.service.IGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupServiceImpl implements IGroupService {
    @Autowired
    private IGroupRepository groupRepository;

    @Override
    public List<GroupEntity> findAll() {
        return groupRepository.findAll();
    }

    @Override
    @Cacheable(cacheNames = "groups", condition = "#is_active = true")
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
    public List<GroupEntity> findByNameContaining(String name) {
        return groupRepository.findByNameContaining(name);
    }

}
