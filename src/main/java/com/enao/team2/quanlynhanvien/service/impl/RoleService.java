package com.enao.team2.quanlynhanvien.service.impl;

import com.enao.team2.quanlynhanvien.constants.ESearchKey;
import com.enao.team2.quanlynhanvien.constants.ESearchOperation;
import com.enao.team2.quanlynhanvien.dto.SearchCriteria;
import com.enao.team2.quanlynhanvien.generic.GenericSpecification;
import com.enao.team2.quanlynhanvien.model.GroupEntity;
import com.enao.team2.quanlynhanvien.model.RoleEntity;
import com.enao.team2.quanlynhanvien.repository.IRoleRepository;
import com.enao.team2.quanlynhanvien.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import com.enao.team2.quanlynhanvien.messages.MessageResponse;
import com.enao.team2.quanlynhanvien.model.RoleEntity;
import com.enao.team2.quanlynhanvien.repository.IRoleRepository;
import com.enao.team2.quanlynhanvien.service.IRoleService;
import com.enao.team2.quanlynhanvien.utils.SlugUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RoleService implements IRoleService {

    private static List<RoleEntity> DB = new ArrayList<>();

    @Autowired
    private IRoleRepository roleRepository;

    @Autowired
    private SlugUtils slugUtils;

    @Override
    public List<RoleEntity> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public Optional<RoleEntity> findByName(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    public RoleEntity save(RoleEntity newRoleEntity) { return roleRepository.save(newRoleEntity); }

    @Override
    public MessageResponse delete(UUID roleId) {
        MessageResponse message = new MessageResponse();
        RoleEntity entity = (RoleEntity) this.roleRepository.getOne(roleId);
        Optional<RoleEntity> roleEntity = this.roleRepository.findById(roleId);
        try {
            if (!roleEntity.isPresent()) {
                message.setMessage("Not Found!");
            } else {
                this.roleRepository.delete(entity);
                message.setMessage("Delete success");
            }
        } catch (Exception e) {
            message.setMessage("Lỗi xóa: " + e);
        }
        return message;
    }

    @Override
    public Page<RoleEntity> getByPage(int pageIndex, int pageSize) {
        Page<RoleEntity> page = this.roleRepository.findAll(PageRequest.of(pageIndex - 1, pageSize));
        return page;
    }

    @Override
    public Optional<RoleEntity> findById(UUID id) {
        return this.roleRepository.findById(id);
    }

    @Override
    public RoleEntity getOne(UUID id) {
        return this.roleRepository.getOne(id);
    }

    @Override
    public Page<RoleEntity> findRolesWithPredicate(String keyword, Pageable pageable) {
        String slugKeyword = slugUtils.slug(keyword);
        GenericSpecification<RoleEntity> genericSpecification = new GenericSpecification<>();
        genericSpecification.add(new SearchCriteria(ESearchKey.name.name(), keyword, ESearchOperation.MATCH, null));
        genericSpecification.add(new SearchCriteria(ESearchKey.slug.name(), slugKeyword, ESearchOperation.MATCH, null));
        return roleRepository.findAll(genericSpecification, pageable);
    }


    @Override
    public Page<RoleEntity> findAll(Pageable pageable) {
        return roleRepository.findAll(pageable);
    }


    @CacheEvict(cacheNames = "roles", allEntries = true)
    public void flushCache() {
    }
}
