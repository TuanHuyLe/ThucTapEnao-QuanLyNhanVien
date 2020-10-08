package com.enao.team2.quanlynhanvien.converter;

import com.enao.team2.quanlynhanvien.dto.RoleDTO;
import com.enao.team2.quanlynhanvien.model.AuditableEntity;
import com.enao.team2.quanlynhanvien.model.PermissionEntity;
import com.enao.team2.quanlynhanvien.model.RoleEntity;
import com.enao.team2.quanlynhanvien.service.IPermissionService;
import com.enao.team2.quanlynhanvien.utils.SlugUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class RoleConverter {

    @Autowired
    IPermissionService permissionService;

    @Autowired
    private SlugUtils slugUtils;

    public RoleEntity toEntity(RoleDTO roleDTO) {
        RoleEntity roleEntity = new RoleEntity();
        List<PermissionEntity> listPermissionEntity = new ArrayList();
        List<String> listPermissionId = roleDTO.getPermissionIds();
        if(!listPermissionId.isEmpty()){
            for(String permissionId : listPermissionId){
                Optional<PermissionEntity> permissionEntity = permissionService.findByCode(permissionId);
                listPermissionEntity.add(permissionEntity.get());
            }
            Set<PermissionEntity> setPer = new HashSet<>(listPermissionEntity);
            roleEntity.setPermissions(setPer);
        }
        if (roleDTO.getId() != null) {
            roleEntity.setId(roleDTO.getId());
        } else {
            roleEntity.setId(UUID.randomUUID());
        }
        roleEntity.setDescription(roleDTO.getDescription());
        roleEntity.setName(roleDTO.getName());
        roleEntity.setSlug(slugUtils.slug(roleDTO.getName()));
        if (roleDTO.getActive() != null) {
            roleEntity.setActive(roleDTO.getActive());
        } else {
            roleEntity.setActive(true);
        }
        return roleEntity;
    }

    public RoleDTO toDTO(RoleEntity roleEntity) {
        Set<PermissionEntity> setPermissionEntity = roleEntity.getPermissions();
        List<PermissionEntity> listPermissionEntity = new ArrayList<>(setPermissionEntity);
        List<String> listPermissionIds = new ArrayList<>();
        for (PermissionEntity permissionEntity : listPermissionEntity) {
            listPermissionIds.add(permissionEntity.getCode());
        }
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(roleEntity.getId());
        roleDTO.setDescription(roleEntity.getDescription());
        roleDTO.setName(roleEntity.getName());
        roleDTO.setActive(roleEntity.getActive());
        roleDTO.setSlug(roleEntity.getSlug());
        roleDTO.setPermissionIds(listPermissionIds);
        return roleDTO;
    }

    public Page<RoleDTO> toPageDTO(Page<RoleEntity> pageEntity){
        Page<RoleDTO> pageDTO = pageEntity.map(this::toDTO);
        return pageDTO;
    }

}
