package com.enao.team2.quanlynhanvien.converter;

import com.enao.team2.quanlynhanvien.dto.GroupDTO;
import com.enao.team2.quanlynhanvien.model.GroupEntity;
import com.enao.team2.quanlynhanvien.utils.SlugUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class GroupConverter {

    @Autowired
    private SlugUtils slugUtils;

    public GroupEntity toEntity(GroupDTO groupDTO) {
        GroupEntity groupEntity = new GroupEntity();
        if (groupDTO.getId() != null) {
            groupEntity.setId(groupDTO.getId());
        } else {
            groupEntity.setId(UUID.randomUUID());
        }
        groupEntity.setDescription(groupDTO.getDescription());
        groupEntity.setName(groupDTO.getName());
        groupEntity.setSlug(slugUtils.slug(groupDTO.getName() + "-" + groupDTO.getDescription()));
        if (groupDTO.getActive() != null) {
            groupEntity.setActive(groupDTO.getActive());
        } else {
            groupEntity.setActive(true);
        }
        return groupEntity;
    }

    public GroupDTO toDTO(GroupEntity groupEntity) {
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setId(groupEntity.getId());
        groupDTO.setDescription(groupEntity.getDescription());
        groupDTO.setName(groupEntity.getName());
        groupDTO.setActive(groupEntity.getActive());
        groupDTO.setSlug(groupEntity.getSlug());
        return groupDTO;
    }

    public Page<GroupDTO> toPageDTO(Page<GroupEntity> pageEntity){
        Page<GroupDTO> pageDTO = pageEntity.map(this::toDTO);
        return pageDTO;
    }
}