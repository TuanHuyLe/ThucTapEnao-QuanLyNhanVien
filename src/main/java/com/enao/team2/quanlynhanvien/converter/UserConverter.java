package com.enao.team2.quanlynhanvien.converter;

import com.enao.team2.quanlynhanvien.dto.UserDTO;
import com.enao.team2.quanlynhanvien.model.GroupEntity;
import com.enao.team2.quanlynhanvien.model.UserEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserConverter {
    public UserEntity toEntity(UserDTO userDTO) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(UUID.randomUUID());
        userEntity.setUsername(userDTO.getUsername());
        userEntity.setPassword(userDTO.getPassword());
        userEntity.setFullName(userDTO.getFullName());
        userEntity.setEmail(userDTO.getEmail());
        userEntity.setGender(userDTO.getGender());
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
        return userEntity;
    }

    public UserDTO toDTO(UserEntity userEntity) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userEntity.getId());
        userDTO.setUsername(userEntity.getUsername());
        userDTO.setPassword(userEntity.getPassword());
        userDTO.setFullName(userEntity.getFullName());
        userDTO.setEmail(userEntity.getEmail());
        userDTO.setGender(userEntity.getGender());
        userDTO.setActive(userEntity.getActive());
        return userDTO;
    }
}
