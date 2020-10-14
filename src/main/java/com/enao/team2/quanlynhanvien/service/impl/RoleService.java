package com.enao.team2.quanlynhanvien.service.impl;

import com.enao.team2.quanlynhanvien.constants.Constants;
import com.enao.team2.quanlynhanvien.constants.ESearchKey;
import com.enao.team2.quanlynhanvien.constants.ESearchOperation;
import com.enao.team2.quanlynhanvien.dto.SearchCriteria;
import com.enao.team2.quanlynhanvien.generic.GenericSpecification;
import com.enao.team2.quanlynhanvien.generic.RoleSpecification;
import com.enao.team2.quanlynhanvien.model.RoleEntity;
import com.enao.team2.quanlynhanvien.repository.IRoleRepository;
import com.enao.team2.quanlynhanvien.service.IRoleService;
import com.enao.team2.quanlynhanvien.utils.SlugUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import com.enao.team2.quanlynhanvien.messages.MessageResponse;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.ArrayList;
import java.util.UUID;

@Service
public class RoleService implements IRoleService {

    private static List<RoleEntity> DB = new ArrayList<>();

    @Autowired
    private IRoleRepository roleRepository;

    @Autowired
    private SlugUtils slugUtils;

    @Autowired
    private RoleSpecification roleSpecification;

    @Override
    public List<RoleEntity> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public Optional<RoleEntity> findByName(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    public RoleEntity save(RoleEntity newRoleEntity) {
        return roleRepository.save(newRoleEntity);
    }

    @Override
    public MessageResponse delete(UUID id) {
        MessageResponse messageResponse = new MessageResponse();
        RoleEntity roleEntity = this.roleRepository.getOne(id);
        Optional<RoleEntity> roleEntity1 = this.roleRepository.findById(id);
        try {
            if (roleEntity1.isPresent()) {
                roleEntity.setActive(false);
                roleRepository.save(roleEntity);
                messageResponse.setMessage("Delete successful!");
            } else
                messageResponse.setMessage("No record found!");
        }
        catch (Exception exception) {
            messageResponse.setMessage("Error deleting " + exception);
        }
        return messageResponse;
    }

//    @Override
//    public MessageResponse delete(UUID roleId) {
//        MessageResponse message = new MessageResponse();
//        RoleEntity entity = (RoleEntity) this.roleRepository.getOne(roleId);
//        Optional<RoleEntity> roleEntity = this.roleRepository.findById(roleId);
//        try {
//            if (!roleEntity.isPresent()) {
//                message.setMessage("Not Found!");
//            } else {
//                this.roleRepository.delete(entity);
//                message.setMessage("Delete success");
//            }
//        } catch (Exception e) {
//            message.setMessage("Lỗi xóa: " + e);
//        }
//        return message;
//    }

//    @Override
//    public Page<RoleEntity> getByPage(int pageIndex, int pageSize) {
//        Page<RoleEntity> page = this.roleRepository.findAll(PageRequest.of(pageIndex - 1, pageSize));
//        return page;
//    }

    @Override
    public Optional<RoleEntity> findById(UUID id) {
        return this.roleRepository.findById(id);
    }

    @Override
    public boolean checkId(UUID id) {
        Optional<RoleEntity> roleEntity = this.roleRepository.findById(id);
        if (!roleEntity.isPresent()) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public Page<RoleEntity> findRolesWithPredicate(String keyword, String type[], Pageable pageable) {
        GenericSpecification<RoleEntity> genericSpecification = new GenericSpecification<>();
        for (String t : type) {
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
        }
        return roleRepository.findAll(genericSpecification, pageable);
    }

    @Override
    public Page<RoleEntity> findRolesWithPredicate(String keyword, Pageable pageable) {
        Specification<RoleEntity> specification = Specification
                .where(roleSpecification.hasName(keyword))
                .or(roleSpecification.hasDescription(keyword));
        if (Constants.VALID_FULL_NAME_REGEX.matcher(keyword).matches()) {
            String slugKeyword = slugUtils.slug(keyword);
            specification = specification.or(roleSpecification.hasSlugName(slugKeyword));
            specification = specification.or(roleSpecification.hasSlugDescription(slugKeyword));
        } else {
            specification = specification.or(roleSpecification.hasName(keyword));
            specification = specification.or(roleSpecification.hasDescription(keyword));
        }

        return roleRepository.findAll(specification, pageable);
    }


    @Override
    public RoleEntity getOne(UUID id) {
        return this.roleRepository.getOne(id);
    }


    @Override
    public Page<RoleEntity> findAll(Pageable pageable) {
        return roleRepository.findAll(pageable);
    }


    @CacheEvict(cacheNames = "roles", allEntries = true)
    public void flushCache() {
    }
}
