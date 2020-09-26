package com.enao.team2.quanlynhanvien.converter;

import com.enao.team2.quanlynhanvien.dto.GroupDTO;
import com.enao.team2.quanlynhanvien.model.GroupEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class GroupConverter {
    public GroupEntity toEntity(GroupDTO groupDTO) {
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setId(UUID.randomUUID());
        groupEntity.setDescription(groupDTO.getDescription());
        groupEntity.setName(groupDTO.getName());
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
        return groupDTO;
    }
}