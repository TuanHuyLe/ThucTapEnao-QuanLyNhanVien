package com.enao.team2.quanlynhanvien.converter;

import com.enao.team2.quanlynhanvien.dto.AddUserDTO;
import com.enao.team2.quanlynhanvien.dto.UserDTO;
import com.enao.team2.quanlynhanvien.model.GroupEntity;
import com.enao.team2.quanlynhanvien.model.PositionEntity;
import com.enao.team2.quanlynhanvien.model.UserEntity;
import com.enao.team2.quanlynhanvien.service.IGroupService;
import com.enao.team2.quanlynhanvien.service.IPositionService;
import com.enao.team2.quanlynhanvien.utils.SlugUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class UserConverter {
    @Autowired
    IGroupService groupService;

    @Autowired
    IPositionService positionService;

    @Autowired
    private SlugUtils slugUtils;

    public UserEntity toEntity(UserDTO userDTO) {
        Optional<PositionEntity> positionEntity = positionService.findByName(userDTO.getPositionName());
        Optional<GroupEntity> groupEntity = groupService.findByName(userDTO.getPositionName());
        UserEntity userEntity = new UserEntity();
        if(userDTO.getId() == null){
            userEntity.setId(UUID.randomUUID());
        }else{
            userEntity.setId(userDTO.getId());
        }
        userEntity.setUsername(userDTO.getUsername());
        userEntity.setFullName(userDTO.getFullName());
        userEntity.setEmail(userDTO.getEmail());
        userEntity.setGender(userDTO.getGender());
        userEntity.setSlug(slugUtils.slug(userDTO.getFullName()));
        if (null != userDTO.getActive()) {
            userEntity.setActive(userDTO.getActive());
        } else {
            userEntity.setActive(true);
        }
        if (null != userDTO.getGender()) {
            userEntity.setGender(userDTO.getGender());
        } else {
            userEntity.setGender(true);
        }
        if(positionEntity.isPresent()){
            userEntity.setPositions(positionEntity.get());
        }
        if(groupEntity.isPresent()){
            userEntity.setGroup(groupEntity.get());
        }
        return userEntity;
    }

    public UserEntity toEntityWhenAdd(AddUserDTO userDTO) {
        Optional<PositionEntity> positionEntity = positionService.findByName(userDTO.getPositionName());
        Optional<GroupEntity> groupEntity = groupService.findByName(userDTO.getPositionName());
        UserEntity userEntity = new UserEntity();
        userEntity.setId(UUID.randomUUID());
        userEntity.setUsername(userDTO.getUsername());
        userEntity.setPassword(BCrypt.hashpw(userDTO.getPassword(), BCrypt.gensalt(12)));
        userEntity.setFullName(userDTO.getFullName());
        userEntity.setEmail(userDTO.getEmail());
        userEntity.setGender(userDTO.getGender());
        userEntity.setSlug(slugUtils.slug(userDTO.getFullName()));
        if (userDTO.getActive() != null) {
            userEntity.setActive(userDTO.getActive());
        } else {
            userEntity.setActive(true);
        }
        if (userDTO.getGender() != null) {
            userEntity.setGender(userDTO.getGender());
        } else {
            userEntity.setGender(true);
        }
        if(positionEntity.isPresent()){
            userEntity.setPositions(positionEntity.get());
        }
        if(groupEntity.isPresent()){
            userEntity.setGroup(groupEntity.get());
        }
        return userEntity;
    }

    public UserDTO toDTO(UserEntity userEntity) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userEntity.getId());
        userDTO.setUsername(userEntity.getUsername());
        userDTO.setFullName(userEntity.getFullName());
        userDTO.setEmail(userEntity.getEmail());
        userDTO.setGender(userEntity.getGender());
        userDTO.setActive(userEntity.getActive());
        userDTO.setSlug(userEntity.getSlug());
        if (userEntity.getGroup() != null) {
            userDTO.setGroupName(userEntity.getGroup().getName());
        }
        if (userEntity.getPositions() != null) {
            userDTO.setGroupName(userEntity.getPositions().getName());
        }
        return userDTO;
    }
}
