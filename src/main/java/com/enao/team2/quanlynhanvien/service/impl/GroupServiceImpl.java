package com.enao.team2.quanlynhanvien.service.impl;

import com.enao.team2.quanlynhanvien.converter.GroupConverter;
import com.enao.team2.quanlynhanvien.dto.ResponseMessage;
import com.enao.team2.quanlynhanvien.model.GroupEntity;
import com.enao.team2.quanlynhanvien.repository.IGroupRepository;
import com.enao.team2.quanlynhanvien.service.IGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class GroupServiceImpl implements IGroupService {
    @Autowired
    private IGroupRepository groupRepository;

    @Autowired
    private GroupConverter groupConverter;

    @Override
    public List<GroupEntity> findAll() {
        return groupRepository.findAll();
    }

    @Override
    public Optional<GroupEntity> findByName(String name) {
        return groupRepository.findByName(name);
    }

    @Override
    public GroupEntity save(GroupEntity groupEntity) {
        return groupRepository.save(groupEntity);
    }

    @Override
    public ResponseMessage delete(UUID id) {
        ResponseMessage message = new ResponseMessage();
        GroupEntity entity = (GroupEntity) this.groupRepository.getOne(id);
        if (entity == null){
            message.setMessage("Không tìm thấy bản ghi!");
            return  message;
        }
        try {
            this.groupRepository.delete(entity);
            message.setMessage("Xóa thành công");
            return  message;
        } catch (Exception e){
            message.setMessage("Lỗi xóa: " + e);
            return  message;
        }
    }

    @Override
    public Page<GroupEntity> getByPage(int pageIndex, int pageSize) {
        Page<GroupEntity> page = this.groupRepository.findAll(PageRequest.of(pageIndex - 1, pageSize));
        return page;
    }

    @Override
    public GroupEntity findById(UUID id) {
        return this.groupRepository.getOne(id);
    }
}
