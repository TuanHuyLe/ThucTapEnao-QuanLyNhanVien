package com.enao.team2.quanlynhanvien.service.impl;

import com.enao.team2.quanlynhanvien.constants.ESearchKey;
import com.enao.team2.quanlynhanvien.constants.ESearchOperation;
import com.enao.team2.quanlynhanvien.converter.GroupConverter;
import com.enao.team2.quanlynhanvien.dto.GroupDTO;
import com.enao.team2.quanlynhanvien.dto.SearchCriteria;
import com.enao.team2.quanlynhanvien.generic.GenericSpecification;
import com.enao.team2.quanlynhanvien.messages.MessageResponse;
import com.enao.team2.quanlynhanvien.model.GroupEntity;
import com.enao.team2.quanlynhanvien.model.UserEntity;
import com.enao.team2.quanlynhanvien.repository.IGroupRepository;
import com.enao.team2.quanlynhanvien.repository.IUserRepository;
import com.enao.team2.quanlynhanvien.service.IGroupService;
import com.enao.team2.quanlynhanvien.utils.SlugUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class GroupServiceImpl implements IGroupService {
    @Autowired
    private IGroupRepository groupRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private GroupConverter groupConverter;

    @Autowired
    private SlugUtils slugUtils;

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
    public MessageResponse delete(UUID id) {
        MessageResponse message = new MessageResponse();
        GroupEntity entity = (GroupEntity) this.groupRepository.getOne(id);
        Optional<GroupEntity> groupEntity = this.groupRepository.findById(id);
        try {
            if (!groupEntity.isPresent()) {
                message.setMessage("Không tìm thấy bản ghi!");
            } else {
                this.groupRepository.delete(entity);
                message.setMessage("Xóa thành công");
            }
        } catch (Exception e) {
            message.setMessage("Lỗi xóa: " + e);
        }
        return message;
    }

    @Override
    public Page<GroupEntity> getByPage(int pageIndex, int pageSize) {
        Page<GroupEntity> page = this.groupRepository.findAll(PageRequest.of(pageIndex - 1, pageSize));
        return page;
    }

    @Override
    public Optional<GroupEntity> findById(UUID id) {
//        return this.groupRepository.getOne(id);
        return this.groupRepository.findById(id);
    }

    @Override
    public GroupEntity getOne(UUID id) {
        return this.groupRepository.getOne(id);
    }

    @Override
    public Page<GroupEntity> findGroupsWithPredicate(String keyword, Pageable pageable) {
        String slugKeyword = slugUtils.slug(keyword);
        GenericSpecification<GroupEntity> genericSpecification = new GenericSpecification<>();
        genericSpecification.add(new SearchCriteria(ESearchKey.name.name(), keyword, ESearchOperation.MATCH, null));
        genericSpecification.add(new SearchCriteria(ESearchKey.slug.name(), slugKeyword, ESearchOperation.MATCH, null));
        return groupRepository.findAll(genericSpecification, pageable);
    }

    @Override
    public Page<GroupEntity> findAll(Pageable pageable) {
        return groupRepository.findAll(pageable);
    }

}