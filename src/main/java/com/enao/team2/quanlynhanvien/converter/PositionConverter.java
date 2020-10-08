package com.enao.team2.quanlynhanvien.converter;

import com.enao.team2.quanlynhanvien.dto.PositionDTO;
import com.enao.team2.quanlynhanvien.model.PermissionEntity;
import com.enao.team2.quanlynhanvien.model.PositionEntity;
import com.enao.team2.quanlynhanvien.model.UserEntity;
import com.enao.team2.quanlynhanvien.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Component
public class PositionConverter {
    @Autowired
    IUserService userService;

    public PositionEntity toEntity(PositionDTO positionDTO){
        PositionEntity positionEntity = new PositionEntity();
        if (positionDTO.getId() != null) {
            positionEntity.setId(positionDTO.getId());
        } else {
            positionEntity.setId(UUID.randomUUID());
        }
        positionEntity.setDescription(positionDTO.getDescription());
        positionEntity.setName(positionDTO.getName());
        if (positionDTO.getActive() != null) {
            positionEntity.setActive(positionDTO.getActive());
        } else {
            positionEntity.setActive(true);
        }
        return positionEntity;
    }

    public PositionDTO toDTO(PositionEntity positionEntity){
        Set<UserEntity> setUserEntity = positionEntity.getUsers();
        List<UserEntity> listUserEntity = new ArrayList<>(setUserEntity);
        List<String> listUserName = new ArrayList<>();
        for (UserEntity userEntity : listUserEntity) {
            listUserName.add(userEntity.getUsername());
        }
        PositionDTO positionDTO = new PositionDTO();
        positionDTO.setId(positionEntity.getId());
        positionDTO.setName(positionEntity.getName());
        positionDTO.setDescription(positionEntity.getDescription());
        positionDTO.setActive(positionEntity.getActive());
        positionDTO.setUserName(listUserName);
        return positionDTO;
    }
}
