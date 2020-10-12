package com.enao.team2.quanlynhanvien.converter;

import com.enao.team2.quanlynhanvien.dto.AddUserDTO;
import com.enao.team2.quanlynhanvien.dto.UserDTO;
import com.enao.team2.quanlynhanvien.model.GroupEntity;
import com.enao.team2.quanlynhanvien.model.PositionEntity;
import com.enao.team2.quanlynhanvien.model.RoleEntity;
import com.enao.team2.quanlynhanvien.model.UserEntity;
import com.enao.team2.quanlynhanvien.service.IGroupService;
import com.enao.team2.quanlynhanvien.service.IPositionService;
import com.enao.team2.quanlynhanvien.service.IRoleService;
import com.enao.team2.quanlynhanvien.service.IUserService;
import com.enao.team2.quanlynhanvien.service.impl.UserDetailsImpl;
import com.enao.team2.quanlynhanvien.utils.SlugUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class UserConverter {
    @Autowired
    IRoleService roleService;

    @Autowired
    IUserService userService;

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
        for(String roleName : userDTO.getRoleName()){
            RoleEntity roleEntity = roleService.findByName(roleName).get();
            Set<RoleEntity> roleEntities = new HashSet<>();
            roleEntities.add(roleEntity);
            userEntity.setRoles(roleEntities);
        }
        return userEntity;
    }

    public UserEntity toEntityWhenAdd(AddUserDTO userDTO) {
        Optional<PositionEntity> positionEntity = positionService.findByName(userDTO.getPositionName());
        Optional<GroupEntity> groupEntity = groupService.findByName(userDTO.getGroupName());
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
        if(groupEntity.isPresent()) {
            userEntity.setGroup(groupEntity.get());
        }
        Set<RoleEntity> roleEntities = new HashSet<>();
        for(String roleName : userDTO.getRoleName()){
            RoleEntity roleEntity = roleService.findByName(roleName).get();
            roleEntities.add(roleEntity);
        }
        userEntity.setRoles(roleEntities);
        return userEntity;
    }

    public UserDTO toDTO(UserEntity userEntity) {
        Set<RoleEntity> userEntityRoles= userEntity.getRoles();
        List<RoleEntity> list = new ArrayList<>(userEntityRoles);
        List<String> roleName = new ArrayList<>();
        for (RoleEntity roleEntity : list){
            roleName.add(roleEntity.getName());
        }
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
            userDTO.setPositionName(userEntity.getPositions().getName());
        }
        userDTO.setRoleName(roleName);
        return userDTO;
    }
}
