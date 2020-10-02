package com.enao.team2.quanlynhanvien.controller;

import com.enao.team2.quanlynhanvien.dto.PermissionDTO;
import com.enao.team2.quanlynhanvien.model.PermissionEntity;
import com.enao.team2.quanlynhanvien.service.IPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class PermissionController {

    @Autowired
    private IPermissionService permissionService;

    @GetMapping("api/enao/permission")
    public List<PermissionDTO> listAllPermission(){
        List<PermissionEntity> listPermissionEntity = permissionService.findAll();
        List<PermissionDTO> listPermissionDTO = new ArrayList<>();
        for(PermissionEntity permissionEntity : listPermissionEntity){
            PermissionDTO permissionDTO = new PermissionDTO();
            permissionDTO.setCode(permissionEntity.getCode());
            permissionDTO.setName(permissionEntity.getName());
            listPermissionDTO.add(permissionDTO);
        }
        return listPermissionDTO;
    }

}
