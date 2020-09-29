package com.enao.team2.quanlynhanvien.controller;

import com.enao.team2.quanlynhanvien.converter.GroupConverter;
import com.enao.team2.quanlynhanvien.dto.GroupDTO;
import com.enao.team2.quanlynhanvien.model.GroupEntity;
import com.enao.team2.quanlynhanvien.service.IGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/")
public class GroupController {

    @Autowired
    private IGroupService groupService;

    @Autowired
    private GroupConverter groupConverter;

    @GetMapping(value = "/groups")
    public ResponseEntity<?> getAllGroup() {
        List<GroupEntity> groupEntities = groupService.findAll();
        List<GroupDTO> groupDTOs = new ArrayList<>();
        groupEntities.forEach(x -> groupDTOs.add(groupConverter.toDTO(x)));
        return ResponseEntity.ok(groupDTOs);
    }
}
